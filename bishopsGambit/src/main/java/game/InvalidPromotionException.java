package main.java.game;

import main.java.pieces.Piece.Typ;

public class InvalidPromotionException extends IllegalArgumentException
{
    public InvalidPromotionException( Typ type )
    {
        super( "Cannot promote to a piece of type '" + type + "'." );
    }

    public InvalidPromotionException( String code )
    {
        super( "Unrecognised promotion piece type code '" + code + "'." );
    }
}
