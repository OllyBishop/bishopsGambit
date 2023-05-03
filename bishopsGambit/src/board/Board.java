package board;

import java.util.ArrayList;
import java.util.List;

import pieces.Piece;

public class Board extends ArrayList<Square> {

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
		Square square;
		if ('a' <= file && file <= 'h' && 1 <= rank && rank <= 8) {
			int fileIndex = Square.getFileIndex(file);
			int rankIndex = Square.getRankIndex(rank);
			square = get(fileIndex * 8 + rankIndex);
		} else {
			square = null;
		}
		return square;
	}

	/**
	 * Returns a list of all squares the piece in the given square is currently
	 * targeting; i.e., all squares the piece could move to (assuming checks are
	 * ignored).
	 * 
	 * @param square the square
	 * @return a list of all squares the piece in the given square is currently
	 *         targeting
	 */
	public List<Square> getTargets(Square square) {
		return square.getPiece().getTargets(this);
	}

	public List<Square> getMoves(Square square) {
		return square.getPiece().getMoves(this);
	}

	/**
	 * Moves the piece occupying the <b>from</b> square to the <b>to</b> square. The
	 * <b>from</b> square is emptied and its piece is assigned to the <b>to</b>
	 * square.
	 * 
	 * @param from the square being moved from
	 * @param to   the square being moved to
	 */
	public void move(Square from, Square to) {
		Piece pieceFrom = from.getPiece();
		Piece pieceTo = to.getPiece();

		pieceFrom.setMoved(true);
		if (pieceTo != null)
			pieceTo.setCaptured(true);

		to.setPiece(pieceFrom);
		from.setPiece(null);
	}

	public Board testMove(Square from, Square to) {
		Board newBoard = (Board) clone();

		Square newFrom = from.clone();
		Square newTo = to.clone();
		newTo.setPiece(from.getPiece());

		newBoard.replace(from, newFrom);
		newBoard.replace(to, newTo);

		return newBoard;
	}

	private void replace(Square s1, Square s2) {
		int index = indexOf(s1);
		remove(index);
		add(index, s2);
	}

}
