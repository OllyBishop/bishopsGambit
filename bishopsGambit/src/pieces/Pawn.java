package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import players.Player;

public class Pawn extends Piece {

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public List<Square> getTargets(Board board) {
		List<Square> targets = new ArrayList<Square>();

		Square square = getSquare(board);
		char file = square.getFile();
		int rank = square.getRank();
		int y = getPlayer().getDirection();

		// Move forward one or two squares
		for (int n : new int[] { 1, 2 }) {
			if (n == 1 || !hasMoved()) {
				Square s = board.getSquare(file, rank + n * y);
				if (s != null)
					if (s.isOccupied())
						break;
					else
						targets.add(s);
			}
		}

		// Capture diagonally
		for (int x : new int[] { -1, 1 }) {
			Square s = board.getSquare((char) (file + x), rank + y);
			if (s != null)
				if (s.isOccupiedByOpponent(getPlayer()))
					targets.add(s);
		}

		return targets;
	}

	public Pawn(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

}
