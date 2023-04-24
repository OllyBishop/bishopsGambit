package pieces;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import board.Board;
import board.Square;
import players.Colour;
import players.Player;

public abstract class Piece {

	private Player player; // References the player this piece belongs to

	private void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Colour getColour() {
		return getPlayer().getColour();
	}

	private char startFile;
	private int startRank;
	private boolean hasMoved;
	private boolean isCaptured;

	private Image image;

	/**
	 * Returns the value of this piece as an integer.
	 * 
	 * @return the value of this piece as an integer
	 */
	public abstract int getValue();

	/**
	 * Returns a list of all squares this piece is currently targeting; i.e., all
	 * squares this piece could move to (assuming checks are ignored).
	 * 
	 * @param board the chess board
	 * @return a list of all squares this piece is currently targeting
	 */
	public abstract ArrayList<Square> getTargets(Board board);

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

	public Piece(Player player, char startFile, int startRank) {
		setPlayer(player);

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

	/**
	 * Generates a URL for this piece's image based on its colour and type. The URL
	 * has the form <code>"/img/COLOUR_TYPE.png"</code>.
	 * 
	 * @return a URL for this piece's image based on its colour and type
	 */
	public String getImageURL() {
		String colourStr = getColour().toString();
		String pieceStr = getClass().getSimpleName();
		return String.format("/img/%s_%s.png", colourStr, pieceStr);
	}

	/**
	 * Finds the square this piece is assigned to.
	 * 
	 * @param board the chess board
	 * @return the square this piece is assigned to
	 */
	public Square getSquare(Board board) {
		Square square = null;
		for (Square s : board) {
			if (s.getPiece() == this) {
				square = s;
				break;
			}
		}
		return square;
	}

	/**
	 * Returns a boolean indicating whether or not this piece is currently being
	 * targeted by an opponent's piece.
	 * 
	 * @param board the chess board
	 * @return true if this piece is currently being targeted by an opponent's
	 *         piece, false otherwise
	 */
	public boolean isTargeted(Board board) {
		boolean isTargeted = false;
		for (Square s : board) {
			if (s.isOccupiedByOpponent(getPlayer()) && board.getTargets(s).contains(getSquare(board))) {
				isTargeted = true;
				break;
			}
		}
		return isTargeted;
	}

}
