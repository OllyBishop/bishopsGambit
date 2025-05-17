package main.java.pieces;

import java.util.Arrays;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.player.Player;
import main.java.player.Player.Colour;

public abstract class Piece
{
    public static Piece newInstance( Typ type, Player player, char file, char rank )
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
     * The player this piece belongs to.
     * <p>
     * Given an arbitrary {@code Player p}, then {@code this.player == p} if and only if
     * {@code p.getPieces().contains( this )}.
     */
    private final Player player;

    private final char startFile;
    private final char startRank;

    public Player getPlayer()
    {
        return this.player;
    }

    public Colour getColour()
    {
        return getPlayer().getColour();
    }

    public int getSign()
    {
        return getPlayer().getSign();
    }

    protected Piece( Player player, char startFile, char startRank )
    {
        this.player = player;

        this.startFile = startFile;
        this.startRank = startRank;

        getPlayer().getPieces().add( this );
    }

    @Override
    public String toString()
    {
        return String.format( "%s %s", getColour(), getType() );
    }

    public char toChar()
    {
        return (char) ('\u2654' + 6 * getColour().ordinal() + getType().ordinal());
    }

    public abstract Typ getType();

    /**
     * Returns the value of this piece as an integer.
     * 
     * @return the value of this piece as an integer
     */
    public abstract int getValue();

    /**
     * Returns a list of all squares this piece is currently targeting; i.e., all squares this piece
     * could move to if checks are ignored.
     * 
     * @param board the chess board
     * @return a list of all squares this piece could move to if checks are ignored
     */
    protected abstract List<Square> getTargets( Board board );

    /**
     * Returns a list of all squares this piece can legally move to. The list returned is a filtered
     * version of {@code getTargets(Board)}.
     * 
     * @param board the chess board
     * @return a list of all squares this piece can legally move to
     */
    public List<Square> getMoves( Board board )
    {
        Square from = getSquare( board );
        return getTargets( board ).stream()
                                  .filter( to -> !getPlayer().isInCheck( board.cloneAndMove( from, to ) ) )
                                  .toList();
    }

    public Square getStartSquare( Board board )
    {
        return board.getSquare( startFile, startRank );
    }

    /**
     * Finds the square this piece is occupying.
     * 
     * @param board the chess board
     * @return the square this piece is occupying (if it exists); {@code null} otherwise
     */
    public Square getSquare( Board board )
    {
        return board.stream()
                    .filter( s -> s.getPiece() == this )
                    .findAny()
                    .orElse( null );
    }

    /**
     * Returns a boolean indicating whether (the square occupied by) this piece is being targeted by
     * an enemy piece.
     * 
     * @param board the chess board
     * @return {@code true} if this piece is being targeted; {@code false} otherwise
     */
    public boolean isTargeted( Board board )
    {
        return getSquare( board ).isTargeted( getPlayer(), board );
    }

    /**
     * Returns a boolean indicating whether this piece is targeting the given square.
     * 
     * @param square the square
     * @param board  the chess board
     * @return {@code true} if this piece is targeting the given square; {@code false} otherwise
     */
    public boolean isTargeting( Square square, Board board )
    {
        return getTargets( board ).contains( square );
    }

    public boolean canPromote( Square square )
    {
        return this instanceof Pawn && square.isOnLastRank( getPlayer() );
    }

    public boolean movedTwoSquaresForward( Square from, Square to )
    {
        return to.fileDiff( from ) == 0 &&
               to.rankDiff( from ) == 2 * getSign();
    }

    public boolean movedOneSquareDiagonallyForward( Square from, Square to )
    {
        return Math.abs( to.fileDiff( from ) ) == 1 &&
               to.rankDiff( from ) == getSign();
    }

    public boolean movedTwoSquaresHorizontally( Square from, Square to )
    {
        return Math.abs( to.fileDiff( from ) ) == 2 &&
               to.rankDiff( from ) == 0;
    }

    /**
     * Returns a boolean indicating whether this piece is one of the given <b>types</b>.
     * 
     * @param types the piece types to check
     * @return {@code true} if this piece is one of the given types; {@code false} otherwise
     */
    public boolean isType( Typ... types )
    {
        return Arrays.asList( types ).contains( getType() );
    }

    public enum Typ
    {
        KING( "King" ),
        QUEEN( "Queen" ),
        ROOK( "Rook" ),
        BISHOP( "Bishop" ),
        KNIGHT( "Knight" ),
        PAWN( "Pawn" );

        public static final Typ[] PROMOTION_TYPES = new Typ[] { KNIGHT, BISHOP, ROOK, QUEEN };

        private final String str;

        private Typ( String str )
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
