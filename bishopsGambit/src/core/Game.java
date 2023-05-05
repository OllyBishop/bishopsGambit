package core;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
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

	public Player getWhite() {
		return this.white;
	}

	public Player getBlack() {
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
		Player opponent = getOpponent(player);
		int n = player.numberOfMoves(board);
		if (n == 0) {
			if (player.inCheck(board))
				System.out.printf("%s wins by checkmate!\n", opponent.getColour());
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
	 * @return White if the number of turns taken is even, Black if it is odd
	 */
	public Player getCurrentPlayer() {
		return getNumberOfTurns() % 2 == 0 ? getWhite() : getBlack();
	}

	/**
	 * Returns the opponent of the given player.
	 * 
	 * @param player the player
	 * @return White if the given player is Black, Black if the given player is
	 *         White, <code>null</code> otherwise
	 */
	private Player getOpponent(Player player) {
		Player opponent = null;
		switch (player.getColour()) {
		case WHITE:
			opponent = getBlack();
			break;
		case BLACK:
			opponent = getWhite();
			break;
		}
		return opponent;
	}

	public Board move(Square from, Square to) {
		Board newBoard = getBoard().move(from, to);
		addBoard(newBoard);
		return newBoard;
	}

	public Board move(char fromFile, int fromRank, char toFile, int toRank) {
		Square from = getBoard().getSquare(fromFile, fromRank);
		Square to = getBoard().getSquare(toFile, toRank);
		return move(from, to);
	}

	public Board move(String from, String to) {
		char fromFile = from.charAt(0);
		int fromRank = Integer.parseInt(from.substring(1));
		char toFile = to.charAt(0);
		int toRank = Integer.parseInt(to.substring(1));
		return move(fromFile, fromRank, toFile, toRank);
	}

}
