package core;

import java.awt.EventQueue;
import board.Board;
import gui.ChessGUI;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Game game = new Game();
		Board board = game.getBoard();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChessGUI frame = new ChessGUI(board);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
