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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private final JPanel contentPane = new JPanel();
	private final List<JLabel> files = new ArrayList<JLabel>();
	private final List<JLabel> ranks = new ArrayList<JLabel>();
	private final Map<SButton, Square> map = new HashMap<SButton, Square>();

	private Game game;

	private SButton from;
	private SButton to;

	private int xMid;
	private int yMid;
	private int scale;
	private Border checkBorder;
	// ---------------------------------------------------------------- //

	// Getters and setters -------------------------------------------- //
	private void setGame(Game game) {
		this.game = game;
	}

	private Game getGame() {
		return this.game;
	}
	// ---------------------------------------------------------------- //

	// Map methods ---------------------------------------------------- //
	private Square getSquare(SButton button) {
		return map.get(button);
	}

	private Set<SButton> getButtons() {
		return map.keySet();
	}

	private SButton getButton(Square square) {
		return getButtons().stream().filter(b -> getSquare(b) == square).findAny().orElse(null);
	}
	// ---------------------------------------------------------------- //

	// Derived getters ------------------------------------------------ //
	private Board getBoard() {
		return getGame().getBoard();
	}

	/**
	 * Returns the player whose turn it currently is, based on the number of turns
	 * taken.
	 * 
	 * @return White if the number of turns taken is even, Black if it is odd
	 */
	private Player getCurrentPlayer() {
		return getGame().getCurrentPlayer();
	}

	private Player getLastPlayer() {
		return getGame().getLastPlayer();
	}

	private List<Square> getMoves(SButton button) {
		return getSquare(button).getPiece().getMoves(getBoard());
	}

	private SButton getKingButton(Player player) {
		return getButton(player.getKing().getSquare(getBoard()));
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

		// Create square buttons
		for (Square square : getBoard()) {
			SButton button = new SButton(square.getFile(), square.getRank());
			map.put(button, square);
			contentPane.add(button);
		}

		for (SButton button : getButtons()) {
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (to != null)
						unpreviewMove();
					else if (from != null)
						getButtons().stream().forEach(b -> b.setText(null));

					Square square = getSquare(button);

					boolean deselectFrom = false;
					boolean deselectTo = true;
					SButton selectFrom = null;
					SButton selectTo = null;

					if (square.isOccupiedBy(getCurrentPlayer())) {
						deselectFrom = true;
						if (from != button)
							selectFrom = button;
					}

					else if (from != null) {
						if (to == null)
							if (getMoves(from).contains(square))
								selectTo = button;
							else
								deselectFrom = true;
						else if (to != button)
							deselectFrom = true;
					}

					if (deselectFrom && from != null)
						from = from.deselect();
					if (deselectTo && to != null)
						to = to.deselect();
					if (selectFrom != null)
						from = selectFrom.select();
					if (selectTo != null)
						to = selectTo.select();

					if (to != null)
						previewMove();
					else if (from != null)
						getMoves(from).stream().forEach(s -> getButton(s).setText("●"));
				}
			});
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean enterKeyReleased = e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_RELEASED;
				boolean moveSelected = from != null && to != null;
				if (enterKeyReleased && moveSelected) {
					getKingButton(getCurrentPlayer()).resetBorder();
					makeMove();
					updateBoard();
					turnInfo();
				}
				return enterKeyReleased;
			}

			private void makeMove() {
				Square fromSquare = getSquare(from);
				Square toSquare = getSquare(to);

				String move = fromSquare.getCoordinates() + toSquare.getCoordinates();

				Board newBoard = game.move(move);

				for (SButton button : getButtons()) {
					Square square = getSquare(button);
					char file = square.getFile();
					int rank = square.getRank();
					Square newSquare = newBoard.getSquare(file, rank);
					map.put(button, newSquare);
				}

				from = from.deselect();
				to = to.deselect();
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateScale();
				updateGUI();
			}
		});

		turnInfo();
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
		checkBorder = BorderFactory.createLineBorder(Color.red, Math.max(1, scale / 20));
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

			updateIcon(button, square);
		}

		Player currentPlayer = getCurrentPlayer();

		if (currentPlayer.inCheck(getBoard()))
			getKingButton(currentPlayer).setBorder(checkBorder);

		if (to != null)
			previewMove();
	}

	private void previewMove() {
		updateIcon(from, null);
		updateIcon(to, getSquare(from));
	}

	private void unpreviewMove() {
		updateIcon(from, getSquare(from));
		updateIcon(to, getSquare(to));
	}

	/**
	 * Sets the icon of the given button to an image of the piece occupying the
	 * given square. If the square is {@code null} or not occupied by any piece, an
	 * empty icon is set.
	 * 
	 * @param button the button
	 * @param square the square
	 */
	private void updateIcon(SButton button, Square square) {
		Icon icon = null;
		if (square != null && square.isOccupied()) {
			Image imageFull = square.getPiece().getImage();
			Image imageScaled = imageFull.getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
			icon = new ImageIcon(imageScaled);
		}
		button.setIcon(icon);
	}

	private void turnInfo() {
		Player currentPlayer = getCurrentPlayer();
		int n = currentPlayer.numberOfMoves(getBoard());

		System.out.printf("%s has %d legal move%s.\n", currentPlayer.getName(), n, n == 1 ? "" : "s");

		if (n == 0) {
			String message;
			Icon icon = null;

			if (currentPlayer.inCheck(getBoard())) {
				Player lastPlayer = getLastPlayer();
				message = lastPlayer.getName() + " wins by checkmate!";
				icon = getKingButton(lastPlayer).getIcon();
			} else {
				message = "It's a stalemate!";
			}

			JOptionPane.showMessageDialog(this, message, "Game over", JOptionPane.PLAIN_MESSAGE, icon);
		}
	}

}
