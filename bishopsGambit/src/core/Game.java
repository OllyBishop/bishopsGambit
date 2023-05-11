package core;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import pieces.King;
import pieces.Piece;
import pieces.Rook;
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
		board.assignPieces(getWhite());
		board.assignPieces(getBlack());
		addBoard(board);
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

	public Board move(Piece piece, Square to) {
		Board newBoard = getBoard().move(piece, to);

		// Castling
		if (piece instanceof King && !piece.hasMoved()) {
			int fileDiff = to.getFile() - piece.getStartFile();
			int rankDiff = to.getRank() - piece.getStartRank();

			if (Math.abs(fileDiff) == 2 && rankDiff == 0) {
				Player player = piece.getPlayer();
				int x = fileDiff / 2;

				Rook rook = player.getRook(x);
				Square rookTo = player.getCastlingSquare(getBoard(), x);

				newBoard = newBoard.move(rook, rookTo);

				rook.setMoved(true);
			}
		}

		piece.setMoved(true);
		if (to.isOccupied())
			to.getPiece().setCaptured(true); // This must go before addBoard() call to prevent NPEs when capturing pawns

		addBoard(newBoard);

		return newBoard;
	}

	public Board move(String string) {
		String from = string.substring(0, 2);
		String to = string.substring(2, 4);

		Square fromSquare = getBoard().getSquare(from);
		Square toSquare = getBoard().getSquare(to);

		return move(fromSquare.getPiece(), toSquare);
	}

}
