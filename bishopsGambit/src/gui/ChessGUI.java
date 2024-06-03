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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import board.Board;
import board.Square;
import core.Game;
import pieces.Piece;
import pieces.Piece.Typ;
import players.Player;
import utils.ComponentUtils;

public class ChessGUI extends JFrame {

	private static final int SQUARE_LAYER = 1;
	private static final int FILE_RANK_LAYER = 2;
	private static final int PIECE_LAYER = 3;
	private static final int PLACEHOLDER_LAYER = 4;

	// Fields --------------------------------------------------------- //
	private final JLayeredPane contentPane = new JLayeredPane();

	private final Map<PieceLabel, SquareButton> boardMap = new HashMap<>();

	private final List<PieceLabel> pieceLbls = new ArrayList<>();
	private final List<SquareButton> squareBtns = new ArrayList<>();

	private final List<JLabel> fileLbls = new ArrayList<>();
	private final List<JLabel> rankLbls = new ArrayList<>();

	private int xMid;
	private int yMid;
	private int scale;

	private Border checkBorder;
	private boolean inCheck;
	private SquareButton kingSquareBtn;

	private SquareButton from;
	private SquareButton to;

	private Game game;
	// ---------------------------------------------------------------- //

	// Getters and setters -------------------------------------------- //
	private Game getGame() {
		return this.game;
	}

	private void setGame(Game game) {
		this.game = game;
	}
	// ---------------------------------------------------------------- //

	// Map methods ---------------------------------------------------- //
	private Square getSquare(SquareButton squareBtn) {
		return getBoard().get(squareBtn.getIndex());
	}

	private SquareButton getSquareButton(Square square) {
		return squareBtns.get(square.getIndex());
	}

	private SquareButton getSquareButton(Board board, Piece piece) {
		return getSquareButton(piece.getSquare(board));
	}

