package io.github.ollybishop.bishopsgambit.game;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.model.piece.Pawn;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;

public class Game
{
    private final List<Board> boardStateHistory = new ArrayList<>();

    private final Player white = new Player( Player.Colour.WHITE );
    private final Player black = new Player( Player.Colour.BLACK );

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
     * Adds the given <b>board</b> to the board state history after a ply has been played.
     * <p>
     * The board state history allows previous positions to be viewed and may be used to support
     * undoing moves. The number of stored board states also determines which player's turn it is.
     * 
     * @param board the new board state after the ply
     */
    private void addBoard( Board board )
    {
        boardStateHistory.add( board );
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
     * Returns the number of plies played in this game.
     * <p>
     * A ply is a single move made by one player. The number of plies played is derived from the
     * size of the board state history, excluding the initial board state.
     * 
     * @return the number of plies played in this game
     */
    public int getNumberOfPliesPlayed()
    {
        return boardStateHistory.size() - 1;
    }

    public Board getBoard( int index )
    {
        return boardStateHistory.get( index );
    }

    /**
     * Returns the active board state.
     * <p>
     * The active board state is the latest board in the board state history.
     * 
     * @return the active board state
     */
    public Board getActiveBoard()
    {
        return getBoard( getNumberOfPliesPlayed() );
    }

    /**
     * Returns the player whose turn it currently is.
     * 
     * @return White if the number of plies played is even; Black if it is odd
     */
    public Player getActivePlayer()
    {
        return getNumberOfPliesPlayed() % 2 == 0 ? white : black;
    }

    /**
     * @param uci a string representing the move in Universal Chess Interface (UCI) notation
     * @return the new piece (if promoting); {@code null} otherwise
     */
    Piece makeMove( String uci )
    {
        Object[] moveInfo = getMoveInfo( uci, getActiveBoard() );

        Square from = (Square) moveInfo[ 0 ];
        Square to = (Square) moveInfo[ 1 ];
        Piece.Type newType = (Piece.Type) moveInfo[ 2 ];

        return makeMove( from, to, newType );
    }

    private Object[] getMoveInfo( String uci, Board board )
    {
        if ( uci == null )
            throw new IllegalArgumentException( "UCI string cannot be null." );

        if ( uci.length() < 4 || uci.length() > 5 )
            throw new IllegalArgumentException( "UCI string must be 4 or 5 characters in length." );

        if ( !Board.isValidSquare( uci.charAt( 0 ), uci.charAt( 1 ) ) )
            throw new IllegalArgumentException( "First & second characters in UCI string must represent a valid square." );

        if ( !Board.isValidSquare( uci.charAt( 2 ), uci.charAt( 3 ) ) )
            throw new IllegalArgumentException( "Third & fourth characters in UCI string must represent a valid square." );

        Square from = board.getSquare( uci.charAt( 0 ), uci.charAt( 1 ) );
        Square to = board.getSquare( uci.charAt( 2 ), uci.charAt( 3 ) );

        if ( uci.length() == 4 )
            return new Object[] { from, to, null };

        Piece.Type newType = switch ( uci.charAt( 4 ) )
        {
            case 'n' -> Piece.Type.KNIGHT;
            case 'b' -> Piece.Type.BISHOP;
            case 'r' -> Piece.Type.ROOK;
            case 'q' -> Piece.Type.QUEEN;

            default -> throw new IllegalArgumentException( "Fifth character in UCI string must represent a valid piece." );
        };

        return new Object[] { from, to, newType };
    }

    /**
     * Validates the requested move, then clones the board returned by {@link Game#getActiveBoard()}
     * and moves the piece occupying {@code from} to {@code to}. Also handles the following special
     * moves, if applicable:
     * <ul>
     * <li>Castling: Moves the corresponding rook to the square adjacent to the king.</li>
     * <li>En passant: Removes the captured pawn.</li>
     * <li>Promotion: Replaces the pawn with a new piece of the given type.</li>
     * </ul>
     * 
     * @param from    the square containing the piece to be moved
     * @param to      the destination square
     * @param newType the new piece type when promoting; {@code null} otherwise
     * @return the promoted piece, or {@code null} if no promotion occurs
     * @throws NoPieceOnSquareException  if {@code from} is unoccupied
     * @throws IllegalMoveException      if the piece does not belong to the active player, or
     *                                   cannot legally move to {@code to}
     * @throws InvalidPromotionException if {@code newType} is {@code null} when promotion is
     *                                   mandatory, or is non-null and any of the following apply:
     *                                   <ul>
     *                                   <li>the piece occupying {@code from} is not a pawn,</li>
     *                                   <li>{@code to} is not on the active player's last rank,
     *                                   or</li>
     *                                   <li>{@code newType} is not a valid promotion type</li>
     *                                   </ul>
     */
    public Piece makeMove( Square from, Square to, Piece.Type newType )
    {
        if ( !from.isOccupied() )
            throw new NoPieceOnSquareException( from );

        Piece piece = from.getPiece();
        Player activePlayer = getActivePlayer();

        if ( piece.getPlayer() != activePlayer )
            throw IllegalMoveException.wrongPlayer( piece, activePlayer );

        Board activeBoard = getActiveBoard();

        if ( !activeBoard.isLegalMove( from, to ) )
            throw IllegalMoveException.illegalPieceMove( from, to );

        Board newBoard = activeBoard.cloneAndMove( from, to );

        Piece newPiece;

        if ( newType == null )
        {
            if ( piece.canPromote( to ) )
                throw new InvalidPromotionException( "Promotion is mandatory, but no new piece type was specified." );

            newPiece = null;
        }
        else
        {
            if ( !( piece instanceof Pawn ) )
            {
                String msg = String.format( "The promoting piece (%s) must be a Pawn.", piece );
                throw new InvalidPromotionException( msg );
            }

            if ( !to.isOnLastRank( activePlayer ) )
            {
                String msg = String.format( "The promotion square (%s) must be on %s's last rank.", to, activePlayer );
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
