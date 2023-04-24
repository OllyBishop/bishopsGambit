package gui;

import java.awt.Color;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import board.Board;
import board.Square;
import core.Game;
import players.Player;

public class ChessGUI extends JFrame {

	private JPanel contentPane;

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

		for (Square s : getBoard()) {
			s.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getBoard().squarePressed(s, getCurrentPlayer());
				}
			});
			contentPane.add(s);
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_RELEASED) {
					if (getBoard().makeMove()) {
						getGame().nextTurn();
						updateBoard();
					}
				}
				return false;
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

	private void updateGUI() {
		updateBoard();
	}

	private void updateBoard() {
		updateBoard(getCurrentPlayer());
	}

	/**
	 * Resizes and repositions the chess board relative to the size of the
	 * application window. Updates the appearance of each square based on pieces
	 * moved. The board is oriented to the given player's perspective.
	 * 
	 * @param perspective the player to whose perspective the board is oriented
	 */
	private void updateBoard(Player perspective) {
		int width = contentPane.getWidth();
		int height = contentPane.getHeight();

		int xMid = width / 2;
		int yMid = height / 2;

		/*
		 * Set scale to 10% of width or height of frame (whichever is smallest),
		 * ensuring a lower bound of 10 pixels.
		 */
		int scale = Math.max(10, Math.min(width, height) / 10);

		for (Square s : getBoard()) {
			int fileIndex = s.getFileIndex();
			int rankIndex = s.getRankIndex();

			int x = xMid;
			int y = yMid;

			switch (perspective.getColour()) {
			case WHITE:
				x -= (4 - fileIndex) * scale;
				y += (3 - rankIndex) * scale;
				break;
			case BLACK:
				x += (3 - fileIndex) * scale;
				y -= (4 - rankIndex) * scale;
				break;
			}

			s.setBounds(x, y, scale, scale);

			if (s.isOccupied()) {
				Image imageFull = s.getPiece().getImage();
				Image imageScaled = imageFull.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
				s.setIcon(new ImageIcon(imageScaled));
			} else {
				s.setIcon(null);
			}

			s.setFont(s.getFont().deriveFont((float) scale));

			s.setEmptyBorder();
		}

		showCheck(getGame().getWhite(), scale);
		showCheck(getGame().getBlack(), scale);
	}

	private void showCheck(Player player, int scale) {
		Square square = player.getKing().getSquare(getBoard());
		if (square != null && player.inCheck(getBoard())) {
			Border border = BorderFactory.createLineBorder(Color.red, Math.max(1, scale / 20));
			square.setBorder(border);
		}
	}

}