	private void updateBoardMap() {
		Board board;
		if (to == null)
			board = getBoard();
		else
			board = getBoard().move(getSquare(from), getSquare(to));

		for (PieceLabel pieceLbl : pieceLbls) {
			Piece piece = pieceLbl.getPiece();
			if (board.containsPiece(piece))
				boardMap.put(pieceLbl, getSquareButton(board, piece));
			else
				boardMap.put(pieceLbl, null);
		}
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
	 * @return White if the number of turns taken is even; Black if it is odd
	 */
	private Player getCurrentPlayer() {
		return getGame().getCurrentPlayer();
	}

	/**
	 * Returns the opponent of the player whose turn it currently is, based on the
	 * number of turns taken.
	 * 
	 * @return Black if the number of turns taken is even; White if it is odd
	 */
	private Player getCurrentOpponent() {
		return getGame().getCurrentOpponent();
	}

	private List<Square> getMoves(SquareButton squareBtn) {
		return getSquare(squareBtn).getPiece().getMoves(getBoard());
	}
	// ---------------------------------------------------------------- //

	private void addToLayer(JComponent component, int layer) {
		contentPane.add(component, layer, 0);
	}

	private void createPieceLabel(Piece piece) {
		PieceLabel pieceLbl = new PieceLabel(piece);
		pieceLbls.add(pieceLbl);
		addToLayer(pieceLbl, PIECE_LAYER);
	}

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

		// Create square buttons
		for (Square square : getBoard()) {
			SquareButton squareBtn = new SquareButton(square.getFile(), square.getRank());
			squareBtns.add(squareBtn);
			addToLayer(squareBtn, SQUARE_LAYER);
			addToLayer(squareBtn.getPlaceholder(), PLACEHOLDER_LAYER);
		}

		// Create file labels
		for (char file = 'a'; file <= 'h'; file++) {
			JLabel fileLbl = new JLabel(Character.toString(file));
			fileLbl.setHorizontalAlignment(SwingConstants.CENTER);
			fileLbl.setVerticalAlignment(SwingConstants.TOP);
			fileLbls.add(fileLbl);
			addToLayer(fileLbl, FILE_RANK_LAYER);
		}

		// Create rank labels
		for (int rank = 1; rank <= 8; rank++) {
			JLabel rankLbl = new JLabel(Integer.toString(rank));
			rankLbl.setHorizontalAlignment(SwingConstants.RIGHT);
			rankLbl.setVerticalAlignment(SwingConstants.CENTER);
			rankLbls.add(rankLbl);
			addToLayer(rankLbl, FILE_RANK_LAYER);
		}

		// Create piece labels
		for (Piece piece : game.getAllPieces()) {
			createPieceLabel(piece);
		}

		updateBoardMap();

		for (SquareButton squareBtn : squareBtns) {
			squareBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (from != null && to == null)
						for (SquareButton sqBtn : squareBtns)
							sqBtn.getPlaceholder().setVisible(false);

					// The square of the button that was pressed
					Square square = getSquare(squareBtn);

					boolean deselectFrom = false;
					boolean deselectTo = true;
					SquareButton selectFrom = null;
					SquareButton selectTo = null;

					if (square.isOccupiedBy(getCurrentPlayer())) {
						deselectFrom = true;
						if (from != squareBtn)
							selectFrom = squareBtn;
					}

					else if (from != null) {
						if (to == null)
							if (getMoves(from).contains(square))
								selectTo = squareBtn;
							else
								deselectFrom = true;
						else if (to != squareBtn)
							deselectFrom = true;
					}

					SquareButton toBefore = to;

					if (deselectFrom && from != null)
						from = from.deselect();
					if (deselectTo && to != null)
						to = to.deselect();
					if (selectFrom != null)
						from = selectFrom.select();
					if (selectTo != null)
						to = selectTo.select();

					// If assignment of 'to' changed, update map and reposition pieces
					if (to != toBefore) {
						updateBoardMap();
						positionPieces();
					}

					if (from != null && to == null)
						for (Square sq : getMoves(from))
							getSquareButton(sq).getPlaceholder().setVisible(true);
				}
			});
		}

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				boolean enterKeyReleased = e.getKeyCode() == KeyEvent.VK_SPACE && e.getID() == KeyEvent.KEY_RELEASED;
				boolean moveSelected = from != null && to != null;

				if (enterKeyReleased && moveSelected) {
					makeMove();
					postMove();
					refreshBoard();
				}

				return enterKeyReleased;
			}

			private void makeMove() {
				Square fromSquare = getSquare(from);
				Square toSquare = getSquare(to);

				Typ promotionType = null;

				if (fromSquare.getPiece().canPromote(getBoard(), toSquare)) {
					Typ[] options = new Typ[] { Typ.KNIGHT, Typ.BISHOP, Typ.ROOK, Typ.QUEEN };

					int i = JOptionPane.showOptionDialog(rootPane, "Select a piece to promote to.", "Promotion",
							JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, to.getIcon(), options, null);

					if (i == JOptionPane.CLOSED_OPTION)
						promotionType = Typ.QUEEN;
					else
						promotionType = options[i];
				}

				Piece promotedPiece = game.move(fromSquare, toSquare, promotionType);

				if (promotedPiece != null)
					createPieceLabel(promotedPiece);

				from = from.deselect();
				to = to.deselect();

				// TODO: Can remove this once promotion previews are implemented
				updateBoardMap();
			}

			private void postMove() {
				if (inCheck)
					kingSquareBtn.clearBorder();

				Player currentPlayer = getCurrentPlayer();
				inCheck = currentPlayer.inCheck(getBoard());
				kingSquareBtn = getSquareButton(getBoard(), currentPlayer.getKing());

				if (currentPlayer.noLegalMoves(getBoard())) {
					String message;
					Icon icon;

					if (inCheck) {
						Player currentOpponent = getCurrentOpponent();
						message = currentOpponent.getColour() + " wins by checkmate!";
						icon = new ImageIcon(Graphics.getImage(currentOpponent.getKing()));
					} else {
						message = "It's a stalemate!";
						icon = null;
					}

					JOptionPane.showMessageDialog(ChessGUI.this, message, "Game over", JOptionPane.PLAIN_MESSAGE, icon);
				}
			}
		});

		addComponentListener(new ComponentAdapter() {
			/**
			 * This method is called once when the frame is initialised, and each time the
			 * frame is resized thereafter.
			 */
			@Override
			public void componentResized(ComponentEvent e) {
				updateScale();
				refreshBoard();
			}

			private void updateScale() {
				int width = contentPane.getWidth();
				int height = contentPane.getHeight();

				xMid = width / 2;
				yMid = height / 2;
				scale = Math.max(10, Math.min(width, height) / 10);

				checkBorder = BorderFactory.createLineBorder(Color.red, Math.max(1, scale / 20));

				for (SquareButton squareBtn : squareBtns) {
					squareBtn.setSize(scale, scale);
					ComponentUtils.resizeFont(squareBtn.getPlaceholder(), scale);
				}

				for (PieceLabel pieceLbl : pieceLbls) {
					pieceLbl.setSize(scale, scale);
				}

				for (JLabel fileLbl : fileLbls) {
					fileLbl.setSize(scale, scale);
					ComponentUtils.resizeFont(fileLbl, scale / 4);
				}

				for (JLabel rankLbl : rankLbls) {
					rankLbl.setSize(scale, scale);
					ComponentUtils.resizeFont(rankLbl, scale / 4);
				}
			}
		});
	}

	/**
	 * Repositions the chess board (squares, files, ranks, and pieces) relative to
	 * the dimensions of the application window. The board is oriented to the
	 * current player's perspective.
	 */
	private void refreshBoard() {
		refreshBoard(getCurrentPlayer());
	}

	/**
	 * Repositions the chess board (squares, files, ranks, and pieces) relative to
	 * the dimensions of the application window. The board is oriented to the given
	 * player's perspective.
	 * 
	 * @param perspective the player to whose perspective the board is oriented
	 */
	private void refreshBoard(Player perspective) {
		positionSquares(perspective);
		positionFilesAndRanks(perspective);
		positionPieces();

		paintPieces();
		paintCheckBorder();
	}

	private void positionSquares(Player perspective) {
		for (SquareButton squareBtn : squareBtns) {
			int fileIndex = Square.getFileIndex(squareBtn.getFile());
			int rankIndex = Square.getRankIndex(squareBtn.getRank());

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

			squareBtn.setLocation(x, y);
		}
	}

	private void positionFilesAndRanks(Player perspective) {
		for (int index = 0; index <= 7; index++) {
			JLabel fileLbl = fileLbls.get(index);
			JLabel rankLbl = rankLbls.get(index);

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

			fileLbl.setLocation(xFile, yFile);
			rankLbl.setLocation(xRank, yRank);
		}
	}

	private void positionPieces() {
		for (var entry : boardMap.entrySet()) {
			PieceLabel pieceLbl = entry.getKey();
			SquareButton squareBtn = entry.getValue();

			if (squareBtn == null) {
				pieceLbl.setVisible(false);
			} else {
				pieceLbl.setVisible(true);
				pieceLbl.setLocation(squareBtn.getLocation());
			}
		}
	}

	private void paintPieces() {
		for (PieceLabel pieceLbl : pieceLbls)
			pieceLbl.paintIcon();
	}

	private void paintCheckBorder() {
		if (inCheck)
			kingSquareBtn.setBorder(checkBorder);
	}

}
