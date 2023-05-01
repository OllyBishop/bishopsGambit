package board;

import java.util.ArrayList;
import java.util.List;

import pieces.Piece;
import players.Player;

public class Board extends ArrayList<Square> {

	private Square from;
	private Square to;

	private void setFrom(Square from) {
		this.from = from;
	}

	private Square getFrom() {
		return this.from;
	}

	private void setTo(Square to) {
		this.to = to;
	}

	private Square getTo() {
		return this.to;
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
	 * Updates the appearance of the chess board based on which square has been
	 * pressed. Specifically, this method shows legal moves and highlights pressed
	 * squares (that correspond to legal moves).
	 * 
	 * @param square the square that was pressed
	 * @param player the player who pressed the square
	 */
	public void squarePressed(Square square, Player player) {
		stream().forEach(s -> s.setText(null));

		boolean deselectFrom = false;
		Square selectFrom = null;
		Square selectTo = null;

		if (square.isOccupiedByPlayer(player)) {
			deselectFrom = true;
			if (getFrom() != square)
				selectFrom = square;
		}

		else if (getFrom() != null) {
			if (getTo() == null)
				if (getTargets(getFrom()).contains(square))
					selectTo = square;
				else
					deselectFrom = true;
			else if (getTo() != square)
				deselectFrom = true;
		}

		if (deselectFrom && getFrom() != null)
			setFrom(getFrom().deselect());
		if (getTo() != null)
			setTo(getTo().deselect());
		if (selectFrom != null)
			setFrom(selectFrom.select());
		if (selectTo != null)
			setTo(selectTo.select());

		if (getFrom() != null && getTo() == null)
			getTargets(getFrom()).stream().forEach(s -> s.setText("●"));
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
	 * Returns a boolean indicating whether or not both a valid 'from' and 'to'
	 * square have been selected, thus a move can be made.
	 * 
	 * @return <code>true</code> if a move can be made, <code>false</code> otherwise
	 */
	public boolean canMove() {
		return (getFrom() != null && getTo() != null);
	}

	/**
	 * Moves the piece from the 'from' square to the 'to' square. The 'from' square
	 * is emptied and its piece is assigned to the 'to' square.
	 */
	public void move() {
		Piece pieceFrom = getFrom().getPiece();
		Piece pieceTo = getTo().getPiece();

		pieceFrom.setMoved(true);
		if (pieceTo != null)
			pieceTo.setCaptured(true);

		getTo().setPiece(pieceFrom);
		getFrom().setPiece(null);

		setFrom(getFrom().deselect());
		setTo(getTo().deselect());
	}

}
