package gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import board.Board;
import board.Square;
import utils.ColorUtils;
import utils.ComponentUtils;

public class SButton extends JButton {

	private static final Color DARK = new Color(209, 139, 71);
	private static final Color LIGHT = new Color(254, 206, 157);
	private static final Color HIGHLIGHT = ColorUtils.blend(Color.yellow, Color.white);
	private static final Color BLACK_SEMI_TRANSPARENT = ColorUtils.changeAlpha(Color.black, 85);

	private final char file;
	private final int rank;

	public char getFile() {
		return this.file;
	}

	public int getRank() {
		return this.rank;
	}

	public SButton(char file, int rank) {
		this.file = file;
		this.rank = rank;

		clearBorder();
		resetBackground();

		setForeground(BLACK_SEMI_TRANSPARENT);
		setHorizontalTextPosition(CENTER);
		setOpaque(true);
	}

	private Color getDefaultBg() {
		return (getFile() + getRank()) % 2 == 0 ? DARK : LIGHT;
	}

	public void clearBorder() {
		setBorder(BorderFactory.createEmptyBorder());
	}

	private void resetBackground() {
		setBackground(getDefaultBg());
	}

	/**
	 * Sets the selected state of this button to <code>true</code>. Changes the
	 * background color of this button to indicate it is selected.
	 * 
	 * @return this button
	 */
	public SButton select() {
		setSelected(true);
		setBackground(ColorUtils.blend(getDefaultBg(), HIGHLIGHT, 1, 3));
		return this;
	}

	/**
	 * Sets the selected state of this button to <code>false</code>. Changes the
	 * background color of this button to its default color.
	 * 
	 * @return <code>null</code>
	 */
	public SButton deselect() {
		setSelected(false);
		resetBackground();
		return null;
	}

	/**
	 * Sets the width, height and font size of this button to the given scale.
	 * 
	 * @param scale the new width, height and font size of this button
	 */
	public void setScale(int scale) {
		setSize(scale, scale);
		ComponentUtils.resizeFont(this, scale);
	}

	/**
	 * Sets this button's icon to an image of the piece occupying its corresponding
	 * square. The corresponding square is the square on the given <b>board</b> that
	 * has the same file and rank as this button. If the square is not occupied by
	 * any piece, an empty icon is set.
	 * 
	 * @param board the board
	 */
	public void paintIcon(Board board, Graphics graphics) {
		Icon icon = null;

		Square square = board.getSquare(getFile(), getRank());
		if (square.isOccupied()) {
			Image imageFull = graphics.getImage(square.getPiece());
			Image imageScaled = imageFull.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			icon = new ImageIcon(imageScaled);
		}

		setIcon(icon);
	}

}
