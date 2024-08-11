package main.java.player;

import java.util.ArrayList;
import java.util.List;

import main.java.board.Board;
import main.java.pieces.Bishop;
import main.java.pieces.King;
import main.java.pieces.Knight;
import main.java.pieces.Pawn;
import main.java.pieces.Piece;
import main.java.pieces.Queen;
import main.java.pieces.Rook;

public class Player
{
    private final Colour colour;
    private final int sign;

    private final List<Piece> pieces = new ArrayList<>();

    private final Rook queensideRook;
    private final Rook kingsideRook;
    private final King king;

    public Player( Colour colour )
    {
        char backRank = switch ( colour )
        {
            case WHITE -> '1';
            case BLACK -> '8';
        };
        char pawnRank = switch ( colour )
        {
            case WHITE -> '2';
            case BLACK -> '7';
        };

        this.colour = colour;
        this.sign = Integer.signum( pawnRank - backRank );

        new Pawn( this, 'a', pawnRank );
        new Pawn( this, 'b', pawnRank );
        new Pawn( this, 'c', pawnRank );
        new Pawn( this, 'd', pawnRank );
        new Pawn( this, 'e', pawnRank );
        new Pawn( this, 'f', pawnRank );
        new Pawn( this, 'g', pawnRank );
        new Pawn( this, 'h', pawnRank );

        new Knight( this, 'b', backRank );
        new Knight( this, 'g', backRank );

        new Bishop( this, 'c', backRank );
        new Bishop( this, 'f', backRank );

        this.queensideRook = new Rook( this, 'a', backRank );
        this.kingsideRook = new Rook( this, 'h', backRank );

        new Queen( this, 'd', backRank );

        this.king = new King( this, 'e', backRank );
    }

    public Colour getColour()
    {
        return this.colour;
    }

    public int getSign()
    {
        return this.sign;
    }

    public List<Piece> getPieces()
    {
        return this.pieces;
    }

    public Rook getQueensideRook()
    {
        return this.queensideRook;
    }

    public Rook getKingsideRook()
    {
        return this.kingsideRook;
    }

    /**
     * Returns the queenside or kingside rook belonging this player.
     * 
     * @param x the direction along the x-axis of the rook's starting square (relative to the king's
     *          starting square)
     * @return the queenside rook if <b>x</b> is negative; the kingside rook if <b>x</b> is positive
     * @throws IllegalArgumentException if <b>x</b> is zero
     */
    public Rook getRook( int x )
    {
        if ( x < 0 )
            return getQueensideRook();

        if ( x > 0 )
            return getKingsideRook();

        throw new IllegalArgumentException( "The value 'x' must be non-zero." );
    }

    public King getKing()
    {
        return this.king;
    }

    @Override
    public String toString()
    {
        return getColour().toString();
    }

    /**
     * Returns a boolean indicating whether this player is currently in check.
     * 
     * @param board the chess board
     * @return {@code true} if this player is currently in check; {@code false} otherwise
     */
    public boolean isInCheck( Board board )
    {
        return getKing().isTargeted( board );
    }

    /**
     * Returns a boolean indicating whether this player is currently in checkmate.
     * 
     * @param board the chess board
     * @return {@code true} if this player is currently in checkmate; {@code false} otherwise
     */
    public boolean isInCheckmate( Board board )
    {
        return hasNoLegalMoves( board ) && isInCheck( board );
    }

    /**
     * Returns a boolean indicating whether this player is currently in stalemate.
     * 
     * @param board the chess board
     * @return {@code true} if this player is currently in stalemate; {@code false} otherwise
     */
    public boolean isInStalemate( Board board )
    {
        return hasNoLegalMoves( board ) && !isInCheck( board );
    }

    /**
     * Calculates the number of legal moves this player can make.
     * 
     * @param board the chess board
     * @return the number of legal moves this player can make
     */
    public int getNumberOfLegalMoves( Board board )
    {
        return getPieces().stream()
                          .filter( pc -> board.containsPiece( pc ) )
                          .mapToInt( pc -> pc.getMoves( board ).size() )
                          .sum();
    }

    /**
     * Returns a boolean indicating whether this player has any legal moves.
     * 
     * @param board the chess board
     * @return {@code true} if this player has no legal moves; {@code false} otherwise
     */
    private boolean hasNoLegalMoves( Board board )
    {
        return getNumberOfLegalMoves( board ) == 0;
    }

    public enum Colour
    {
        WHITE( "White" ),
        BLACK( "Black" );

        private final String str;

        Colour( String str )
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
