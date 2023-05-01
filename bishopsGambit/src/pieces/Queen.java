package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import players.Player;

public class Queen extends Piece {

	@Override
	public int getValue() {
		return 9;
	}

	@Override
	public List<Square> getTargets(Board board) {
		List<Square> targets = new ArrayList<Square>();
		targets.addAll(Bishop.getTargets(board, this));
		targets.addAll(Rook.getTargets(board, this));
		return targets;
	}

	public Queen(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

}
