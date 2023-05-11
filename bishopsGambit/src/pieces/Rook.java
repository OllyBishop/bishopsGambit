package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import players.Player;

public class Rook extends Piece {

	public Rook(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

	@Override
	public int getValue() {
		return 5;
	}

	@Override
	public List<Square> getTargets(Board board) {
		return getTargets(board, this);
	}

	public static List<Square> getTargets(Board board, Piece piece) {
		List<Square> targets = new ArrayList<Square>();

		Square square = piece.getSquare(board);
		char file = square.getFile();
		int rank = square.getRank();

		for (int x : new int[] { 0, 1 }) {
			for (int y : new int[] { -1, 1 }) {
				for (int n = 1; n < 8; n++) {
					Square s = board.getSquare((char) (file + n * x * y), rank + n * (1 - x) * y);
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

}
