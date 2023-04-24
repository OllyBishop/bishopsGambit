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

	private void assignPieces(Player player) {
		for (Piece p : player.getPieces()) {
			char startFile = p.getStartFile();
			int startRank = p.getStartRank();
			getBoard().getSquare(startFile, startRank).setPiece(p);
		}
	}

	public Player getCurrentPlayer() {
		return this.noOfTurns % 2 == 0 ? getWhite() : getBlack();
	}

	public void nextTurn() {
		this.noOfTurns++;
	}

}
