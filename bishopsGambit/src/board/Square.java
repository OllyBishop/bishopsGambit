package board;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import pieces.Piece;

public class Square extends JButton {

	private static final Color DARK = new Color(157, 108, 60);
	private static final Color LIGHT = new Color(238, 213, 174);

	private char file;
	private int rank;
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

	public void setPiece(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public Square(char file, int rank) {
		setFile(file);
		setRank(rank);

		setBorder(BorderFactory.createEmptyBorder());
		setBackground((file + rank) % 2 == 0 ? DARK : LIGHT);
		setToolTipText(getCoordinates());
		setOpaque(true);

		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
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

	public void setFontSize(float fontSize) {
		setFont(getFont().deriveFont(fontSize));
	}

	public void updateIcon() {
		if (isOccupied()) {
			setIcon(getPiece().getIcon());
		} else {
			setIcon(null);
		}
	}

	public static int fileToIndex(char file) {
		return (int) (file - 97);
	}

	public static int rankToIndex(int rank) {
		return rank - 1;
	}

}
