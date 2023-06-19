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
	 * Returns a list of all pieces currently in play; i.e., all pieces that are
	 * currently on the board.
	 * 
	 * @return a list of all pieces currently in play
	 */
	public List<Piece> getPieces() {
		return stream().filter(Square::isOccupied).map(Square::getPiece).toList();
	}

	/**
	 * Finds the square whose coordinates match the given string. For example, an
	 * input of "a1" finds the square with file 'a' and rank '1'.
	 * 
	 * @param string the coordinates of the desired square as a string
	 * @return the square whose coordinates match the given string
	 */
	public Square getSquare(String string) {
		char file = string.charAt(0);
		int rank = string.charAt(1) - '0';
		return getSquare(file, rank);
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
		Square square = null;
		if ('a' <= file && file <= 'h' && 1 <= rank && rank <= 8) {
			int fileIndex = Square.getFileIndex(file);
			int rankIndex = Square.getRankIndex(rank);
			square = get(fileIndex * 8 + rankIndex);
		}
		return square;
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
