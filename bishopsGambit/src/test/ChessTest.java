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

		game.move("f2", "f3");
		game.move("e7", "e5");

		game.move("g2", "g4");
		game.move("d8", "h4");

		Player white = game.getWhite();
		Board board = game.getBoard();

		assertTrue(game.getCurrentPlayer() == white);
		assertTrue(white.inCheckmate(board));
	}

	@Test
	void scholarsMate() {
		Game game = new Game();

		game.move("e2", "e4");
		game.move("e7", "e5");

		game.move("d1", "f3");
		game.move("f8", "c5");

		game.move("f1", "c4");
		game.move("d7", "d6");

		game.move("f3", "f7");

		Player black = game.getBlack();
		Board board = game.getBoard();

		assertTrue(game.getCurrentPlayer() == black);
		assertTrue(black.inCheckmate(board));
	}

}
