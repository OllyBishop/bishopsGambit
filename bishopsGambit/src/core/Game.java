package core;

import board.Board;
import pieces.Piece;
import players.Colour;
import players.Player;

public class Game {

	private Board board;

	private Player white;
	private Player black;

	private int noOfTurns = 0;

	private void setBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return this.board;
	}

	private void setWhite(Player player) {
		this.white = player;
	}

	public Player getWhite() {
		return this.white;
	}

	private void setBlack(Player player) {
		this.black = player;
	}

	public Player getBlack() {
		return this.black;
	}

	public Game() {
		setBoard(new Board());

		setWhite(new Player(Colour.WHITE));
		setBlack(new Player(Colour.BLACK));

		assignPieces(getWhite());
		assignPieces(getBlack());
	}

	/**
	 * Assigns all the pieces of the given player to their starting squares.
	 * 
	 * @param player the player whose pieces are to be assigned
	 */
	private void assignPieces(Player player) {
		for (Piece p : player.getPieces()) {
			char startFile = p.getStartFile();
			int startRank = p.getStartRank();
			getBoard().getSquare(startFile, startRank).setPiece(p);
		}
	}

	/**
	 * Returns the player whose turn it currently is, based on the number of turns
	 * taken.
	 * 
	 * @return white if the number of turns taken is even, black if it is odd
	 */
	public Player getCurrentPlayer() {
		return this.noOfTurns % 2 == 0 ? getWhite() : getBlack();
	}

	/**
	 * Increments the number of turns taken by 1.
	 */
	public void nextTurn() {
		this.noOfTurns++;
	}

}
