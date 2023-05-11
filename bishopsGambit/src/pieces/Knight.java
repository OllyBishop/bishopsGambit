package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import players.Player;

public class Knight extends Piece {

	public Knight(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public List<Square> getTargets(Board board) {
		List<Square> targets = new ArrayList<Square>();

		Square square = getSquare(board);
		char file = square.getFile();
		int rank = square.getRank();

		for (int x : new int[] { -1, 1 }) {
			for (int y : new int[] { -1, 1 }) {
				for (int n : new int[] { 1, 2 }) {
					Square s = board.getSquare((char) (file + n * x), rank + (3 - n) * y);
					if (s != null)
						if (!s.isOccupiedBy(getPlayer()))
							targets.add(s);
				}
			}
		}

		return targets;
	}

}
