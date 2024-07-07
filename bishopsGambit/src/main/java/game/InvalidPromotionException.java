package main.java.game;

import main.java.pieces.Piece;

public class InvalidPromotionException extends IllegalArgumentException
{
    public InvalidPromotionException( Piece.Typ type )
    {
        super( "Cannot promote to a piece of type '" + type + "'." );
    }
}
