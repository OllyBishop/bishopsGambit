package board;

import pieces.Piece;
import players.Player;

public class Square {

	private final char file;
	private final int rank;

	private Piece piece;

	public char getFile() {
		return this.file;
	}

	public int getRank() {
		return this.rank;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public Square(char file, int rank) {
		this.file = file;
		this.rank = rank;
	}

	@Override
	public Square clone() {
		return new Square(getFile(), getRank());
	}

	public static int getFileIndex(char file) {
		return file - 'a';
	}

	public static int getRankIndex(int rank) {
		return rank - 1;
	}

	public int getFileIndex() {
		return getFileIndex(getFile());
	}

	public int getRankIndex() {
		return getRankIndex(getRank());
	}

	public String getCoordinates() {
		return String.format("%s%s", getFile(), getRank());
	}

	/**
	 * Returns a boolean indicating whether or not this square is occupied.
	 * 
	 * @return <code>true</code> if this square contains a piece, <code>false</code>
	 *         otherwise
	 */
	public boolean isOccupied() {
		return getPiece() != null;
	}

	/**
	 * Returns a boolean indicating whether or not this square is occupied by a
	 * piece belonging to the given player.
	 * 
	 * @param player the player
	 * @return <code>true</code> if this square contains a piece belonging to the
	 *         given player, <code>false</code> otherwise
	 */
	public boolean isOccupiedBy(Player player) {
		return player.getPieces().contains(getPiece());
	}

	/**
	 * Returns a boolean indicating whether or not this square is occupied by a
	 * piece <i>not</i> belonging to the given player.
	 * 
	 * @param player the player
	 * @return <code>true</code> if this square contains a piece <i>not</i>
	 *         belonging to the given player, <code>false</code> otherwise
	 */
	public boolean isOccupiedByOpponent(Player player) {
		return isOccupied() && !isOccupiedBy(player);
	}

	/**
	 * Returns a boolean indicating whether or not this square is being targeted.
	 * 
	 * @param board the chess board
	 * @return <code>true</code> if this square is being targeted,
	 *         <code>false</code> otherwise
	 */
	public boolean isTargeted(Board board) {
		return board.getPieces().stream().anyMatch(p -> p.isTargeting(this, board));
	}

}
