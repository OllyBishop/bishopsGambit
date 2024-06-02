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
		for (char file = 'a'; file <= 'h'; file++)
			for (int rank = 1; rank <= 8; rank++)
				add(new Square(file, rank));
	}

	/**
	 * Returns a list of all pieces currently on the board.
	 * 
	 * @return a list of all pieces currently on the board
	 */
	public List<Piece> getPieces() {
		return stream().filter(Square::isOccupied).map(Square::getPiece).toList();
	}

	/**
	 * Assigns each of the given player's pieces to its start square.
	 * 
	 * @param player the player whose pieces are to be assigned
	 */
	public void assignPieces(Player player) {
		for (Piece piece : player.getPieces())
			piece.getStartSquare(this).setPiece(piece);
	}

	/**
	 * Returns a boolean indicating whether this board contains the given piece;
	 * i.e., whether the given piece is currently occupying any square on this
	 * board.
	 * 
	 * @param piece the piece
	 * @return a boolean indicating whether this board contains the given piece
	 */
	public boolean containsPiece(Piece piece) {
		return stream().anyMatch(sq -> sq.getPiece() == piece);
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
	 * @return the square with the given file and rank (if it exists), {@code null}
	 *         otherwise
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

	public Board move(Square from, Square to) {
		Board newBoard = movePiece(from, to);

		Piece piece = from.getPiece();
		int x = Integer.signum(to.fileDiff(from));
		Square adjacent = from.travel(this, x, 0);

		switch (piece.getType()) {
		case PAWN:
			// Capture opponent's pawn en passant
			if (piece.movedOneSquareDiagonallyForward(from, to) && !to.isOccupied())
				newBoard.replace(adjacent, adjacent.clone());
			break;
		case KING:
			// Castling
			if (piece.movedTwoSquaresLaterally(from, to)) {
				Square rookFrom = piece.getPlayer().getRook(x).getStartSquare(this);
				newBoard = newBoard.movePiece(rookFrom, adjacent);
			}
			break;
		default:
			break;
		}

		return newBoard;
	}

	/**
	 * Creates a clone of this board where the piece occupying the <i>from</i>
	 * square has been moved to the <i>to</i> square. This is achieved by cloning
	 * the squares and assigning the piece accordingly.
	 * 
	 * @param from the square containing the piece to be moved
	 * @param to   the destination square for the piece
	 * @return a clone of this board where the piece occupying the <i>from</i>
	 *         square has been moved to the <i>to</i> square
	 */
	private Board movePiece(Square from, Square to) {
		Board newBoard = (Board) clone();
		newBoard.replace(from, from.clone());
		newBoard.replace(to, to.clone(from.getPiece()));
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

	public boolean isLegalMove(Square from, Square to) {
		return from.getPiece().getMoves(this).contains(to);
	}

	public int getMaterialDiff() {
		return getPieces().stream().mapToInt(pc -> pc.getDirection() * pc.getValue()).sum();
	}

	public void print() {
		Printer.print(this);
	}

	private class Printer {

		// For each box drawing char below, n is the number of "prongs" that char has
		// To convert box drawing chars from light to heavy, add (1 << n) - 1 to each

		// n = 1
		private static final char HORIZONTAL = '\u2500';
		private static final char VERTICAL = '\u2502';

		// n = 2
		private static final char DOWN_RIGHT = '\u250c';
		private static final char DOWN_LEFT = '\u2510';
		private static final char UP_RIGHT = '\u2514';
		private static final char UP_LEFT = '\u2518';

		// n = 3
		private static final char VERTICAL_RIGHT = '\u251c';
		private static final char VERTICAL_LEFT = '\u2524';
		private static final char DOWN_HORIZONTAL = '\u252c';
		private static final char UP_HORIZONTAL = '\u2534';

		// n = 4
		private static final char VERTICAL_HORIZONTAL = '\u253c';

		private static final String UPPER_ROW = getRow(DOWN_RIGHT, DOWN_HORIZONTAL, DOWN_LEFT);
		private static final String INNER_ROW = getRow(VERTICAL_RIGHT, VERTICAL_HORIZONTAL, VERTICAL_LEFT);
		private static final String LOWER_ROW = getRow(UP_RIGHT, UP_HORIZONTAL, UP_LEFT);

		private static String getRow(char right, char horizontal, char left) {
			StringBuilder row = new StringBuilder();

			row.append(right);
			for (char file = 'a'; file <= 'h'; file++) {
				for (int j = 0; j < 3; j++)
					row.append(HORIZONTAL);
				if (file < 'h')
					row.append(horizontal);
			}
			row.append(left);

			return row.toString();
		}

		private static void print(Board board) {
			System.out.println(UPPER_ROW);

			for (int rank = 8; rank >= 1; rank--) {
				StringBuilder pieceRow = new StringBuilder();

				pieceRow.append(VERTICAL);
				for (char file = 'a'; file <= 'h'; file++)
					pieceRow.append(' ').append(board.getSquare(file, rank).toChar()).append(' ').append(VERTICAL);

				System.out.println(pieceRow);

				if (rank > 1)
					System.out.println(INNER_ROW);
			}

			System.out.println(LOWER_ROW);
		}

	}

}
