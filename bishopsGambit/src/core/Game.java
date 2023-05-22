package core;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
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

	private void addBoard(Board board) {
		getBoards().add(board);
	}

	private int getNumberOfTurns() {
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
		return getPlayer(getNumberOfTurns());
	}

	public Player getLastPlayer() {
		return getPlayer(getNumberOfTurns() - 1);
	}

	private Player getPlayer(int n) {
		return n % 2 == 0 ? getWhite() : getBlack();
	}

	public Board move(String move) {
		String fromString = move.substring(0, 2);
		String toString = move.substring(2, 4);

		Square from = getBoard().getSquare(fromString);
		Square to = getBoard().getSquare(toString);

		Player player = getCurrentPlayer();
		char toFile = to.getFile();
		int toRank = to.getRank();

		Piece promotion = null;

		switch (move.substring(4)) {
		case "n":
			promotion = new Knight(player, toFile, toRank);
			break;
		case "b":
			promotion = new Bishop(player, toFile, toRank);
			break;
		case "r":
			promotion = new Rook(player, toFile, toRank);
			break;
		case "q":
			promotion = new Queen(player, toFile, toRank);
			break;
		}

		return move(from, to, promotion);
	}

	private Board move(Square from, Square to, Piece promotion) {
		Piece piece = from.getPiece();

		Board newBoard = getBoard().move(piece, to);

		for (Piece p : getLastPlayer().getPieces())
			if (p instanceof Pawn)
				((Pawn) p).setEnPassant(false);

		// En passant
		if (piece instanceof Pawn) {
			int fileDiff = to.getFile() - from.getFile();
			int rankDiff = to.getRank() - from.getRank();
			int direction = piece.getPlayer().getDirection();

			if (fileDiff == 0 && rankDiff == 2 * direction)
				((Pawn) piece).setEnPassant(true);

			if (Math.abs(fileDiff) == 1 && rankDiff == direction && !to.isOccupied()) {
				Square s = getBoard().getSquare(to.getFile(), from.getRank());
				s.getPiece().setCaptured(true);
				s.setPiece(null);
			}
		}

		// Castling
		else if (piece instanceof King && !piece.hasMoved()) {
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
			to.getPiece().setCaptured(true);

		addBoard(newBoard);

		return newBoard;
	}

}
