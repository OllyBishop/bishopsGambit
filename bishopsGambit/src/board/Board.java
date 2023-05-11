package board;

import java.util.ArrayList;
import java.util.List;

import pieces.Piece;
import players.Player;

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
	 * Assigns all the pieces of the given player to their starting squares.
	 * 
	 * @param player the player whose pieces are to be assigned
	 */
	public void assignPieces(Player player) {
		for (Piece piece : player.getPieces()) {
			char startFile = piece.getStartFile();
			int startRank = piece.getStartRank();
			getSquare(startFile, startRank).setPiece(piece);
		}
	}

	/**
	 * Finds the square with the given file and rank.
	 * 
	 * @param file the file of the square to be found
	 * @param rank the rank of the square to be found
	 * @return the square with the given file and rank (if it exists), otherwise
	 *         <code>null</code>
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

	public Square getSquare(String string) {
		char file = string.charAt(0);
		int rank = string.charAt(1) - '0';
		return getSquare(file, rank);
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
	 * Creates a clone of this board where the given piece has been moved to the
	 * given square. This is achieved by cloning the squares and assigning the piece
	 * accordingly.
	 * 
	 * @param piece the piece to be moved
	 * @param to    the destination square for the piece
	 * @return a clone of this board where the given piece has been moved to the
	 *         given square
	 */
	public Board move(Piece piece, Square to) {
		Square from = piece.getSquare(this);

		Square newFrom = from.clone();
		Square newTo = to.clone();
		newTo.setPiece(piece);

		Board newBoard = (Board) clone();
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
