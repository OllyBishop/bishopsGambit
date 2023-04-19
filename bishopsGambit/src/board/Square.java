package board;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import pieces.Piece;

public class Square extends JButton {

	private static final Color DARK = new Color(209, 139, 71);
	private static final Color LIGHT = new Color(254, 206, 157);
	public static final Color SELECTED = new Color(253, 253, 150);

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

	public Color getBgColor() {
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

		setBorder(BorderFactory.createEmptyBorder());
		setBackground(getBgColor());
		setToolTipText(getCoordinates());
		setOpaque(true);
	}

	/**
	 * Expresses the file and rank of this square as a string.
	 * 
	 * @return the coordinates of this square as a string
	 */
	public String getCoordinates() {
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

	public Square select() {
		setSelected(true);
		setBackground(SELECTED);
		return this;
	}

	public Square deselect() {
		setSelected(false);
		setBackground(getBgColor());
		return null;
	}

}
