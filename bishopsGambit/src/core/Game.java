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
import pieces.Piece.Typ;
import pieces.Queen;
import pieces.Rook;
import players.Player;
import players.Player.Colour;

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

	/**
	 * Returns the player whose turn it last was, based on the number of turns
	 * taken.
	 * 
	 * @return White if the number of turns taken is even, Black if it is odd
	 */
	public Player getLastPlayer() {
		return getPlayer(getNumberOfTurns() - 1);
	}

	private Player getPlayer(int n) {
		return n % 2 == 0 ? getWhite() : getBlack();
	}

	public Board move(String move) {
		Board board = getBoard();

		String fromString = move.substring(0, 2);
		String toString = move.substring(2, 4);

		Square from = board.getSquare(fromString);
		Square to = board.getSquare(toString);

		Typ prom;

		switch (move.substring(4)) {
		case "n":
			prom = Typ.KNIGHT;
			break;
		case "b":
			prom = Typ.BISHOP;
			break;
		case "r":
			prom = Typ.ROOK;
			break;
		case "q":
			prom = Typ.QUEEN;
			break;
		default:
			prom = null;
		}

		return move(from, to, prom);
	}

	public Board move(Square from, Square to, Typ prom) {
		Piece piece = from.getPiece();

		Board newBoard = getBoard().move(piece, to);

		for (Piece p : getLastPlayer().getPieces())
			if (p instanceof Pawn)
				((Pawn) p).setEnPassant(false);

		char fromFile = from.getFile();
		int fromRank = from.getRank();
		char toFile = to.getFile();
		int toRank = to.getRank();

		if (piece instanceof Pawn) {
			// Promotion
			if (prom != null) {
				Piece promotion;

				switch (prom) {
				case KNIGHT:
					promotion = new Knight(getCurrentPlayer(), toFile, toRank);
					break;
				case BISHOP:
					promotion = new Bishop(getCurrentPlayer(), toFile, toRank);
					break;
				case ROOK:
					promotion = new Rook(getCurrentPlayer(), toFile, toRank);
					break;
				case QUEEN:
					promotion = new Queen(getCurrentPlayer(), toFile, toRank);
					break;
				default:
					promotion = null;
				}

				piece.setCaptured(true);
				newBoard.getSquare(toFile, toRank).setPiece(promotion);
			}

			// En passant (make the player's pawn available for capture)
			else if (piece.movedTwoSquaresForward(from, to)) {
				((Pawn) piece).setEnPassant(true);
			}

			// En passant (capture the opponent's pawn)
			else if (piece.movedOneSquareDiagonallyForward(from, to) && !to.isOccupied()) {
				Square s = newBoard.getSquare(toFile, fromRank);
				s.getPiece().setCaptured(true);
				s.setPiece(null);
			}
		}

		// Castling
		else if (piece instanceof King && !piece.hasMoved() && piece.movedTwoSquaresLaterally(from, to)) {
			Player player = piece.getPlayer();
			int x = Integer.signum(toFile - fromFile);

			Rook rook = player.getRook(x);
			Square rookTo = from.travel(newBoard, x, 0);

			newBoard = newBoard.move(rook, rookTo);

			rook.setMoved(true);
		}

		piece.setMoved(true);
		if (to.isOccupied())
			to.getPiece().setCaptured(true);

		addBoard(newBoard);

		return newBoard;
	}

}
