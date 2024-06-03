package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import board.Board;
import board.Square;
import core.Game;
import core.IllegalMoveException;
import core.UnoccupiedSquareException;
import pieces.Piece;
import players.Player;

class ChessTest {

	final String A1 = "a1", A2 = "a2", A3 = "a3", A4 = "a4", A5 = "a5", A6 = "a6", A7 = "a7", A8 = "a8";
	final String B1 = "b1", B2 = "b2", B3 = "b3", B4 = "b4", B5 = "b5", B6 = "b6", B7 = "b7", B8 = "b8";
	final String C1 = "c1", C2 = "c2", C3 = "c3", C4 = "c4", C5 = "c5", C6 = "c6", C7 = "c7", C8 = "c8";
	final String D1 = "d1", D2 = "d2", D3 = "d3", D4 = "d4", D5 = "d5", D6 = "d6", D7 = "d7", D8 = "d8";
	final String E1 = "e1", E2 = "e2", E3 = "e3", E4 = "e4", E5 = "e5", E6 = "e6", E7 = "e7", E8 = "e8";
	final String F1 = "f1", F2 = "f2", F3 = "f3", F4 = "f4", F5 = "f5", F6 = "f6", F7 = "f7", F8 = "f8";
	final String G1 = "g1", G2 = "g2", G3 = "g3", G4 = "g4", G5 = "g5", G6 = "g6", G7 = "g7", G8 = "g8";
	final String H1 = "h1", H2 = "h2", H3 = "h3", H4 = "h4", H5 = "h5", H6 = "h6", H7 = "h7", H8 = "h8";

	Game game;

	void move(String fromStr, String toStr) {
		game.move(fromStr, toStr);
	}

	Player getWhite() {
		return game.getWhite();
	}

	Player getBlack() {
		return game.getBlack();
	}

	Board getBoard() {
		return game.getBoard();
	}

	Square getSquare(String squareStr) {
		return getBoard().getSquare(squareStr);
	}

	Piece getPiece(String squareStr) {
		return getSquare(squareStr).getPiece();
	}

	boolean canMoveTo(Piece piece, String squareStr) {
		return piece.getMoves(getBoard()).contains(getSquare(squareStr));
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
		getBoard().print();
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

	@Test
	void enemyKnightBlocksKingsideCastling() {
		move(E2, E4);
		move(G8, F6);

		move(G1, F3);
		move(F6, G4);

		move(F1, C4);
		move(G4, E3);

		move(D2, D3);
		move(E3, F1);

		assertEquals(numberOfTurnsTaken(), 8);

		assertFalse(canMoveTo(getWhite().getKing(), G1));
	}

	@Test
	void enemyBishopBlocksKingsideCastling() {
		move(E2, E4);
		move(B7, B6);

		move(G1, F3);
		move(C8, A6);

		move(D2, D4);
		move(A6, F1);

		assertEquals(numberOfTurnsTaken(), 6);

		assertFalse(canMoveTo(getWhite().getKing(), G1));
	}

}
