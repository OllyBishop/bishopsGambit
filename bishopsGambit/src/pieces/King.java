package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import players.Player;

public class King extends Piece {

	public King(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

	@Override
	public Typ getType() {
		return Typ.KING;
	}

	@Override
	public int getValue() {
		return 0;
	}

	@Override
	public List<Square> getTargets(Board board) {
		List<Square> targets = new ArrayList<>();

		Square square = getSquare(board);

		for (int x : new int[] { -1, 0, 1 }) {
			for (int y : new int[] { -1, 0, 1 }) {
				Square s = square.travel(board, x, y);
				if (s != null)
					if (!s.isOccupiedBy(getPlayer()))
						targets.add(s);
			}
		}

		return targets;
	}

	@Override
	public List<Square> getMoves(Board board) {
		List<Square> moves = new ArrayList<>(super.getMoves(board));

		for (int x : new int[] { -1, 1 }) {
			if (!hasMoved() && !isTargeted(board)) {
				Rook rook = getPlayer().getRook(x);

				if (!rook.hasMoved() && !rook.isCaptured()) {
					Square s = getStartSquare(board);

					Square s1 = s.travel(board, x, 0); // Rook moves to
					Square s2 = s.travel(board, 2 * x, 0); // King moves to

					Board b1 = board.move(s, s1);
					Board b2 = board.move(s, s2);

					if (rook.isTargeting(board, s1) && !isTargeted(b1) && !isTargeted(b2))
						moves.add(s2);
				}
			}
		}

		return moves;
	}

}
