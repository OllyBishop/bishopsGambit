package core;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import pieces.Piece;
import players.Colour;
import players.Player;

public class Game {

	private final List<Board> boards = new ArrayList<Board>();

	private final Player white = new Player(Colour.WHITE);
	private final Player black = new Player(Colour.BLACK);

	private List<Board> getBoards() {
		return this.boards;
	}

	private Player getWhite() {
		return this.white;
	}

	private Player getBlack() {
		return this.black;
	}

	public Game() {
		Board board = new Board();
		assignPieces(board, getWhite());
		assignPieces(board, getBlack());
		addBoard(board);
	}

	/**
	 * Assigns all the pieces of the given player to their starting squares.
	 * 
	 * @param board  the chess board
	 * @param player the player whose pieces are to be assigned
	 */
	private void assignPieces(Board board, Player player) {
		for (Piece piece : player.getPieces()) {
			char startFile = piece.getStartFile();
			int startRank = piece.getStartRank();
			board.getSquare(startFile, startRank).setPiece(piece);
		}
	}

	public void addBoard(Board board) {
		getBoards().add(board);
		turnInfo(board);
	}

	private void turnInfo(Board board) {
		Player player = getCurrentPlayer();
		Player opponent = player == getWhite() ? getBlack() : player;
		int n = player.numberOfMoves(board);
		if (n == 0) {
			if (player.inCheck(board))
				System.out.printf("%s wins by checkmate!", opponent.getColour());
			else
				System.out.println("It's a stalemate!");
		} else {
			System.out.printf("%s has %d legal move%s.\n", player.getColour(), n, n == 1 ? "" : "s");
		}
	}

	public int getNumberOfTurns() {
		return getBoards().size() - 1;
	}

	public Board getBoard() {
		return getBoards().get(getNumberOfTurns());
	}

	/**
	 * Returns the player whose turn it currently is, based on the number of turns
	 * taken.
	 * 
	 * @return white if the number of turns taken is even, black if it is odd
	 */
	public Player getCurrentPlayer() {
		return getNumberOfTurns() % 2 == 0 ? getWhite() : getBlack();
	}

}
