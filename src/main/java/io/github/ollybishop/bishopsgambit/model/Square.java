package io.github.ollybishop.bishopsgambit.model;

import io.github.ollybishop.bishopsgambit.model.piece.Piece;

public class Square
{
    private final char file;
    private final char rank;

    private Piece piece;

    Square( char file, char rank )
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

    void setPiece( Piece piece )
    {
        this.piece = piece;
    }

    @Override
    public String toString()
    {
        return String.format( "%s%s", getFile(), getRank() );
    }

    /**
     * Returns a new, unoccupied square with the same file and rank as this square.
     *
     * @return a new, unoccupied square with the same file and rank as this square
     */
    Square copyWithoutPiece()
    {
        return new Square( getFile(), getRank() );
    }

    /**
     * Returns whether this square is empty.
     * 
     * @return {@code true} if this square is empty; {@code false} otherwise
     */
    public boolean isEmpty()
    {
        return getPiece() == null;
    }

    /**
     * Returns whether this square is occupied by a piece.
     * 
     * @return {@code true} if this square is occupied by a piece; {@code false} otherwise
     */
    public boolean isOccupied()
    {
        return !isEmpty();
    }

    /**
     * Returns whether this square is occupied by a piece belonging to the given player.
     * 
     * @param player the player to check
     * @return {@code true} if this square is occupied by a piece belonging to the given player;
     *         {@code false} otherwise
     */
    public boolean isOccupiedBy( Player player )
    {
        return isOccupied() && getPiece().getPlayer() == player;
    }

    /**
     * Returns whether this square is occupied by a piece belonging to the given player's opponent.
     * 
     * @param player the player to check against
     * @return {@code true} if this square is occupied by a piece belonging to the given player's
     *         opponent; {@code false} otherwise
     */
    public boolean isOccupiedByOpponentOf( Player player )
    {
        return isOccupied() && getPiece().getPlayer() != player;
    }

    /**
     * Returns whether this square is controlled by a piece belonging to the given player's
     * opponent.
     * 
     * @param player the player whose opponent's pieces are checked
     * @param board  the chessboard
     * @return {@code true} if this square is controlled by a piece belonging to the given player's
     *         opponent; {@code false} otherwise
     */
    public boolean isControlledByOpponentOf( Player player, Board board )
    {
        return board.getPieces()
                    .stream()
                    .filter( piece -> piece.getPlayer() != player )
                    .anyMatch( piece -> piece.controls( this, board ) );
    }

    public boolean isOnLastRank( Player player )
    {
        return switch ( player.getColour() )
        {
            case WHITE -> getRank() == '8';
            case BLACK -> getRank() == '1';
        };
    }

    public int fileDiff( Square square )
    {
        return getFile() - square.getFile();
    }

    public int rankDiff( Square square )
    {
        return getRank() - square.getRank();
    }

    public static Shade getShade( char file, char rank )
    {
        return ( file + rank ) % 2 == 0 ? Shade.DARK : Shade.LIGHT;
    }

    public int getIndex()
    {
        return getIndex( getFile(), getRank() );
    }

    public static int getIndex( char file, char rank )
    {
        return 8 * ( file - 'a' ) + rank - '1';
    }

    public enum Shade
    {
        DARK, LIGHT
    }
}
