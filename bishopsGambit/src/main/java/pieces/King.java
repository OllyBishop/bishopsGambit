package main.java.pieces;

import java.util.ArrayList;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.player.Player;

public class King extends Piece
{
    public King( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Typ getType()
    {
        return Typ.KING;
    }

    @Override
    public int getValue()
    {
        return 0;
    }

    @Override
    public List<Square> getTargets( Board board )
    {
        List<Square> targets = new ArrayList<>();

        Square square = getSquare( board );

        for ( int x : new int[] { -1, 0, 1 } )
        {
            for ( int y : new int[] { -1, 0, 1 } )
            {
                Square s = square.travel( board, x, y );

                if ( s != null && !s.isOccupiedBy( getPlayer() ) )
                {
                    targets.add( s );
                }
            }
        }

        return targets;
    }

    @Override
    public List<Square> getMoves( Board board )
    {
        List<Square> moves = new ArrayList<>( super.getMoves( board ) );

        if ( !isTargeted( board ) )
        {
            for ( int x : new int[] { -1, 1 } )
            {
                Rook rook = getPlayer().getRook( x );

                if ( isCastlingAllowed( rook, board ) )
                {
                    Square k = getSquare( board );
                    Square r = rook.getSquare( board );

                    Square k1 = k.travel( board, x, 0 ); // One square adjacent to king (rook moves here)
                    Square k2 = k.travel( board, 2 * x, 0 ); // Two squares adjacent to king (king moves here)
                    Square r1 = r.travel( board, -x, 0 ); // One square adjacent to rook (same as 'k2' on kingside)

                    if ( !k1.isOccupied() &&
                         !k2.isOccupied() &&
                         !r1.isOccupied() &&
                         !k1.isTargeted( board, getPlayer() ) &&
                         !k2.isTargeted( board, getPlayer() ) )
                    {
                        moves.add( k2 );
                    }
                }
            }
        }

        return moves;
    }

    private boolean isCastlingAllowed( Rook rook, Board board )
    {
        switch ( getColour() )
        {
            case WHITE:
                if ( rook == getPlayer().getQueensideRook() )
                    return board.isWhiteQueensideCastlingAllowed();

                if ( rook == getPlayer().getKingsideRook() )
                    return board.isWhiteKingsideCastlingAllowed();

                break;

            case BLACK:
                if ( rook == getPlayer().getQueensideRook() )
                    return board.isBlackQueensideCastlingAllowed();

                if ( rook == getPlayer().getKingsideRook() )
                    return board.isBlackKingsideCastlingAllowed();

                break;
        }

        throw new RuntimeException();
    }
}
