package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import utils.ColorUtils;
import utils.ComponentUtils;

public class SButton extends JButton {

	private static final Color DARK = new Color(209, 139, 71);
	private static final Color LIGHT = new Color(254, 206, 157);
	private static final Color HIGHLIGHT = ColorUtils.blend(Color.yellow, Color.white);
	private static final Color BLACK_SEMI_TRANSPARENT = ColorUtils.changeAlpha(Color.black, 85);

	private final Color defaultBg;

	private Color getDefaultBg() {
		return this.defaultBg;
	}

	public SButton(char file, int rank) {
		this.defaultBg = (file + rank) % 2 == 0 ? DARK : LIGHT;

		resetBorder();
		resetBackground();

		setForeground(BLACK_SEMI_TRANSPARENT);
		setHorizontalTextPosition(CENTER);
		setOpaque(true);
	}

	public void resetBorder() {
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

}
