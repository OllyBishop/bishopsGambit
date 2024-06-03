package board;

import pieces.Piece;
import players.Player;

public class Square {

	private final char file;
	private final int rank;

	private Piece piece;

	public Square(char file, int rank) {
		this.file = file;
		this.rank = rank;
	}

	public char getFile() {
		return this.file;
	}

	public int getRank() {
		return this.rank;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	@Override
	public String toString() {
		return String.format("%s%s", getFile(), getRank());
	}

	public char toChar() {
		if (isOccupied())
			return getPiece().toChar();
		else
			return ' ';
	}

	/**
	 * Creates a new square that has the same file and rank as {@code this}. No
	 * piece is assigned to the new square; that is, the new square is unoccupied.
	 * 
	 * @return a new square that has the same file and rank as {@code this}
	 */
	@Override
	public Square clone() {
		return new Square(getFile(), getRank());
	}

	public Square clone(Piece piece) {
		Square clone = clone();
		clone.setPiece(piece);
		return clone;
	}

	public static int getFileIndex(char file) {
		return file - 'a';
	}

	public static int getRankIndex(int rank) {
		return rank - 1;
	}

	public static int getIndex(char file, int rank) {
		return getFileIndex(file) * 8 + getRankIndex(rank);
	}

	public int getIndex() {
		return getIndex(getFile(), getRank());
	}

	/**
	 * Returns a boolean indicating whether or not this square is occupied.
	 * 
	 * @return {@code true} if this square contains a piece; {@code false} otherwise
	 */
	public boolean isOccupied() {
		return getPiece() != null;
	}

	/**
	 * Returns a boolean indicating whether or not this square is occupied by a
	 * piece belonging to the given player.
	 * 
	 * @param player the player
	 * @return {@code true} if this square contains a piece belonging to the given
	 *         player; {@code false} otherwise
	 */
	public boolean isOccupiedBy(Player player) {
		return player.getPieces().contains(getPiece());
	}

	/**
	 * Returns a boolean indicating whether or not this square is occupied by a
	 * piece <i>not</i> belonging to the given player.
	 * 
	 * @param player the player
	 * @return {@code true} if this square contains a piece <i>not</i> belonging to
	 *         the given player; {@code false} otherwise
	 */
	public boolean isOccupiedByOpponent(Player player) {
		return isOccupied() && !isOccupiedBy(player);
	}

	/**
	 * Returns a boolean indicating whether or not this square is being targeted by
	 * a piece <i>not</i> belonging to the given player.
	 * 
	 * @param board  the chess board
	 * @param player the player
	 * @return {@code true} if this square is being targeted by a piece <i>not</i>
	 *         belonging to the given player; {@code false} otherwise
	 */
	public boolean isTargeted(Board board, Player player) {
		return board.getPieces().stream().filter(pc -> pc.getPlayer() != player)
				.anyMatch(pc -> pc.isTargeting(board, this));
	}

	public Square travel(Board board, int x, int y) {
		return board.getSquare((char) (getFile() + x), getRank() + y);
	}

	public int fileDiff(Square square) {
		return getFile() - square.getFile();
	}

	public int rankDiff(Square square) {
		return getRank() - square.getRank();
	}

}
