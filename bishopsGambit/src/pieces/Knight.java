package pieces;

import java.util.ArrayList;

import board.Board;
import board.Square;
import players.Player;

public class Knight extends Piece {

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public ArrayList<Square> getTargets(Board board) {
		ArrayList<Square> targets = new ArrayList<Square>();

		Square square = getSquare(board);
		char file = square.getFile();
		int rank = square.getRank();

		for (int x : new int[] { -1, 1 }) {
			for (int y : new int[] { -1, 1 }) {
				for (int n : new int[] { 1, 2 }) {
					Square s = board.getSquare((char) (file + n * x), rank + (3 - n) * y);
					if (s != null)
						if (!s.isOccupiedByPlayer(getPlayer()))
							targets.add(s);
				}
			}
		}

		return targets;
	}

	public Knight(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

}
