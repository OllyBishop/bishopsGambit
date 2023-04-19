package board;

import java.util.ArrayList;

import pieces.Piece;
import players.Player;

public class Board extends ArrayList<Square> {

	public Square from;
	public Square to;

	/**
	 * Creates an ArrayList of 64 squares, comprising the chess board.
	 */
	public Board() {
		for (char file = 'a'; file <= 'h'; file++) {
			for (int rank = 1; rank <= 8; rank++) {
				add(new Square(file, rank));
			}
		}
	}

	/**
	 * Finds the square with the given file and rank.
	 * 
	 * @param file the file of the square to be found
	 * @param rank the rank of the square to be found
	 * @return the square with the given file and rank
	 */
	public Square getSquare(char file, int rank) {
		int fileIndex = Square.getFileIndex(file);
		int rankIndex = Square.getRankIndex(rank);
		return get(fileIndex * 8 + rankIndex);
	}

	public void squareSelected(Square square, Player player) {
		boolean squareCanBeMovedFrom = player.getPieces().contains(square.getPiece());

		boolean deselectFrom = false;
		Square selectFrom = null;
		Square selectTo = null;

		if (squareCanBeMovedFrom) {
			deselectFrom = true;
			if (square != from)
				selectFrom = square;
		}

		else if (from != null) {
			boolean squareCanBeMovedTo = !squareCanBeMovedFrom;

			if (squareCanBeMovedTo) {
				if (square != to)
					selectTo = square;
			}

			else {
				deselectFrom = true;
			}
		}

		if (deselectFrom && from != null)
			from = from.deselect();
		if (to != null)
			to = to.deselect();
		if (selectFrom != null)
			from = selectFrom.select();
		if (selectTo != null)
			to = selectTo.select();
	}

	public boolean makeMove() {
		boolean b;
		if (from != null && to != null) {
			Piece pieceFrom = from.getPiece();
			Piece pieceTo = to.getPiece();

			pieceFrom.setMoved(true);
			if (pieceTo != null)
				pieceTo.setCaptured(true);

			to.setPiece(pieceFrom);
			from.setPiece(null);

			from = from.deselect();
			to = to.deselect();

			b = true;
		} else {
			b = false;
		}
		return b;
	}

}
