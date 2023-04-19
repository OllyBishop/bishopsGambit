package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import board.Board;
import board.Square;
import core.Game;
import players.Player;

public class ChessGUI extends JFrame {

	private JPanel contentPane;
	private int scale;

	private Game game;

	private void setGame(Game game) {
		this.game = game;
	}

	private Game getGame() {
		return this.game;
	}

	private Board getBoard() {
		return getGame().getBoard();
	}

	private Player getCurrentPlayer() {
		return getGame().getCurrentPlayer();
	}

	/**
	 * Create the frame.
	 */
	public ChessGUI(Game game) {
		setGame(game);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width / 2, screenSize.height);

		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		for (Square square : getBoard()) {
			square.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getBoard().squareSelected(square, getCurrentPlayer());
				}
			});
			contentPane.add(square);
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean b;
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_RELEASED) {
					b = getBoard().makeMove();
					if (b) {
						getGame().nextTurn();
						updateBoard();
					}
				} else {
					b = false;
				}
				return b;
			}
		});

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateGUI();
			}
		});

		updateGUI();
	}

	public void updateGUI() {
		updateBoard();
	}

	public void updateBoard() {
		updateBoard(getCurrentPlayer());
	}

	/**
	 * Resizes and repositions the board relative to the size of the application
	 * window.
	 */
	public void updateBoard(Player perspective) {
		int width = contentPane.getWidth();
		int height = contentPane.getHeight();

		int xMid = width / 2;
		int yMid = height / 2;

		/*
		 * Set scale to 10% of width or height of frame (whichever is smallest),
		 * ensuring a lower bound of 10 pixels.
		 */
		scale = Math.max(10, Math.min(width, height) / 10);

		for (Square square : getBoard()) {
			int fileIndex = square.getFileIndex();
			int rankIndex = square.getRankIndex();

			int x, y;
			if (perspective == getGame().getWhite()) {
				x = xMid - (4 - fileIndex) * scale;
				y = yMid + (3 - rankIndex) * scale;
			} else {
				x = xMid + (3 - fileIndex) * scale;
				y = yMid - (4 - rankIndex) * scale;
			}
			square.setBounds(x, y, scale, scale);

			if (square.isOccupied()) {
				Image imageFull = square.getPiece().getImage();
				Image imageScaled = imageFull.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
				square.setIcon(new ImageIcon(imageScaled));
			} else {
				square.setIcon(null);
			}
		}
	}

}
