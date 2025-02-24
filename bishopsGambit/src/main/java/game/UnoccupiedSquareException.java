package main.java.game;

import main.java.board.Square;

public class UnoccupiedSquareException extends RuntimeException
{
    protected UnoccupiedSquareException( Square from )
    {
        super( String.format( "Cannot make a move from the unoccupied square %s.", from ) );
    }
}
