package pieces;

import java.util.ArrayList;

import board.Board;
import board.Square;
import players.Player;

public class Bishop extends Piece {

	@Override
	public int getValue() {
		return 3;
	}

	@Override
	public ArrayList<Square> getTargets(Board board) {
		return getTargets(board, this);
	}

	public static ArrayList<Square> getTargets(Board board, Piece piece) {
		ArrayList<Square> targets = new ArrayList<Square>();

		Square square = board.getSquare(piece);
		char file = square.getFile();
		int rank = square.getRank();

		for (int x : new int[] { -1, 1 }) {
			for (int y : new int[] { -1, 1 }) {
				for (int n = 1; n < 8; n++) {
					Square s = board.getSquare((char) (file + n * x), rank + n * y);
					if (s == null) {
						break;
					} else {
						if (s.isOccupied()) {
							if (s.isOccupiedByOpponent(piece.getPlayer()))
								targets.add(s);
							break;
						} else {
							targets.add(s);
						}
					}
				}
			}
		}

		return targets;
	}

	public Bishop(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

}
