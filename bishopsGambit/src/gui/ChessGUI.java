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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	// Fields --------------------------------------------------------- //
	private final JPanel contentPane;

	private final List<JLabel> files = new ArrayList<JLabel>();
	private final List<JLabel> ranks = new ArrayList<JLabel>();

	private final Map<SButton, Square> buttonMap = new HashMap<SButton, Square>();

	private SButton from;
	private SButton to;

	private int xMid;
	private int yMid;
	private int scale;

	private Game game;
	// ---------------------------------------------------------------- //

	// Getters and setters -------------------------------------------- //
	private void setFrom(SButton from) {
		this.from = from;
	}

	private SButton getFrom() {
		return from;
	}

	private void setTo(SButton to) {
		this.to = to;
	}

	private SButton getTo() {
		return to;
	}

	private void setGame(Game game) {
		this.game = game;
	}

	private Game getGame() {
		return this.game;
	}
	// ---------------------------------------------------------------- //

	// Derived getters ------------------------------------------------ //
	private Square getSquare(SButton button) {
		return buttonMap.get(button);
	}

	private Set<SButton> getButtons() {
		return buttonMap.keySet();
	}

	private SButton getButton(Square square) {
		return getButtons().stream().filter(b -> getSquare(b) == square).findAny().orElse(null);
	}

	private Board getBoard() {
		return getGame().getBoard();
	}

	private List<Square> getMoves(SButton button) {
		return getBoard().getMoves(getSquare(button));
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

	private SButton getKingButton() {
		return getButton(getCurrentPlayer().getKing().getSquare(getBoard()));
	}
	// ---------------------------------------------------------------- //

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

		// Create file labels
		for (char file = 'a'; file <= 'h'; file++) {
			JLabel label = new JLabel(Character.toString(file));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.TOP);
			files.add(label);
			contentPane.add(label);
		}

		// Create rank labels
		for (int rank = 1; rank <= 8; rank++) {
			JLabel label = new JLabel(Integer.toString(rank));
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setVerticalAlignment(SwingConstants.CENTER);
			ranks.add(label);
			contentPane.add(label);
		}

		for (Square square : getBoard()) {
			SButton button = new SButton(square);

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getButtons().stream().forEach(b -> b.setText(null));

					boolean deselectFrom = false;
					SButton selectFrom = null;
					SButton selectTo = null;

					if (getSquare(button).isOccupiedByPlayer(getCurrentPlayer())) {
						deselectFrom = true;
						if (getFrom() != button)
							selectFrom = button;
					}

					else if (getFrom() != null) {
						if (getTo() == null)
							if (getMoves(getFrom()).contains(getSquare(button)))
								selectTo = button;
							else
								deselectFrom = true;
						else if (getTo() != button)
							deselectFrom = true;
					}

					if (deselectFrom && getFrom() != null)
						setFrom(getFrom().deselect());
					if (getTo() != null)
						setTo(getTo().deselect());
					if (selectFrom != null)
						setFrom(selectFrom.select());
					if (selectTo != null)
						setTo(selectTo.select());

					if (getFrom() != null && getTo() == null)
						getMoves(getFrom()).stream().forEach(s -> getButton(s).setText("●"));
				}
			});

			buttonMap.put(button, square);
			contentPane.add(button);
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean enterKeyReleased = e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_RELEASED;
				boolean canMove = getFrom() != null && getTo() != null;
				if (enterKeyReleased && canMove) {
					makeMove();
					updateBoard();
				}
				return enterKeyReleased;
			}

			private void makeMove() {
				Square from = getSquare(getFrom());
				Square to = getSquare(getTo());

				from.getPiece().setMoved(true);
				if (to.isOccupied())
					to.getPiece().setCaptured(true);

				Board newBoard = getBoard().move(from, to);

				Square newFrom = newBoard.get(getBoard().indexOf(from));
				Square newTo = newBoard.get(getBoard().indexOf(to));

				buttonMap.put(getFrom(), newFrom);
				buttonMap.put(getTo(), newTo);

				getKingButton().setEmptyBorder();

				setFrom(getFrom().deselect());
				setTo(getTo().deselect());

				getGame().addBoard(newBoard);
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
		for (int index = 0; index <= 7; index++) {
			JLabel file = files.get(index);
			JLabel rank = ranks.get(index);

			int xFile = xMid;
			int yFile = yMid + 4 * scale;
			int xRank = xMid - 5 * scale;
			int yRank = yMid;

			switch (perspective.getColour()) {
			case WHITE:
				xFile -= (4 - index) * scale;
				yRank += (3 - index) * scale;
				break;
			case BLACK:
				xFile += (3 - index) * scale;
				yRank -= (4 - index) * scale;
				break;
			}

			file.setBounds(xFile, yFile, scale, scale);
			rank.setBounds(xRank, yRank, scale, scale);

			ComponentUtils.resizeFont(file, scale / 4);
			ComponentUtils.resizeFont(rank, scale / 4);
		}

		for (SButton button : getButtons()) {
			Square square = getSquare(button);

			int fileIndex = square.getFileIndex();
			int rankIndex = square.getRankIndex();

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

			button.setBounds(x, y, scale, scale);

			ComponentUtils.resizeFont(button, scale);

			if (square.isOccupied()) {
				Image imageFull = square.getPiece().getImage();
				Image imageScaled = imageFull.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
				button.setIcon(new ImageIcon(imageScaled));
			} else {
				button.setIcon(null);
			}
		}

		if (inCheck()) {
			Border border = BorderFactory.createLineBorder(Color.red, Math.max(1, scale / 20));
			getKingButton().setBorder(border);
		}
	}

}
