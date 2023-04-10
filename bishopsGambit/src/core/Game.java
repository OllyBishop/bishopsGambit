package core;

import board.Board;
import pieces.Piece;
import players.*;

public class Game {

	private Board board;
	private Player white;
	private Player black;

	private void setBoard(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return this.board;
	}

	private void setWhite(Player white) {
		this.white = white;
	}

	public Player getWhite() {
		return this.white;
	}

	private void setBlack(Player black) {
		this.black = black;
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
			char startingFile = piece.getStartingFile();
			int startingRank = piece.getStartingRank();
			getBoard().getSquare(startingFile, startingRank).setPiece(piece);
		}
	}

}
