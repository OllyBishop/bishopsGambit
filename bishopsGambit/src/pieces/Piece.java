package pieces;

import java.awt.Image;
import java.io.IOException;
import java.util.List;

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

	private Colour getColour() {
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
	public abstract List<Square> getTargets(Board board);

	/**
	 * Returns a list of all squares this piece can legally move to. The squares
	 * returned are a subset of the list returned by getTargets(), with any moves
	 * that would result in check removed.
	 * 
	 * @param board the chess board
	 * @return a list of all squares this piece can legally move to
	 */
	public List<Square> getMoves(Board board) {
		List<Square> targets = getTargets(board);
		Player player = getPlayer();
		Square square = getSquare(board);
		return targets.stream().filter(s -> !player.inCheck(board.move(square, s))).toList();
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
	private String getImageURL() {
		String colourStr = getColour().toString();
		String pieceStr = getClass().getSimpleName();
		return String.format("/img/%s_%s.png", colourStr, pieceStr);
	}

	/**
	 * Finds the square this piece is occupying.
	 * 
	 * @param board the chess board
	 * @return the square this piece is occupying (if it exists), otherwise
	 *         <code>null</code>
	 */
	public Square getSquare(Board board) {
		return board.stream().filter(s -> s.getPiece() == this).findAny().orElse(null);
	}

	/**
	 * Returns a boolean indicating whether or not this piece is being targeted by
	 * an opponent's piece.
	 * 
	 * @param board the chess board
	 * @return <code>true</code> if this piece is being targeted by an opponent's
	 *         piece, <code>false</code> otherwise
	 */
	public boolean isTargeted(Board board) {
		return board.stream().anyMatch(s -> s.isTargeting(this, board));
	}

}
