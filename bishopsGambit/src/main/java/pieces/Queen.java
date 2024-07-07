package main.java.pieces;

import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.player.Player;
import main.java.util.ListUtils;

public class Queen extends Piece
{
    public Queen( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Typ getType()
    {
        return Typ.QUEEN;
    }

    @Override
    public int getValue()
    {
        return 9;
    }

    @Override
    public List<Square> getTargets( Board board )
    {
        List<Square> bishopTargets = Bishop.getTargets( board, this );
        List<Square> rookTargets = Rook.getTargets( board, this );
        return ListUtils.combine( bishopTargets, rookTargets );
    }
}
