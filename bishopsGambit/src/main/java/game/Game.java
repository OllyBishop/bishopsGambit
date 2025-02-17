package main.java.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.pieces.Bishop;
import main.java.pieces.Knight;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.pieces.Queen;
import main.java.pieces.Rook;
import main.java.player.Player;
import main.java.player.Player.Colour;

public class Game
{
    private final List<Board> boards = new ArrayList<>();

    private final Player white = new Player( Colour.WHITE );
    private final Player black = new Player( Colour.BLACK );

    private Status status;

    public Status getStatus()
    {
        return this.status;
    }

    private void setStatus( Status status )
    {
        this.status = status;
    }

    public Game()
    {
        System.out.println( "New game started." );

        Board board = new Board();
        board.assignPieces( white );
        board.assignPieces( black );
        addBoard( board );
    }

    /**
     * This method is called whenever a move is made. By storing a list of board states throughout
     * the game, we can view how the board looked on a previous turn and (potentially) undo moves.
     * The number of boards stored is used to determine which player's turn it is.
     * 
     * @param board the new {@code Board} after a move was made
     */
    private void addBoard( Board board )
    {
        boards.add( board );
        updateStatus( board );

        printBoardInfo( board );
    }

    private void updateStatus( Board board )
    {
        Player activePlayer = getActivePlayer();

        if ( activePlayer.isInCheckmate( board ) )
            setStatus( Status.CHECKMATE );

        else if ( activePlayer.isInStalemate( board ) )
            setStatus( Status.STALEMATE );

        else if ( board.hasInsufficientMaterial() )
            setStatus( Status.INSUFFICIENT_MATERIAL );

        else if ( activePlayer.isInCheck( board ) )
            setStatus( Status.CHECK );

        else
            setStatus( Status.DEFAULT );
    }

    private void printBoardInfo( Board board )
    {
        Player activePlayer = getActivePlayer();
        int n = activePlayer.getNumberOfLegalMoves( board );

        if ( n == 1 )
            System.out.printf( "%s has 1 legal move.", activePlayer );
        else
            System.out.printf( "%s has %d legal moves.", activePlayer, n );

        int diff = board.getMaterialDifference();

        if ( diff != 0 )
            System.out.printf( " %s: +%d", diff > 0 ? white : black, Math.abs( diff ) );

        System.out.println();
    }

    /**
     * Returns the current board.
     * 
     * @return the current board
     */
    public Board getBoard()
    {
        return getBoard( getNumberOfMovesMade() );
    }

    public Board getBoard( int index )
    {
        return boards.get( index );
    }

    public int getNumberOfMovesMade()
    {
        return boards.size() - 1;
    }

    /**
     * Returns the player whose turn it currently is.
     * 
     * @return White if the number of moves made is even; Black if it is odd
     */
    public Player getActivePlayer()
    {
        return getNumberOfMovesMade() % 2 == 0 ? white : black;
    }

    private String[] parseUCI( String uci )
    {
        if ( uci == null )
            throw new IllegalArgumentException( "String cannot be null." );

        if ( uci.length() < 4 || uci.length() > 5 )
            throw new IllegalArgumentException( "String must be 4 or 5 characters in length." );

        String value1 = uci.substring( 0, 2 );
        String value2 = uci.substring( 2, 4 );
        String value3 = uci.substring( 4 );

        if ( !Board.isValidSquare( value1 ) )
            throw new IllegalArgumentException( "First pair of characters '" + value1 + "' must represent a valid square." );

        if ( !Board.isValidSquare( value2 ) )
            throw new IllegalArgumentException( "Second pair of characters '" + value2 + "' must represent a valid square." );

        if ( !Arrays.asList( "", "n", "b", "r", "q" ).contains( value3 ) )
            throw new IllegalArgumentException( "Fifth character '" + value3 + "' must be empty or represent a valid piece." );

        return new String[] { value1, value2, value3 };
    }

    /**
     * @param uci a string representing the move in Universal Chess Interface (UCI) notation
     * @return the promoted piece (if applicable); {@code null} otherwise
     */
    public Piece makeMove( String uci )
    {
        String[] values = parseUCI( uci );

        Board board = getBoard();

        Square from = board.getSquare( values[ 0 ] );
        Square to = board.getSquare( values[ 1 ] );

        Typ promType = switch ( values[ 2 ] )
        {
            case "n" -> Typ.KNIGHT;
            case "b" -> Typ.BISHOP;
            case "r" -> Typ.ROOK;
            case "q" -> Typ.QUEEN;

            default -> null;
        };

        return makeMove( from, to, promType );
    }

    public Piece makeMove( Square from, Square to, Typ promType )
    {
        Board newBoard = move( from, to );
        Piece promPiece = promote( newBoard, to, promType );

        addBoard( newBoard );

        if ( isGameOver() )
            System.out.println( getGameOverMessage() );

        return promPiece;
    }

    /**
     * Moves the piece occupying the given <b>from</b> square to the given <b>to</b> square. Handles
     * special moves (castling, en passant) by moving or removing the relevant piece (rook, pawn).
     * 
     * @param from the square containing the piece to be moved
     * @param to   the destination square for the piece
     * @return the new {@code Board} object
     * @throws UnoccupiedSquareException if the from square is unoccupied
     * @throws IllegalMoveException      if the piece occupying the from square cannot legally move
     *                                   to the to square
     */
    private Board move( Square from, Square to )
    {
        if ( !from.isOccupied() )
            throw new UnoccupiedSquareException( from );

        Board board = getBoard();

        if ( !board.isLegalMove( from, to ) )
            throw new IllegalMoveException( from, to );

        return board.move( from, to );
    }

    private Piece promote( Board newBoard, Square square, Typ promType )
    {
        if ( promType == null )
            return null;

        char file = square.getFile();
        char rank = square.getRank();

        Piece promPiece = switch ( promType )
        {
            case KNIGHT -> new Knight( getActivePlayer(), file, rank );
            case BISHOP -> new Bishop( getActivePlayer(), file, rank );
            case ROOK -> new Rook( getActivePlayer(), file, rank );
            case QUEEN -> new Queen( getActivePlayer(), file, rank );

            default -> throw new InvalidPromotionException( promType );
        };

        newBoard.getSquare( file, rank ).setPiece( promPiece );

        return promPiece;
    }

    public boolean isGameOver()
    {
        return getStatus() == Status.CHECKMATE ||
               getStatus() == Status.STALEMATE ||
               getStatus() == Status.INSUFFICIENT_MATERIAL;
    }

    public String getGameOverMessage()
    {
        return switch ( getStatus() )
        {
            case CHECKMATE -> getActivePlayer().getColour().transpose() + " wins by checkmate.";
            case STALEMATE -> "Game drawn by stalemate.";
            case INSUFFICIENT_MATERIAL -> "Game drawn by insufficient material.";

            default -> null;
        };
    }

    public enum Status
    {
        DEFAULT, CHECK, CHECKMATE, STALEMATE, INSUFFICIENT_MATERIAL;
    }
}
