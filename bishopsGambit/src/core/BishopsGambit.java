package core;

import java.awt.EventQueue;

import gui.ChessGUI;

public class BishopsGambit {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Game game = new Game();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ChessGUI frame = new ChessGUI(game);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
