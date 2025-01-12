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
        Board board = new Board();
        board.assignPieces( white );
        board.assignPieces( black );
        addBoard( board );
    }

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
        return boards.get( getNumberOfMovesMade() );
    }

    public int getNumberOfMovesMade()
    {
        return boards.size() - 1;
    }

    /**
     * Returns the player whose turn it is.
     * 
     * @return White if the number of moves made is even; Black if it is odd
     */
    public Player getActivePlayer()
    {
        return getNumberOfMovesMade() % 2 == 0 ? white : black;
    }

    /**
     * Returns the opponent of the player whose turn it is.
     * 
     * @return Black if the number of moves made is even; White if it is odd
     */
    public Player getInactivePlayer()
    {
        return getNumberOfMovesMade() % 2 == 0 ? black : white;
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
     * @return the promoted piece (if applicable); otherwise {@code null}
     */
    public Piece makeMove( String uci )
    {
        String[] values = parseUCI( uci );

        Board board = getBoard();

        Square fromSquare = board.getSquare( values[ 0 ] );
        Square toSquare = board.getSquare( values[ 1 ] );

        Typ promType = switch ( values[ 2 ] )
        {
            case "" -> null;
            case "n" -> Typ.KNIGHT;
            case "b" -> Typ.BISHOP;
            case "r" -> Typ.ROOK;
            case "q" -> Typ.QUEEN;

            default -> throw new InvalidPromotionException( values[ 2 ] );
        };

        return makeMove( fromSquare, toSquare, promType );
    }

    public Piece makeMove( Square fromSquare, Square toSquare, Typ promType )
    {
        Board newBoard = move( fromSquare, toSquare );
        Piece promPiece = promote( newBoard, toSquare, promType );

        addBoard( newBoard );

        if ( isGameOver() )
            System.out.println( getGameOverMessage() );

        return promPiece;
    }

    private Board move( Square fromSquare, Square toSquare )
    {
        if ( !fromSquare.isOccupied() )
            throw new UnoccupiedSquareException( fromSquare );

        Board board = getBoard();

        if ( !board.isLegalMove( fromSquare, toSquare ) )
            throw new IllegalMoveException( fromSquare, toSquare );

        return board.move( fromSquare, toSquare );
    }

    private Piece promote( Board newBoard, Square toSquare, Typ promType )
    {
        if ( promType == null )
            return null;

        char file = toSquare.getFile();
        char rank = toSquare.getRank();

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
            case CHECKMATE -> getInactivePlayer() + " wins by checkmate.";
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
