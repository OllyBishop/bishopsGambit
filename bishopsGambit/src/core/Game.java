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

	public void assignPieces(Player player) {
		for (Piece piece : player.getPieces()) {
			char startFile = piece.getStartFile();
			int startRank = piece.getStartRank();
			getBoard().getSquare(startFile, startRank).setPiece(piece);
		}
	}

	public Player getCurrentPlayer() {
		return this.noOfTurns % 2 == 0 ? getWhite() : getBlack();
	}

	public void nextTurn() {
		this.noOfTurns++;
	}

}
