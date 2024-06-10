package main.java.pieces;

import java.util.ArrayList;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.player.Player;

public class Rook extends Piece
{
    public Rook( Player player, char startFile, int startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Typ getType()
    {
        return Typ.ROOK;
    }

    @Override
    public int getValue()
    {
        return 5;
    }

    @Override
    public List<Square> getTargets( Board board )
    {
        return getTargets( board, this );
    }

    public static List<Square> getTargets( Board board, Piece piece )
    {
        List<Square> targets = new ArrayList<>();

        Square square = piece.getSquare( board );

        for ( int x : new int[] { 0, 1 } )
        {
            for ( int y : new int[] { -1, 1 } )
            {
                for ( int n = 1; n < 8; n++ )
                {
                    Square s = square.travel( board, n * x * y, n * (1 - x) * y );

                    if ( s == null )
                    {
                        break;
                    }

                    if ( s.isOccupied() )
                    {
                        if ( s.isOccupiedByOpponent( piece.getPlayer() ) )
                        {
                            targets.add( s );
                        }

                        break;
                    }

                    targets.add( s );
                }
            }
        }

        return targets;
    }
}
