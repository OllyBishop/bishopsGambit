package board;

import java.util.ArrayList;

import pieces.Piece;
import players.Player;

public class Board extends ArrayList<Square> {

	private Square from;
	private Square to;

	private void setFrom(Square from) {
		this.from = from;
	}

	private void setTo(Square to) {
		this.to = to;
	}

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
	 * Finds the square containing the given piece.
	 * 
	 * @param piece the piece contained in the square to be found
	 * @return the square containing the given piece
	 */
	public Square getSquare(Piece piece) {
		Square square = null;
		for (Square s : this) {
			if (s.getPiece() == piece) {
				square = s;
				break;
			}
		}
		return square;
	}

	/**
	 * Updates the appearance of the chess board based on which square has been
	 * pressed. Specifically, this method shows legal moves and highlights pressed
	 * squares (that correspond to legal moves).
	 * 
	 * @param square the square that was pressed
	 * @param player the player who pressed the square
	 */
	public void squarePressed(Square square, Player player) {
		boolean deselectFrom = false;
		Square selectFrom = null;
		Square selectTo = null;

		if (square.containsPiece(player)) {
			deselectFrom = true;
			if (from != square)
				selectFrom = square;
		}

		else if (from != null) {
			if (to == null)
				if (getTargets(from).contains(square))
					selectTo = square;
				else
					deselectFrom = true;
			else if (to != square)
				deselectFrom = true;
		}

		for (Square s : this)
			s.setText(null);

		if (deselectFrom && from != null)
			setFrom(from.deselect());
		if (to != null)
			setTo(to.deselect());
		if (selectFrom != null)
			setFrom(selectFrom.select());
		if (selectTo != null)
			setTo(selectTo.select());

		if (from != null && to == null)
			for (Square s : getTargets(from))
				s.setText("●");
	}

	public ArrayList<Square> getTargets(Square square) {
		return square.getPiece().getTargets(this);
	}

	public boolean makeMove() {
		boolean success;
		if (from != null && to != null) {
			Piece pieceFrom = from.getPiece();
			Piece pieceTo = to.getPiece();

			pieceFrom.setMoved(true);
			if (pieceTo != null)
				pieceTo.setCaptured(true);

			to.setPiece(pieceFrom);
			from.setPiece(null);

			setFrom(from.deselect());
			setTo(to.deselect());

			success = true;
		} else {
			success = false;
		}
		return success;
	}

}
