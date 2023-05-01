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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import board.Board;
import board.Square;
import core.Game;
import players.Player;
import utils.ComponentUtils;

public class ChessGUI extends JFrame {

	private JPanel contentPane;

	private List<JLabel> files = new ArrayList<JLabel>();
	private List<JLabel> ranks = new ArrayList<JLabel>();

	private int xMid;
	private int yMid;
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

	/**
	 * Returns the player whose turn it currently is, based on the number of turns
	 * taken.
	 * 
	 * @return white if the number of turns taken is even, black if it is odd
	 */
	private Player getCurrentPlayer() {
		return getGame().getCurrentPlayer();
	}

	private boolean inCheck() {
		return getCurrentPlayer().inCheck(getBoard());
	}

	private Square getKingSquare() {
		return getCurrentPlayer().getKing().getSquare(getBoard());
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

		for (char file = 'a'; file <= 'h'; file++) {
			JLabel label = new JLabel(Character.toString(file));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.TOP);
			files.add(label);
			contentPane.add(label);
		}

		for (int rank = 1; rank <= 8; rank++) {
			JLabel label = new JLabel(Integer.toString(rank));
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setVerticalAlignment(SwingConstants.CENTER);
			ranks.add(label);
			contentPane.add(label);
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean canMove = getBoard().canMove();
				if (canMove && e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_RELEASED) {
					if (inCheck())
						getKingSquare().setEmptyBorder();
					getBoard().move();
					getGame().nextTurn();
					updateBoard();
				}
				return canMove;
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateScale();
				updateGUI();
			}
		});
	}

	/**
	 * Sets scale to 10% of width or height of frame (whichever is smallest),
	 * ensuring a lower bound of 10 pixels.
	 */
	private void updateScale() {
		int width = contentPane.getWidth();
		int height = contentPane.getHeight();

		xMid = width / 2;
		yMid = height / 2;
		scale = Math.max(10, Math.min(width, height) / 10);
	}

	/**
	 * Updates the appearance of the GUI.
	 */
	private void updateGUI() {
		updateBoard();
	}

	/**
	 * Resizes and repositions the chess board relative to the size of the
	 * application window. Updates the appearance of each square based on pieces
	 * moved. The board is oriented to the perspective of the player whose turn it
	 * is.
	 */
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

			ComponentUtils.resizeFont(s, scale);

			if (s.isOccupied()) {
				Image imageFull = s.getPiece().getImage();
				Image imageScaled = imageFull.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
				s.setIcon(new ImageIcon(imageScaled));
			} else {
				s.setIcon(null);
			}
		}

		for (int i = 0; i <= 7; i++) {
			JLabel file = files.get(i);
			JLabel rank = ranks.get(i);

			int xFile = xMid;
			int yFile = yMid + 4 * scale;
			int xRank = xMid - 5 * scale;
			int yRank = yMid;

			switch (perspective.getColour()) {
			case WHITE:
				xFile -= (4 - i) * scale;
				yRank += (3 - i) * scale;
				break;
			case BLACK:
				xFile += (3 - i) * scale;
				yRank -= (4 - i) * scale;
				break;
			}

			file.setBounds(xFile, yFile, scale, scale);
			rank.setBounds(xRank, yRank, scale, scale);

			ComponentUtils.resizeFont(file, scale / 4);
			ComponentUtils.resizeFont(rank, scale / 4);
		}

		if (inCheck()) {
			Border border = BorderFactory.createLineBorder(Color.red, Math.max(1, scale / 20));
			getKingSquare().setBorder(border);
		}
	}

}
