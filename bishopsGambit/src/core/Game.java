package core;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
import pieces.Bishop;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Piece.Typ;
import pieces.Queen;
import pieces.Rook;
import players.Player;
import players.Player.Colour;
import utils.ListUtils;

public class Game {

	private final List<Board> boards = new ArrayList<>();

	private final Player white = new Player(Colour.WHITE);
	private final Player black = new Player(Colour.BLACK);

	public Game() {
		Board board = new Board();

		board.assignPieces(getWhite());
		board.assignPieces(getBlack());

		addBoard(board);
	}

	private List<Board> getBoards() {
		return this.boards;
	}

	public Player getWhite() {
		return this.white;
	}

	public Player getBlack() {
		return this.black;
	}

	private void addBoard(Board board) {
		getBoards().add(board);
		printInfo(board);
	}

	private void printInfo(Board board) {
		Player currentPlayer = getCurrentPlayer();
		int n = currentPlayer.numberOfLegalMoves(board);
		System.out.printf("%s has %d legal move%s.", currentPlayer, n, n == 1 ? "" : "s");

		int diff = board.getMaterialDiff();
		if (diff != 0)
			System.out.printf(" %s: +%d", diff > 0 ? getWhite() : getBlack(), Math.abs(diff));

		System.out.println();
	}

	public int numberOfTurnsTaken() {
		return getBoards().size() - 1;
	}

	public Board getBoard() {
		return getBoards().get(numberOfTurnsTaken());
	}

	/**
	 * Returns the player whose turn it currently is, based on the number of turns
	 * taken.
	 * 
	 * @return White if the number of turns taken is even, Black if it is odd
	 */
	public Player getCurrentPlayer() {
		return numberOfTurnsTaken() % 2 == 0 ? getWhite() : getBlack();
	}

	/**
	 * Returns the opponent of the player whose turn it currently is, based on the
	 * number of turns taken.
	 * 
	 * @return Black if the number of turns taken is even, White if it is odd
	 */
	public Player getCurrentOpponent() {
		return numberOfTurnsTaken() % 2 == 0 ? getBlack() : getWhite();
	}

	public void move(String fromStr, String toStr) {
		move(fromStr, toStr, null);
	}

	public void move(String fromStr, String toStr, Typ prom) {
		Board board = getBoard();

		Square from = board.getSquare(fromStr);
		Square to = board.getSquare(toStr);

		move(from, to, prom);
	}

	public void move(Square from, Square to, Typ prom) {
		if (!from.isOccupied())
			throw new UnoccupiedSquareException(from);

		Board board = getBoard();

		if (!board.isLegalMove(from, to))
			throw new IllegalMoveException(from, to);

		// Disable en passant capture of opponent's pawns
		for (Piece pc : getCurrentOpponent().getPieces())
			if (pc instanceof Pawn)
				((Pawn) pc).setEnPassant(false);

		Piece piece = from.getPiece();
		Board newBoard = board.move(from, to);

		if (piece instanceof Pawn) {
			// Enable en passant capture of this pawn
			if (piece.movedTwoSquaresForward(from, to))
				((Pawn) piece).setEnPassant(true);

			// Promotion
			else if (prom != null) {
				Piece promotion;

				char toFile = to.getFile();
				int toRank = to.getRank();

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

				newBoard.getSquare(toFile, toRank).setPiece(promotion);
			}
		}

		for (Piece pc : getAllPieces()) {
			Square square = pc.getSquare(newBoard);

			// If piece is not on the board, set it as captured
			if (square == null)
				pc.setCaptured(true);

			// If piece is on the board but not on its starting square, set it as moved
			else if (square != pc.getStartSquare(newBoard))
				pc.setMoved(true);
		}

		addBoard(newBoard);
	}

	private List<Piece> getAllPieces() {
		return ListUtils.combine(getWhite().getPieces(), getBlack().getPieces());
	}

}
