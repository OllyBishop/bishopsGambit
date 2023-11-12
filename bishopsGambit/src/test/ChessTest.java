package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import board.Board;
import core.Game;
import core.IllegalMoveException;
import core.UnoccupiedSquareException;
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

	private static Game game;

	@BeforeEach
	void printName(TestInfo testInfo) {
		System.out.println("Running " + testInfo.getDisplayName());
	}

	@BeforeEach
	void newGame() {
		game = new Game();
	}

	@AfterEach
	void printBoard() {
		game.getBoard().print();
	}

	@Test
	void unoccupiedSquare() {
		try {
			game.move(E3, E4);
		} catch (UnoccupiedSquareException e) {
			System.out.println(e.getMessage());
			return;
		}

		fail();
	}

	@Test
	void illegalMove() {
		try {
			game.move(E1, E2);
		} catch (IllegalMoveException e) {
			System.out.println(e.getMessage());
			return;
		}

		fail();
	}

	@Test
	void foolsMate() {
		game.move(F2, F3);
		game.move(E7, E5);

		game.move(G2, G4);
		game.move(D8, H4);

		Board board = game.getBoard();

		assertEquals(game.getNumberOfTurns(), 4);
		assertTrue(game.getCurrentPlayer().inCheckmate(board));
	}

	@Test
	void scholarsMate() {
		game.move(E2, E4);
		game.move(E7, E5);

		game.move(D1, H5);
		game.move(B8, C6);

		game.move(F1, C4);
		game.move(G8, F6);

		game.move(H5, F7);

		Board board = game.getBoard();

		assertEquals(game.getNumberOfTurns(), 7);
		assertTrue(game.getCurrentPlayer().inCheckmate(board));
	}

	@Test
	void kingsideCastling() {
		game.move(E2, E4);
		game.move(E7, E5);

		game.move(G1, F3);
		game.move(G8, F6);

		game.move(F1, C4);
		game.move(F8, C5);

		game.move(E1, G1);
		game.move(E8, G8);

		Board board = game.getBoard();
		Player white = game.getWhite();
		Player black = game.getBlack();

		assertEquals(game.getNumberOfTurns(), 8);

		assertEquals(white.getKing(), board.getPiece(G1));
		assertEquals(white.getKingsideRook(), board.getPiece(F1));

		assertEquals(black.getKing(), board.getPiece(G8));
		assertEquals(black.getKingsideRook(), board.getPiece(F8));
	}

	@Test
	void queensideCastling() {
		game.move(D2, D4);
		game.move(D7, D5);

		game.move(B1, C3);
		game.move(B8, C6);

		game.move(C1, F4);
		game.move(C8, F5);

		game.move(D1, D2);
		game.move(D8, D7);

		game.move(E1, C1);
		game.move(E8, C8);

		Board board = game.getBoard();
		Player white = game.getWhite();
		Player black = game.getBlack();

		assertEquals(game.getNumberOfTurns(), 10);

		assertEquals(white.getKing(), board.getPiece(C1));
		assertEquals(white.getQueensideRook(), board.getPiece(D1));

		assertEquals(black.getKing(), board.getPiece(C8));
		assertEquals(black.getQueensideRook(), board.getPiece(D8));
	}

	@Test
	void enPassantOnlyLegalMove() {
		game.move(E2, E4);
		game.move(E7, E6);

		game.move(E4, E5);
		game.move(D8, H4);

		game.move(E1, E2);
		game.move(B8, C6);

		game.move(E2, E3);
		game.move(H4, G3);

		game.move(E3, E4);
		game.move(D7, D5);

		Board board = game.getBoard();

		assertEquals(game.getNumberOfTurns(), 10);
		assertEquals(game.getCurrentPlayer().numberOfMoves(board), 1);
	}

}
