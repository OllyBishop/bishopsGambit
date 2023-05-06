package board;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * Returns a list of all squares the piece in the given square can legally move
	 * to. The squares returned are a subset of the list returned by getTargets(),
	 * with any moves that would result in check removed.
	 * 
	 * @param square the square
	 * @return a list of all squares the piece in the given square can legally move
	 *         to
	 */
	public List<Square> getMoves(Square square) {
		return square.getPiece().getMoves(this);
	}

	/**
	 * Creates a clone of this board where the piece occupying the <b>from</b>
	 * square has been moved to the <b>to</b> square. This is achieved by cloning
	 * the given squares and assigning the piece accordingly.
	 * 
	 * @param from the square being moved from
	 * @param to   the square being moved to
	 * @return a clone of this board where the piece occupying the <b>from</b>
	 *         square has been moved to the <b>to</b> square
	 */
	public Board move(Square from, Square to) {
		Board newBoard = (Board) clone();

		Square newFrom = from.clone();
		Square newTo = to.clone();
		newTo.setPiece(from.getPiece());

		newBoard.replace(from, newFrom);
		newBoard.replace(to, newTo);

		return newBoard;
	}

	/**
	 * Replaces the square <b>s1</b> on this board with the square <b>s2</b>.
	 * 
	 * @param s1 the old square
	 * @param s2 the new square
	 */
	private void replace(Square s1, Square s2) {
		int index = indexOf(s1);
		remove(index);
		add(index, s2);
	}

}
