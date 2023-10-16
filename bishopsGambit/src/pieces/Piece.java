package pieces;

import java.awt.Image;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import board.Board;
import board.Square;
import players.Player;
import utils.StringUtils;

public abstract class Piece {

	public enum Typ {
		PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;

		@Override
		public String toString() {
			return StringUtils.toUpperCamelCase(name());
		}
	}

	private final Player player; // References the player this piece belongs to
	private final char startFile;
	private final int startRank;
	private final Image image;

	private boolean hasMoved;
	private boolean isCaptured;

	public Player getPlayer() {
		return this.player;
	}

	public char getStartFile() {
		return this.startFile;
	}

	public int getStartRank() {
		return this.startRank;
	}

	public Image getImage() {
		return this.image;
	}

	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public boolean hasMoved() {
		return this.hasMoved;
	}

	public void setCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	public boolean isCaptured() {
		return this.isCaptured;
	}

	public Piece(Player player, char startFile, int startRank) {
		this.player = player;
		this.startFile = startFile;
		this.startRank = startRank;

		Image image = null;
		try {
			String colourStr = getPlayer().getColour().toString();
			String pieceStr = getClass().getSimpleName();
			String imageURL = String.format("/img/%s_%s.png", colourStr, pieceStr);
			image = ImageIO.read(getClass().getResource(imageURL));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.image = image;

		setMoved(false);
		setCaptured(false);

		player.getPieces().add(this);
	}

	@Override
	public String toString() {
		return String.format("%s %s", getPlayer().getColour(), getType());
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
	 * @return the square this piece is occupying (if it exists), otherwise
	 *         <code>null</code>
	 */
	public Square getSquare(Board board) {
		return board.stream().filter(s -> s.getPiece() == this).findAny().orElse(null);
	}

	/**
	 * Returns a boolean indicating whether or not (the square occupied by) this
	 * piece is being targeted by an enemy piece.
	 * 
	 * @param board the chess board
	 * @return <code>true</code> if this piece is being targeted, <code>false</code>
	 *         otherwise
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
	 * @return <code>true</code> if this piece is targeting the given square,
	 *         <code>false</code> otherwise
	 */
	public boolean isTargeting(Board board, Square square) {
		return getTargets(board).contains(square);
	}

	public boolean canPromote(Board board, Square square) {
		return this instanceof Pawn && square.travel(board, 0, getPlayer().getDirection()) == null;
	}

	public boolean movedTwoSquaresForward(Square from, Square to) {
		int fileDiff = to.getFile() - from.getFile();
		int rankDiff = to.getRank() - from.getRank();
		return fileDiff == 0 && rankDiff == 2 * getPlayer().getDirection();
	}

	public boolean movedOneSquareDiagonallyForward(Square from, Square to) {
		int fileDiff = to.getFile() - from.getFile();
		int rankDiff = to.getRank() - from.getRank();
		return Math.abs(fileDiff) == 1 && rankDiff == getPlayer().getDirection();
	}

	public boolean movedTwoSquaresLaterally(Square from, Square to) {
		int fileDiff = to.getFile() - from.getFile();
		int rankDiff = to.getRank() - from.getRank();
		return Math.abs(fileDiff) == 2 && rankDiff == 0;
	}

}
