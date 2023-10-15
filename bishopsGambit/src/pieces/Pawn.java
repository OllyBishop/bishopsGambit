package pieces;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import players.Player;

public class Pawn extends Piece {

	private boolean enPassant;

	public void setEnPassant(boolean enPassant) {
		this.enPassant = enPassant;
	}

	public boolean canEnPassant() {
		return this.enPassant;
	}

	public Pawn(Player player, char startFile, int startRank) {
		super(player, startFile, startRank);
	}

	@Override
	public Typ getType() {
		return Typ.PAWN;
	}

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
			Square s1 = board.getSquare((char) (file + x), rank + y);
			if (s1 != null && s1.isOccupiedByOpponent(getPlayer())) {
				targets.add(s1);
				continue;
			}

			// En passant
			Square s0 = board.getSquare((char) (file + x), rank);
			if (s0 != null && s0.isOccupiedByOpponent(getPlayer())) {
				Piece piece = s0.getPiece();
				if (piece instanceof Pawn && ((Pawn) piece).canEnPassant())
					targets.add(s1);
			}
		}

		return targets;
	}

}
