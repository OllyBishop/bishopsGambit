package main.java.game;

import main.java.board.Square;

public class UnoccupiedSquareException extends RuntimeException
{
    public UnoccupiedSquareException( Square from )
    {
        super( String.format( "Cannot move a piece from %s because it is unoccupied.", from ) );
    }
}
