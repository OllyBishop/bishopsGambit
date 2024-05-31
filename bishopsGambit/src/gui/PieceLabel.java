package gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import pieces.Piece;

public class PieceLabel extends JLabel {

	private final Piece piece;

	public Piece getPiece() {
		return this.piece;
	}

	public PieceLabel(Piece piece) {
		this.piece = piece;
	}

	/**
	 * Sets this label's icon to a scaled image of its corresponding piece.
	 */
	public void paintIcon() {
		Image imageFull = Graphics.getImage(getPiece());
		Image imageScaled = imageFull.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(imageScaled));
	}

}
