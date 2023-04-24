package board;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import pieces.Piece;
import players.Player;
import utils.ColorUtils;

public class Square extends JButton {

	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();

	private static final Color DARK = new Color(209, 139, 71);
	private static final Color LIGHT = new Color(254, 206, 157);
	private static final Color HIGHLIGHT = ColorUtils.blend(Color.yellow, Color.white);
	private static final Color BLACK_SEMI_TRANSPARENT = ColorUtils.changeAlpha(Color.black, 85);

	private char file;
	private int rank;
	private Color bgColor;
	private Piece piece;

	private void setFile(char file) {
		this.file = file;
	}

	public char getFile() {
		return this.file;
	}

	private void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return this.rank;
	}

	private void setBgColor(Color color) {
		this.bgColor = color;
	}

	private Color getBgColor() {
		return this.bgColor;
	}

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public int getFileIndex() {
		return getFileIndex(getFile());
	}

	public int getRankIndex() {
		return getRankIndex(getRank());
	}

	public static int getFileIndex(char file) {
		return file - 97;
	}

	public static int getRankIndex(int rank) {
		return rank - 1;
	}

	public Square(char file, int rank) {
		setFile(file);
		setRank(rank);
		setBgColor((file + rank) % 2 == 0 ? DARK : LIGHT);

		setEmptyBorder();
		setBackground(getBgColor());
		setForeground(BLACK_SEMI_TRANSPARENT);
		setHorizontalTextPosition(CENTER);
		setToolTipText(getCoordinates());
		setOpaque(true);
	}

	public void setEmptyBorder() {
		setBorder(EMPTY_BORDER);
	}

	/**
	 * Expresses the file and rank of this square as a string.
	 * 
	 * @return the coordinates of this square as a string
	 */
	private String getCoordinates() {
		return String.format("%s%s", getFile(), getRank());
	}

	/**
	 * Returns a boolean indicating whether or not this square is occupied.
	 * 
	 * @return true if this square contains a piece, false otherwise
	 */
	public boolean isOccupied() {
		return getPiece() != null;
	}

	public boolean isOccupiedByPlayer(Player player) {
		return isOccupied() && containsPiece(player);
	}

	public boolean isOccupiedByOpponent(Player player) {
		return isOccupied() && !containsPiece(player);
	}

	public boolean containsPiece(Player player) {
		return player.getPieces().contains(getPiece());
	}

	public Square select() {
		setSelected(true);
		setBackground(ColorUtils.blend(getBgColor(), HIGHLIGHT, 1, 3));
		return this;
	}

	public Square deselect() {
		setSelected(false);
		setBackground(getBgColor());
		return null;
	}

}
