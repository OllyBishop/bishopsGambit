package board;

import java.util.ArrayList;
import java.util.List;

import pieces.King;
import pieces.Pawn;
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
	 * Returns a list of all pieces currently on the board.
	 * 
	 * @return a list of all pieces currently on the board
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

	public Board move(Square from, Square to) {
		Piece piece = from.getPiece();
		Board newBoard = movePiece(from, to);

		char fromFile = from.getFile();
		int fromRank = from.getRank();
		char toFile = to.getFile();

		// Capture opponent's pawn en passant
		if (piece instanceof Pawn && piece.movedOneSquareDiagonallyForward(from, to) && !to.isOccupied()) {
			Square square = newBoard.getSquare(toFile, fromRank);
			newBoard.replace(square, square.clone());
		}

		// Castling
		else if (piece instanceof King && piece.movedTwoSquaresLaterally(from, to)) {
			int x = Integer.signum(toFile - fromFile);
			Square rookFrom = piece.getPlayer().getRook(x).getStartSquare(this);
			Square rookTo = from.travel(newBoard, x, 0);
			newBoard = newBoard.movePiece(rookFrom, rookTo);
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

}
