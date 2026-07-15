package io.github.ollybishop.bishopsgambit.model.piece;

import java.util.Arrays;
import java.util.List;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.util.StreamUtils;

public abstract class Piece
{
    public static Piece newInstance( Type type, Player player, char file, char rank )
    {
        return switch ( type )
        {
            case PAWN -> new Pawn( player, file, rank );
            case KNIGHT -> new Knight( player, file, rank );
            case BISHOP -> new Bishop( player, file, rank );
            case ROOK -> new Rook( player, file, rank );
            case QUEEN -> new Queen( player, file, rank );
            case KING -> new King( player, file, rank );
        };
    }

    /**
     * The player that owns this piece.
     * <p>
     * This is the same player whose piece list, returned by {@link Player#getPieces()}, contains this piece.
     */
    private final Player player;

    private final char startFile;
    private final char startRank;

    public Player getPlayer()
    {
        return player;
    }

    public Player.Colour getColour()
    {
        return getPlayer().getColour();
    }

    /**
     * Returns the sign of the player that owns this piece.
     * 
     * @return {@code 1} for a white piece; {@code -1} for a black piece
     */
    public int getPlayerSign()
    {
        return getPlayer().getSign();
    }

    public Board.Side getBoardSide()
    {
        if ( startFile <= 'd' )
            return Board.Side.QUEENSIDE;

        return Board.Side.KINGSIDE;
    }

    Piece( Player player, char startFile, char startRank )
    {
        if ( !Board.isValidSquare( startFile, startRank ) )
            throw new IllegalArgumentException();

        this.player = player;

        this.startFile = startFile;
        this.startRank = startRank;

        getPlayer().getPieces().add( this );
    }

    @Override
    public String toString()
    {
        return "%s %s".formatted( getColour(), getType() );
    }

    public char toChar()
    {
        return (char) ( '\u2654' + 6 * getColour().ordinal() + 5 - getType().ordinal() );
    }

    public abstract Type getType();

    /**
     * Returns this piece's material value.
     * 
     * @return this piece's material value
     */
    public abstract int getValue();

    /**
     * Returns this piece's candidate squares on the given board.
     * <p>
     * Candidate squares follow this piece's movement rules and are used as the basis for both controlled squares and
     * pseudo-legal destination squares.
     * 
     * @param board                  the chessboard
     * @param includeFriendlySquares whether to include squares occupied by friendly pieces
     * @return this piece's candidate squares
     */
    abstract List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares );

    /**
     * Returns the squares controlled by this piece on the given board.
     * <p>
     * A square is controlled by this piece if this piece attacks or defends that square according to its movement rules,
     * regardless of whether this piece could legally move there.
     * 
     * @param board the chessboard
     * @return the squares controlled by this piece
     */
    List<Square> getControlledSquares( Board board )
    {
        return getCandidateSquares( board, true );
    }

    /**
     * Returns the pseudo-legal destination squares available to this piece on the given board.
     * <p>
     * Pseudo-legal destination squares follow this piece's movement rules and exclude squares occupied by friendly pieces. They
     * are not filtered to exclude moves that would leave the moving player in check.
     * 
     * @param board the chessboard
     * @return the squares this piece can pseudo-legally move to
     */
    List<Square> getPseudoLegalMoves( Board board )
    {
        return getCandidateSquares( board, false );
    }

    /**
     * Returns the legal destination squares available to this piece on the given board.
     * <p>
     * Legal destination squares are obtained by filtering this piece's pseudo-legal destination squares to exclude any move
     * that would leave the moving player in check.
     * 
     * @param board the chessboard
     * @return the squares this piece can legally move to
     */
    public List<Square> getLegalMoves( Board board )
    {
        Player player = getPlayer();
        Square from = getSquare( board );

        return getPseudoLegalMoves( board ).stream()
                                           .filter( to -> !board.moveWouldLeavePlayerInCheck( player, from, to ) )
                                           .toList();
    }

    public Square getStartSquare( Board board )
    {
        return board.getSquare( startFile, startRank );
    }

    /**
     * Returns the square occupied by this piece on the given board.
     * 
     * @param board the chessboard
     * @return the square occupied by this piece, or {@code null} if this piece is not on the board
     */
    public Square getSquare( Board board )
    {
        return StreamUtils.findOnlyOrNull( board.stream().filter( square -> square.getPiece() == this ) );
    }

    /**
     * Returns whether this piece is under attack.
     * <p>
     * A piece is under attack if the square it occupies is controlled by any opposing piece.
     * 
     * @param board the chessboard
     * @return {@code true} if this piece is under attack; {@code false} otherwise
     */
    public boolean isUnderAttack( Board board )
    {
        return getSquare( board ).isControlledByOpponentOf( getPlayer(), board );
    }

    /**
     * Returns whether this piece controls the given square.
     * 
     * @param square the square to check
     * @param board  the chessboard
     * @return {@code true} if this piece controls the given square; {@code false} otherwise
     */
    public boolean controls( Square square, Board board )
    {
        return getControlledSquares( board ).contains( square );
    }

    public boolean canPromote( Square square )
    {
        return this instanceof Pawn && square.isOnLastRank( getPlayer() );
    }

    public boolean movedTwoSquaresForward( Square from, Square to )
    {
        return to.fileDiff( from ) == 0 &&
               to.rankDiff( from ) == 2 * getPlayerSign();
    }

    public boolean movedOneSquareDiagonallyForward( Square from, Square to )
    {
        return Math.abs( to.fileDiff( from ) ) == 1 &&
               to.rankDiff( from ) == getPlayerSign();
    }

    public boolean movedTwoSquaresHorizontally( Square from, Square to )
    {
        return Math.abs( to.fileDiff( from ) ) == 2 &&
               to.rankDiff( from ) == 0;
    }

    public enum Type
    {
        PAWN( "Pawn" ),
        KNIGHT( "Knight" ),
        BISHOP( "Bishop" ),
        ROOK( "Rook" ),
        QUEEN( "Queen" ),
        KING( "King" );

        public static final Type[] PROMOTION_TYPES = new Type[] { KNIGHT, BISHOP, ROOK, QUEEN };

        private final String str;

        private Type( String str )
        {
            this.str = str;
        }

        @Override
        public String toString()
        {
            return this.str;
        }

        public boolean isValidPromotionType()
        {
            return Arrays.asList( PROMOTION_TYPES ).contains( this );
        }
    }
}
