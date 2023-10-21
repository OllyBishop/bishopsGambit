package test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import board.Board;
import board.Square;
import core.Game;
import core.IllegalMoveException;
import core.UnoccupiedSquareException;
import pieces.Piece;
import pieces.Piece.Typ;
import players.Player;

class ChessTest {

//	private static final String A1 = "a1";
//	private static final String A2 = "a2";
//	private static final String A3 = "a3";
//	private static final String A4 = "a4";
//	private static final String A5 = "a5";
//	private static final String A6 = "a6";
//	private static final String A7 = "a7";
//	private static final String A8 = "a8";
	private static final String B1 = "b1";
//	private static final String B2 = "b2";
//	private static final String B3 = "b3";
//	private static final String B4 = "b4";
//	private static final String B5 = "b5";
//	private static final String B6 = "b6";
//	private static final String B7 = "b7";
	private static final String B8 = "b8";
	private static final String C1 = "c1";
//	private static final String C2 = "c2";
	private static final String C3 = "c3";
	private static final String C4 = "c4";
	private static final String C5 = "c5";
	private static final String C6 = "c6";
//	private static final String C7 = "c7";
	private static final String C8 = "c8";
	private static final String D1 = "d1";
	private static final String D2 = "d2";
//	private static final String D3 = "d3";
	private static final String D4 = "d4";
	private static final String D5 = "d5";
//	private static final String D6 = "d6";
	private static final String D7 = "d7";
	private static final String D8 = "d8";
	private static final String E1 = "e1";
	private static final String E2 = "e2";
	private static final String E3 = "e3";
	private static final String E4 = "e4";
	private static final String E5 = "e5";
	private static final String E6 = "e6";
	private static final String E7 = "e7";
	private static final String E8 = "e8";
	private static final String F1 = "f1";
	private static final String F2 = "f2";
	private static final String F3 = "f3";
	private static final String F4 = "f4";
	private static final String F5 = "f5";
	private static final String F6 = "f6";
	private static final String F7 = "f7";
	private static final String F8 = "f8";
	private static final String G1 = "g1";
	private static final String G2 = "g2";
	private static final String G3 = "g3";
	private static final String G4 = "g4";
//	private static final String G5 = "g5";
//	private static final String G6 = "g6";
//	private static final String G7 = "g7";
	private static final String G8 = "g8";
//	private static final String H1 = "h1";
//	private static final String H2 = "h2";
//	private static final String H3 = "h3";
	private static final String H4 = "h4";
	private static final String H5 = "h5";
//	private static final String H6 = "h6";
//	private static final String H7 = "h7";
//	private static final String H8 = "h8";

	public static void move(Game game, String fromStr, String toStr) {
		move(game, fromStr, toStr, null);
	}

	public static void move(Game game, String fromStr, String toStr, Typ prom) {
		Board board = game.getBoard();
		Square from = board.getSquare(fromStr);
		Square to = board.getSquare(toStr);
		game.move(from, to, prom);
	}

	public static Piece getPiece(Board board, String squareStr) {
		return board.getSquare(squareStr).getPiece();
	}

	@Test
	void unoccupiedSquare() {
		Game game = new Game();

		try {
			move(game, E3, E4);
		} catch (UnoccupiedSquareException e) {
			System.out.println(e.getMessage());
			return;
		}

		fail();
	}

	@Test
	void illegalMove() {
		Game game = new Game();

		try {
			move(game, E1, E2);
		} catch (IllegalMoveException e) {
			System.out.println(e.getMessage());
			return;
		}

		fail();
	}

	@Test
	void foolsMate() {
		Game game = new Game();

		move(game, F2, F3);
		move(game, E7, E5);

		move(game, G2, G4);
		move(game, D8, H4);

		Board board = game.getBoard();

		assertTrue(game.getNumberOfTurns() == 4);
		assertTrue(game.getCurrentPlayer().inCheckmate(board));
	}

	@Test
	void scholarsMate() {
		Game game = new Game();

		move(game, E2, E4);
		move(game, E7, E5);

		move(game, D1, H5);
		move(game, B8, C6);

		move(game, F1, C4);
		move(game, G8, F6);

		move(game, H5, F7);

		Board board = game.getBoard();

		assertTrue(game.getNumberOfTurns() == 7);
		assertTrue(game.getCurrentPlayer().inCheckmate(board));
	}

	@Test
	void kingsideCastling() {
		Game game = new Game();

		move(game, E2, E4);
		move(game, E7, E5);

		move(game, G1, F3);
		move(game, G8, F6);

		move(game, F1, C4);
		move(game, F8, C5);

		move(game, E1, G1);
		move(game, E8, G8);

		Board board = game.getBoard();
		Player white = game.getWhite();
		Player black = game.getBlack();

		assertTrue(game.getNumberOfTurns() == 8);

		assertTrue(white.getKing() == getPiece(board, G1));
		assertTrue(white.getKingsideRook() == getPiece(board, F1));

		assertTrue(black.getKing() == getPiece(board, G8));
		assertTrue(black.getKingsideRook() == getPiece(board, F8));
	}

	@Test
	void queensideCastling() {
		Game game = new Game();

		move(game, D2, D4);
		move(game, D7, D5);

		move(game, B1, C3);
		move(game, B8, C6);

		move(game, C1, F4);
		move(game, C8, F5);

		move(game, D1, D2);
		move(game, D8, D7);

		move(game, E1, C1);
		move(game, E8, C8);

		Board board = game.getBoard();
		Player white = game.getWhite();
		Player black = game.getBlack();

		assertTrue(game.getNumberOfTurns() == 10);

		assertTrue(white.getKing() == getPiece(board, C1));
		assertTrue(white.getQueensideRook() == getPiece(board, D1));

		assertTrue(black.getKing() == getPiece(board, C8));
		assertTrue(black.getQueensideRook() == getPiece(board, D8));
	}

	@Test
	void enPassantOnlyLegalMove() {
		Game game = new Game();

		move(game, E2, E4);
		move(game, E7, E6);

		move(game, E4, E5);
		move(game, D8, H4);

		move(game, E1, E2);
		move(game, B8, C6);

		move(game, E2, E3);
		move(game, H4, G3);

		move(game, E3, E4);
		move(game, D7, D5);

		Board board = game.getBoard();

		assertTrue(game.getNumberOfTurns() == 10);
		assertTrue(game.getCurrentPlayer().numberOfMoves(board) == 1);
	}

}
