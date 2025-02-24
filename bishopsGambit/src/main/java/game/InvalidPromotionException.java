package main.java.game;

public class InvalidPromotionException extends IllegalArgumentException
{
    protected InvalidPromotionException( String message )
    {
        super( message );
    }
}
