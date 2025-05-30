package main.java.board;

import main.java.pieces.Piece;
import main.java.player.Player;

public class Square
{
    private final char file;
    private final char rank;

    private Piece piece;

    protected Square( char file, char rank )
    {
        this.file = file;
        this.rank = rank;
    }

    public char getFile()
    {
        return this.file;
    }

    public char getRank()
    {
        return this.rank;
    }

    public Piece getPiece()
    {
        return this.piece;
    }

    protected void setPiece( Piece piece )
    {
        this.piece = piece;
    }

    @Override
    public String toString()
    {
        return String.format( "%s%s", getFile(), getRank() );
    }

    /**
     * Creates a new square that has the same file and rank as {@code this}. No piece is assigned to
     * the new square; that is, the new square is unoccupied.
     * 
     * @return a new square that has the same file and rank as {@code this}
     */
    @Override
    protected Square clone()
    {
        return new Square( getFile(), getRank() );
    }

    /**
     * Returns a boolean indicating whether or not this square is occupied.
     * 
     * @return {@code true} if this square contains a piece; {@code false} otherwise
     */
    public boolean isOccupied()
    {
        return getPiece() != null;
    }

    /**
     * Returns a boolean indicating whether or not this square is occupied by a piece belonging to
     * the given player.
     * 
     * @param player the player
     * @return {@code true} if this square contains a piece belonging to the given player;
     *         {@code false} otherwise
     */
    public boolean isOccupiedBy( Player player )
    {
        return player.getPieces().contains( getPiece() );
    }

    /**
     * Returns a boolean indicating whether or not this square is occupied by a piece <i>not</i>
     * belonging to the given player.
     * 
     * @param player the player
     * @return {@code true} if this square contains a piece <i>not</i> belonging to the given
     *         player; {@code false} otherwise
     */
    public boolean isOccupiedByOpponent( Player player )
    {
        return isOccupied() && !isOccupiedBy( player );
    }

    /**
     * Returns a boolean indicating whether or not this square is being targeted by a piece
     * <i>not</i> belonging to the given player.
     * 
     * @param player the player
     * @param board  the chess board
     * @return {@code true} if this square is being targeted by a piece <i>not</i> belonging to the
     *         given player; {@code false} otherwise
     */
    public boolean isTargeted( Player player, Board board )
    {
        return board.getPieces()
                    .stream()
                    .filter( pc -> pc.getPlayer() != player )
                    .anyMatch( pc -> pc.isTargeting( this, board ) );
    }

    public boolean isOnLastRank( Player player )
    {
        return switch ( player.getColour() )
        {
            case WHITE -> getRank() == '8';
            case BLACK -> getRank() == '1';
        };
    }

    public Square travel( Board board, int x, int y )
    {
        return board.getSquare( (char) (getFile() + x), (char) (getRank() + y) );
    }

    public int fileDiff( Square square )
    {
        return getFile() - square.getFile();
    }

    public int rankDiff( Square square )
    {
        return getRank() - square.getRank();
    }

    public int getParity()
    {
        return getParity( getFile(), getRank() );
    }

    public static int getParity( char file, char rank )
    {
        return (file + rank) % 2;
    }

    public int getIndex()
    {
        return getIndex( getFile(), getRank() );
    }

    public static int getIndex( char file, char rank )
    {
        return 8 * (file - 'a') + (rank - '1');
    }
}
