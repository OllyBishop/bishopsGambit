package gui;

import java.awt.Color;
import java.awt.Dimension;
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
import pieces.Piece.Typ;
import players.Player;
import utils.ComponentUtils;
import utils.ListUtils;

public class ChessGUI extends JFrame {

	// Fields --------------------------------------------------------- //
	private final JPanel contentPane = new JPanel();

	private final List<SButton> buttons = new ArrayList<>();

	private final List<JLabel> files = new ArrayList<>();
	private final List<JLabel> ranks = new ArrayList<>();

	private int xMid;
	private int yMid;
	private int scale;

	private Border checkBorder;
	private SButton checkButton;

	private SButton from;
	private SButton to;

	private Game game;
	private Board preview;
	// ---------------------------------------------------------------- //

	// Getters and setters -------------------------------------------- //
	private Game getGame() {
		return this.game;
	}

	private void setGame(Game game) {
		this.game = game;
	}

	private Board getPreview() {
		return this.preview;
	}

	private void setPreview(Board preview) {
		this.preview = preview;
	}
	// ---------------------------------------------------------------- //

	// Map methods ---------------------------------------------------- //
	private Square getSquare(SButton button) {
		return ListUtils.get(getBoard(), buttons, button);
	}

	private SButton getButton(Square square) {
		return ListUtils.get(buttons, getBoard(), square);
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

	/**
	 * Returns the opponent of the player whose turn it currently is, based on the
	 * number of turns taken.
	 * 
	 * @return Black if the number of turns taken is even, White if it is odd
	 */
	private Player getCurrentOpponent() {
		return getGame().getCurrentOpponent();
	}

	private List<Square> getMoves(SButton button) {
		return getSquare(button).getPiece().getMoves(getBoard());
	}
	// ---------------------------------------------------------------- //

	/**
	 * Create the frame.
	 */
	public ChessGUI(Game game) {
		setGame(game);
		updatePreview();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width / 2, screenSize.height);

		contentPane.setLayout(null);
		setContentPane(contentPane);

		// Create square buttons
		for (Square square : getBoard()) {
			SButton button = new SButton(square.getFile(), square.getRank());
			buttons.add(button);
			contentPane.add(button);
		}

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

		for (SButton button : buttons) {
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (from != null && to == null)
						for (SButton btn : buttons)
							btn.setText(null);

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

					SButton toBefore = to;

					if (deselectFrom && from != null)
						from = from.deselect();
					if (deselectTo && to != null)
						to = to.deselect();
					if (selectFrom != null)
						from = selectFrom.select();
					if (selectTo != null)
						to = selectTo.select();

					// If assignment of 'to' changed, repaint pieces
					if (to != toBefore) {
						updatePreview();
						paintPieces();
					}

					if (from != null && to == null)
						for (Square sq : getMoves(from))
							getButton(sq).setText("●");
				}
			});
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean enterKeyReleased = e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_RELEASED;
				boolean moveSelected = from != null && to != null;

				if (enterKeyReleased && moveSelected) {
					makeMove();
					orientBoard();

					clearCheckBorder();
					updateCheckButton();
					paintCheckBorder();

					checkGameOver();
				}

				return enterKeyReleased;
			}

			void makeMove() {
				Square fromSquare = getSquare(from);
				Square toSquare = getSquare(to);

				Typ prom = null;

				if (fromSquare.getPiece().canPromote(getBoard(), toSquare)) {
					Typ[] options = new Typ[] { Typ.KNIGHT, Typ.BISHOP, Typ.ROOK, Typ.QUEEN };

					int i = JOptionPane.showOptionDialog(rootPane, "Select a piece to promote to.", "Promotion",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, to.getIcon(), options, null);

					if (i == JOptionPane.CLOSED_OPTION)
						prom = Typ.QUEEN;
					else
						prom = options[i];
				}

				game.move(fromSquare, toSquare, prom);

				// TODO: Can remove this once promotion previews are implemented
				if (prom != null)
					to.paintIcon(getBoard());

				from = from.deselect();
				to = to.deselect();
			}

			void clearCheckBorder() {
				if (checkButton != null)
					checkButton.clearBorder();
			}

			void updateCheckButton() {
				Player player = getCurrentPlayer();
				if (player.inCheck(getBoard()))
					checkButton = getButton(player.getKing().getSquare(getBoard()));
				else
					checkButton = null;
			}
		});

		addComponentListener(new ComponentAdapter() {
			/**
			 * This method is called when the frame is initialised.
			 */
			@Override
			public void componentResized(ComponentEvent e) {
				updateScale();

				// These three methods should be called after updateScale()
				orientBoard();
				paintPieces();
				paintCheckBorder();
			}

			void updateScale() {
				int width = contentPane.getWidth();
				int height = contentPane.getHeight();

				xMid = width / 2;
				yMid = height / 2;
				scale = Math.max(10, Math.min(width, height) / 10);

				checkBorder = BorderFactory.createLineBorder(Color.red, Math.max(1, scale / 20));

				for (SButton button : buttons) {
					button.setSize(scale, scale);
					ComponentUtils.resizeFont(button, scale);
				}

				for (JLabel file : files) {
					file.setSize(scale, scale);
					ComponentUtils.resizeFont(file, scale / 4);
				}

				for (JLabel rank : ranks) {
					rank.setSize(scale, scale);
					ComponentUtils.resizeFont(rank, scale / 4);
				}
			}
		});
	}

	/**
	 * Repositions the chess board relative to the size of the application window.
	 * The board is oriented to the current player's perspective.
	 */
	private void orientBoard() {
		orientBoard(getCurrentPlayer());
	}

	/**
	 * Repositions the chess board relative to the size of the application window.
	 * The board is oriented to the given player's perspective.
	 * 
	 * @param perspective the player to whose perspective the board is oriented
	 */
	private void orientBoard(Player perspective) {
		for (SButton button : buttons) {
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

			button.setLocation(x, y);
		}

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

			file.setLocation(xFile, yFile);
			rank.setLocation(xRank, yRank);
		}
	}

	private void updatePreview() {
		Board preview;
		if (to == null)
			preview = getBoard();
		else
			preview = getBoard().move(getSquare(from), getSquare(to));
		setPreview(preview);
	}

	private void paintPieces() {
		for (SButton button : buttons)
			button.paintIcon(getPreview());
	}

	private void paintCheckBorder() {
		if (checkButton != null)
			checkButton.setBorder(checkBorder);
	}

	private void checkGameOver() {
		Player currentPlayer = getCurrentPlayer();
		int n = currentPlayer.numberOfLegalMoves(getBoard());

		if (n == 0) {
			String message;
			Icon icon = null;

			if (currentPlayer.inCheck(getBoard())) {
				Player currentOpponent = getCurrentOpponent();
				message = currentOpponent.getColour() + " wins by checkmate!";
				icon = new ImageIcon(Graphics.getImage(currentOpponent.getKing()));
			} else {
				message = "It's a stalemate!";
			}

			JOptionPane.showMessageDialog(this, message, "Game over", JOptionPane.PLAIN_MESSAGE, icon);
		}
	}

}
