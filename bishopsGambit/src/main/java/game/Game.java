package main.java.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.pieces.Pawn;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
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

        if ( activePlayer.isCheckmated( board ) )
            setStatus( Status.CHECKMATE );

        else if ( activePlayer.isStalemated( board ) )
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

        System.out.printf( "%s has %d legal move%s.", activePlayer, n, n == 1 ? "" : "s" );

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
        return getBoard( getNumberOfTurnsTaken() );
    }

    public Board getBoard( int index )
    {
        return boards.get( index );
    }

    /**
     * Returns the number of turns taken in this game.
     * 
     * @return the number of turns taken in this game
     */
    public int getNumberOfTurnsTaken()
    {
        return boards.size() - 1;
    }

    /**
     * Returns the player whose turn it currently is.
     * 
     * @return White if the number of turns taken is even; Black if it is odd
     */
    public Player getActivePlayer()
    {
        return getNumberOfTurnsTaken() % 2 == 0 ? white : black;
    }

    private String[] parseUci( String uci )
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
     * @return the new piece (if promoting); {@code null} otherwise
     */
    public Piece makeMove( String uci )
    {
        String[] values = parseUci( uci );

        Board board = getBoard();

        Square from = board.getSquare( values[ 0 ] );
        Square to = board.getSquare( values[ 1 ] );

        return switch ( values[ 2 ] )
        {
            case "n" -> makeMove( from, to, Typ.KNIGHT );
            case "b" -> makeMove( from, to, Typ.BISHOP );
            case "r" -> makeMove( from, to, Typ.ROOK );
            case "q" -> makeMove( from, to, Typ.QUEEN );

            default -> makeMove( from, to );
        };
    }

    public Piece makeMove( Square from, Square to )
    {
        return makeMove( from, to, null );
    }

    /**
     * Clones the current board (returned by {@code getBoard()}) and moves the piece occupying the
     * <b>from</b> square to the <b>to</b> square. Also handles the following special moves (if
     * applicable):
     * <ul>
     * <li>Castling – Moves the corresponding rook to the square adjacent to the king.</li>
     * <li>En passant – Removes the corresponding pawn.</li>
     * <li>Promotion – Replaces the piece with a new piece of the given type.</li>
     * </ul>
     * 
     * @param from    the square containing the piece to be moved
     * @param to      the destination square for the piece
     * @param newType the new piece type (if promoting)
     * @return the new piece (if promoting); {@code null} otherwise
     * @throws UnoccupiedSquareException if the <b>from</b> square is unoccupied
     * @throws IllegalMoveException      if the piece cannot legally move to the <b>to</b> square
     * @throws InvalidPromotionException if <b>newType</b> is null and promotion is mandatory, or
     *                                   <b>newType</b> is non-null and either
     *                                   <ul>
     *                                   <li>the piece occupying <b>from</b> is not a pawn,</li>
     *                                   <li><b>to</b> is not on the player's last rank, or</li>
     *                                   <li><b>newType</b> is not a valid promotion type</li>
     *                                   </ul>
     */
    public Piece makeMove( Square from, Square to, Typ newType )
    {
        if ( !from.isOccupied() )
            throw new UnoccupiedSquareException( from );

        if ( !getBoard().isLegalMove( from, to ) )
            throw new IllegalMoveException( from, to );

        Board newBoard = getBoard().cloneAndMove( from, to );

        Piece piece = from.getPiece();
        Piece newPiece;

        if ( newType == null )
        {
            if ( piece.canPromote( to ) )
                throw new InvalidPromotionException( "Promotion is mandatory, but no new piece type was specified." );

            newPiece = null;
        }
        else
        {
            if ( !(piece instanceof Pawn) )
            {
                String msg = String.format( "The promoting piece (%s) must be a Pawn.", piece );
                throw new InvalidPromotionException( msg );
            }

            if ( !to.isOnLastRank( piece.getPlayer() ) )
            {
                String msg = String.format( "The promotion square (%s) must be on %s's last rank.", to, piece.getPlayer() );
                throw new InvalidPromotionException( msg );
            }

            if ( !newType.isValidPromotionType() )
            {
                String msg = String.format( "The new piece type (%s) must be one of Knight, Bishop, Rook or Queen.", newType );
                throw new InvalidPromotionException( msg );
            }

            newPiece = newBoard.promote( (Pawn) piece, newType );
        }

        addBoard( newBoard );

        if ( isGameOver() )
            System.out.println( getGameOverMessage() );

        return newPiece;
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
