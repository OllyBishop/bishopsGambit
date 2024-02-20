package pieces;

import java.util.List;

import board.Board;
import board.Square;
import players.Player;
import players.Player.Colour;

public abstract class Piece {

	private final Player player; // References the player this piece belongs to

	private final char startFile;
	private final int startRank;

	private boolean hasMoved;
	private boolean isCaptured;

	public Piece(Player player, char startFile, int startRank) {
		this.player = player;
		this.startFile = startFile;
		this.startRank = startRank;

		setMoved(false);
		setCaptured(false);

		player.getPieces().add(this);
	}

	public Player getPlayer() {
		return this.player;
	}

	public Colour getColour() {
		return getPlayer().getColour();
	}

	public int getDirection() {
		return getPlayer().getDirection();
	}

	public char getStartFile() {
		return this.startFile;
	}

	public int getStartRank() {
		return this.startRank;
	}

	public boolean hasMoved() {
		return this.hasMoved;
	}

	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public boolean isCaptured() {
		return this.isCaptured;
	}

	public void setCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	@Override
	public String toString() {
		return String.format("%s %s", getColour(), getType());
	}

	public char toChar() {
		return (char) ('\u2654' + 6 * getColour().ordinal() + getType().ordinal());
	}

	public abstract Typ getType();

	/**
	 * Returns the value of this piece as an integer.
	 * 
	 * @return the value of this piece as an integer
	 */
	public abstract int getValue();

	/**
	 * Returns a list of all squares this piece is currently targeting; i.e., all
	 * squares this piece could move to (assuming checks are ignored).
	 * 
	 * @param board the chess board
	 * @return a list of all squares this piece is currently targeting
	 */
	public abstract List<Square> getTargets(Board board);

	/**
	 * Returns a list of all squares this piece can legally move to. The squares
	 * returned are a subset of the squares returned by {@code getTargets()}, with
	 * any moves that would result in check removed.
	 * 
	 * @param board the chess board
	 * @return a list of all squares this piece can legally move to
	 */
	public List<Square> getMoves(Board board) {
		Square from = getSquare(board);
		return getTargets(board).stream().filter(to -> !getPlayer().inCheck(board.move(from, to))).toList();
	}

	public Square getStartSquare(Board board) {
		return board.getSquare(getStartFile(), getStartRank());
	}

	/**
	 * Finds the square this piece is occupying.
	 * 
	 * @param board the chess board
	 * @return the square this piece is occupying (if it exists), {@code null}
	 *         otherwise
	 */
	public Square getSquare(Board board) {
		return board.stream().filter(s -> s.getPiece() == this).findAny().orElse(null);
	}

	/**
	 * Returns a boolean indicating whether or not (the square occupied by) this
	 * piece is being targeted by an enemy piece.
	 * 
	 * @param board the chess board
	 * @return {@code true} if this piece is being targeted, {@code false} otherwise
	 */
	public boolean isTargeted(Board board) {
		return getSquare(board).isTargeted(board);
	}

	/**
	 * Returns a boolean indicating whether or not this piece is targeting the given
	 * square.
	 * 
	 * @param board  the chess board
	 * @param square the square
	 * @return {@code true} if this piece is targeting the given square,
	 *         {@code false} otherwise
	 */
	public boolean isTargeting(Board board, Square square) {
		return getTargets(board).contains(square);
	}

	public boolean canPromote(Board board, Square square) {
		return this instanceof Pawn && square.travel(board, 0, getPlayer().getDirection()) == null;
	}

	public boolean movedTwoSquaresForward(Square from, Square to) {
		int fileDiff = to.fileDiff(from);
		int rankDiff = to.rankDiff(from);
		return fileDiff == 0 && rankDiff == 2 * getPlayer().getDirection();
	}

	public boolean movedOneSquareDiagonallyForward(Square from, Square to) {
		int fileDiff = to.fileDiff(from);
		int rankDiff = to.rankDiff(from);
		return Math.abs(fileDiff) == 1 && rankDiff == getPlayer().getDirection();
	}

	public boolean movedTwoSquaresLaterally(Square from, Square to) {
		int fileDiff = to.fileDiff(from);
		int rankDiff = to.rankDiff(from);
		return Math.abs(fileDiff) == 2 && rankDiff == 0;
	}

	public enum Typ {
		KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN;

		@Override
		public String toString() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}

}
