package pieces;

import java.util.ArrayList;

import board.Board;
import board.Square;
import players.Player;

public class King extends Piece {

	@Override
	public int getValue() {
		return 0;
	}

	@Override
	public ArrayList<Square> getTargets(Board board) {
		ArrayList<Square> targets = new ArrayList<Square>();

		Square square = getSquare(board);
		char file = square.getFile();
		int rank = square.getRank();

		for (int x : new int[] { -1, 0, 1 }) {
			for (int y : new int[] { -1, 0, 1 }) {
				Square s = board.getSquare((char) (file + x), rank + y);
				if (s != null)
					if (!s.isOccupiedByPlayer(getPlayer()))
						targets.add(s);
			}
		}

		return targets;
	}

	public King(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

}
