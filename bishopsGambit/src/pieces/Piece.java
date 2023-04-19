package pieces;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import players.Colour;

public abstract class Piece {

	private Colour colour;
	private char startFile;
	private int startRank;

	private boolean hasMoved;
	private boolean isCaptured;

	private Image image;

	public abstract int getValue();

	private void setColour(Colour colour) {
		this.colour = colour;
	}

	public Colour getColour() {
		return this.colour;
	}

	private void setStartFile(char file) {
		this.startFile = file;
	}

	public char getStartFile() {
		return this.startFile;
	}

	private void setStartRank(int rank) {
		this.startRank = rank;
	}

	public int getStartRank() {
		return this.startRank;
	}

	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	public boolean hasMoved() {
		return this.hasMoved;
	}

	public void setCaptured(boolean isCaptured) {
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

	public Piece(Colour colour, char startFile, int startRank) {
		setColour(colour);
		setStartFile(startFile);
		setStartRank(startRank);

		setMoved(false);
		setCaptured(false);

		try {
			setImage(ImageIO.read(getClass().getResource(getImageURL())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getImageURL() {
		String colourStr = getColour().toString();
		String pieceStr = getClass().getSimpleName();
		return String.format("/img/%s_%s.png", colourStr, pieceStr);
	}

}
