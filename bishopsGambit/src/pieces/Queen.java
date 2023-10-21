package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import players.Player;

public class Queen extends Piece {

	public Queen(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

	@Override
	public Typ getType() {
		return Typ.QUEEN;
	}

	@Override
	public int getValue() {
		return 9;
	}

	@Override
	public List<Square> getTargets(Board board) {
		List<Square> targets = new ArrayList<>();
		targets.addAll(Bishop.getTargets(board, this));
		targets.addAll(Rook.getTargets(board, this));
		return targets;
	}

}
