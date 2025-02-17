package main.java.pieces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.player.Player;
import main.java.player.Player.Colour;

public abstract class Piece
{
    private final Player player; // References the player this piece belongs to

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

    public Piece( Player player, char startFile, char startRank )
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
     * could move to (assuming checks are ignored).
     * 
     * @param board the chess board
     * @return a list of all squares this piece is currently targeting
     */
    public abstract List<Square> getTargets( Board board );

    /**
     * Returns a list of all squares this piece can legally move to. The squares returned are a
     * subset of the squares returned by {@code getTargets()}, with any moves that would result in
     * check removed.
     * 
     * @param board the chess board
     * @return a list of all squares this piece can legally move to
     */
    public List<Square> getMoves( Board board )
    {
        List<Square> targets = new ArrayList<>( getTargets( board ) );
        Square from = getSquare( board );
        targets.removeIf( to -> getPlayer().isInCheck( board.move( from, to ) ) );
        return targets;
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
        return getSquare( board ).isTargeted( board, getPlayer() );
    }

    /**
     * Returns a boolean indicating whether this piece is targeting the given square.
     * 
     * @param board  the chess board
     * @param square the square
     * @return {@code true} if this piece is targeting the given square; {@code false} otherwise
     */
    public boolean isTargeting( Board board, Square square )
    {
        return getTargets( board ).contains( square );
    }

    public boolean canPromote( Board board, Square square )
    {
        return this instanceof Pawn && square.travel( board, 0, getSign() ) == null;
    }

    public boolean movedTwoSquaresForward( Square from, Square to )
    {
        int fileDiff = to.fileDiff( from );
        int rankDiff = to.rankDiff( from );
        return fileDiff == 0 && rankDiff == 2 * getSign();
    }

    public boolean movedOneSquareDiagonallyForward( Square from, Square to )
    {
        int fileDiff = to.fileDiff( from );
        int rankDiff = to.rankDiff( from );
        return Math.abs( fileDiff ) == 1 && rankDiff == getSign();
    }

    public boolean movedTwoSquaresLaterally( Square from, Square to )
    {
        int fileDiff = to.fileDiff( from );
        int rankDiff = to.rankDiff( from );
        return Math.abs( fileDiff ) == 2 && rankDiff == 0;
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

        public static final Typ[] PROMOTION_OPTIONS = new Typ[] { KNIGHT, BISHOP, ROOK, QUEEN };

        private final String str;

        Typ( String str )
        {
            this.str = str;
        }

        @Override
        public String toString()
        {
            return this.str;
        }
    }
}
