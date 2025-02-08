package main.java.pieces;

import java.util.ArrayList;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.player.Player;

public class Pawn extends Piece
{
    public Pawn( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Typ getType()
    {
        return Typ.PAWN;
    }

    @Override
    public int getValue()
    {
        return 1;
    }

    @Override
    public List<Square> getTargets( Board board )
    {
        List<Square> targets = new ArrayList<>();

        Square square = getSquare( board );
        int y = getSign();

        // Move forward one or two squares
        for ( int n : new int[] { 1, 2 } )
        {
            if ( n == 1 || square == getStartSquare( board ) )
            {
                Square s = square.travel( board, 0, n * y );

                if ( s != null )
                {
                    if ( s.isOccupied() )
                    {
                        break;
                    }

                    targets.add( s );
                }
            }
        }

        // Capture diagonally
        for ( int x : new int[] { -1, 1 } )
        {
            Square s0 = square.travel( board, x, 0 );
            Square s1 = square.travel( board, x, y );

            boolean regularCapture = s1 != null &&
                                     s1.isOccupiedByOpponent( getPlayer() );

            boolean enPassantCapture = s0 != null &&
                                       s0.isOccupiedByOpponent( getPlayer() ) &&
                                       s0.getPiece() == board.getEnPassantPawn();

            if ( regularCapture || enPassantCapture )
                targets.add( s1 );
        }

        return targets;
    }
}
