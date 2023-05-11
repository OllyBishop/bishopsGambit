package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import board.Board;
import core.Game;
import players.Player;

class ChessTest {

	@Test
	void foolsMate() {
		Game game = new Game();

		game.move("f2f3");
		game.move("e7e5");

		game.move("g2g4");
		game.move("d8h4");

		Player white = game.getWhite();
		Board board = game.getBoard();

		assertTrue(game.getCurrentPlayer() == white);
		assertTrue(white.inCheckmate(board));
	}

	@Test
	void scholarsMate() {
		Game game = new Game();

		game.move("e2e4");
		game.move("e7e5");

		game.move("d1f3");
		game.move("f8c5");

		game.move("f1c4");
		game.move("d7d6");

		game.move("f3f7");

		Player black = game.getBlack();
		Board board = game.getBoard();

		assertTrue(game.getCurrentPlayer() == black);
		assertTrue(black.inCheckmate(board));
	}

}
