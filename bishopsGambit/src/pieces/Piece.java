package pieces;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import players.Colour;

public class Piece {

	public static final int PAWN_VALUE = 1;
	public static final int KNIGHT_VALUE = 3;
	public static final int BISHOP_VALUE = 3;
	public static final int ROOK_VALUE = 5;
	public static final int QUEEN_VALUE = 9;
	public static final int KING_VALUE = 0;

	private int value;
	private Colour colour;

	private char startingFile;
	private int startingRank;

	private boolean hasMoved;
	private boolean isCaptured;

	private Image image;
	private ImageIcon icon;

	private void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	private void setColour(Colour colour) {
		this.colour = colour;
	}

	public Colour getColour() {
		return this.colour;
	}

	private void setStartingFile(char file) {
		this.startingFile = file;
	}

	public char getStartingFile() {
		return this.startingFile;
	}

	private void setStartingRank(int rank) {
		this.startingRank = rank;
	}

	public int getStartingRank() {
		return this.startingRank;
	}

	private void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public boolean hasMoved() {
		return this.hasMoved;
	}

	private void setCaptured(boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	public boolean isCaptured() {
		return this.isCaptured;
	}

	private void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return this.image;
	}

	private void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public ImageIcon getIcon() {
		return this.icon;
	}

	public Piece(int value, Colour colour, char file, int rank) {
		setValue(value);
		setColour(colour);

		setStartingFile(file);
		setStartingRank(rank);

		setMoved(false);
		setCaptured(false);

		try {
			setImage(ImageIO.read(getClass().getResource(getImageURL())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setIcon(new ImageIcon(getImage()));
	}

	public String getImageURL() {
		return String.format("/img/%s_%s.png", getColour().toString(), getClass().getSimpleName());
	}

}
