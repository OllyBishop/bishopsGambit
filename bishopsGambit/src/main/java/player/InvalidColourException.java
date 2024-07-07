package main.java.player;

public class InvalidColourException extends IllegalArgumentException
{
    public InvalidColourException()
    {
        super( String.format( "Colour must be either 'White' or 'Black'." ) );
    }
}
