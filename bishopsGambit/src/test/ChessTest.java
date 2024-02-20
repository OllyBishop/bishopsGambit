package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import core.Game;
import core.IllegalMoveException;
import core.UnoccupiedSquareException;
import pieces.Piece;
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

	void move(String fromStr, String toStr) {
		game.move(fromStr, toStr);
	}

	Player getWhite() {
		return game.getWhite();
	}

	Player getBlack() {
		return game.getBlack();
	}

	Piece getPiece(String squareStr) {
		return game.getBoard().getSquare(squareStr).getPiece();
	}

	int numberOfTurnsTaken() {
		return game.numberOfTurnsTaken();
	}

	int numberOfLegalMoves() {
		return game.getCurrentPlayer().numberOfLegalMoves(game.getBoard());
	}

	boolean checkmate() {
		return game.getCurrentPlayer().inCheckmate(game.getBoard());
	}

	boolean stalemate() {
		return game.getCurrentPlayer().inStalemate(game.getBoard());
	}

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
			move(E3, E4);
		} catch (UnoccupiedSquareException e) {
			System.out.println(e.getMessage());
			return;
		}

		fail();
	}

	@Test
	void illegalMove() {
		try {
			move(E1, E2);
		} catch (IllegalMoveException e) {
			System.out.println(e.getMessage());
			return;
		}

		fail();
	}

	@Test
	void foolsMate() {
		move(F2, F3);
		move(E7, E5);

		move(G2, G4);
		move(D8, H4);

		assertEquals(numberOfTurnsTaken(), 4);
		assertTrue(checkmate());
	}

	@Test
	void scholarsMate() {
		move(E2, E4);
		move(E7, E5);

		move(D1, H5);
		move(B8, C6);

		move(F1, C4);
		move(G8, F6);

		move(H5, F7);

		assertEquals(numberOfTurnsTaken(), 7);
		assertTrue(checkmate());
	}

	@Test
	void kingsideCastling() {
		move(E2, E4);
		move(E7, E5);

		move(G1, F3);
		move(G8, F6);

		move(F1, C4);
		move(F8, C5);

		move(E1, G1);
		move(E8, G8);

		assertEquals(numberOfTurnsTaken(), 8);

		assertEquals(getWhite().getKing(), getPiece(G1));
		assertEquals(getWhite().getKingsideRook(), getPiece(F1));

		assertEquals(getBlack().getKing(), getPiece(G8));
		assertEquals(getBlack().getKingsideRook(), getPiece(F8));
	}

	@Test
	void queensideCastling() {
		move(D2, D4);
		move(D7, D5);

		move(B1, C3);
		move(B8, C6);

		move(C1, F4);
		move(C8, F5);

		move(D1, D2);
		move(D8, D7);

		move(E1, C1);
		move(E8, C8);

		assertEquals(numberOfTurnsTaken(), 10);

		assertEquals(getWhite().getKing(), getPiece(C1));
		assertEquals(getWhite().getQueensideRook(), getPiece(D1));

		assertEquals(getBlack().getKing(), getPiece(C8));
		assertEquals(getBlack().getQueensideRook(), getPiece(D8));
	}

	@Test
	void enPassantOnlyLegalMove() {
		move(E2, E4);
		move(E7, E6);

		move(E4, E5);
		move(D8, H4);

		move(E1, E2);
		move(B8, C6);

		move(E2, E3);
		move(H4, G3);

		move(E3, E4);
		move(D7, D5);

		assertEquals(numberOfTurnsTaken(), 10);
		assertEquals(numberOfLegalMoves(), 1);
	}

}
