package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import board.Square;
import utils.ColorUtils;

public class SButton extends JButton {

	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();

	private static final Color DARK = new Color(209, 139, 71);
	private static final Color LIGHT = new Color(254, 206, 157);
	private static final Color HIGHLIGHT = ColorUtils.blend(Color.yellow, Color.white);
	private static final Color BLACK_SEMI_TRANSPARENT = ColorUtils.changeAlpha(Color.black, 85);

	private Color bgColor;

	private void setBgColor(Color color) {
		this.bgColor = color;
	}

	private Color getBgColor() {
		return this.bgColor;
	}

	public SButton(Square square) {
		setEmptyBorder();

		setBgColor(square.getParity() ? DARK : LIGHT);
		setDefaultBackground();

		setForeground(BLACK_SEMI_TRANSPARENT);
		setHorizontalTextPosition(CENTER);
		setToolTipText(square.getCoordinates());
		setOpaque(true);
	}

	public void setEmptyBorder() {
		setBorder(EMPTY_BORDER);
	}

	private void setDefaultBackground() {
		setBackground(getBgColor());
	}

	/**
	 * Sets the selected state of this button to <code>true</code>. Changes the
	 * background color of this button to indicate it is selected.
	 * 
	 * @return this button
	 */
	public SButton select() {
		setSelected(true);
		setBackground(ColorUtils.blend(getBgColor(), HIGHLIGHT, 1, 3));
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
		setDefaultBackground();
		return null;
	}

}
