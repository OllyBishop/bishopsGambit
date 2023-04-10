package gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import board.Board;
import board.Square;
import players.Colour;

public class ChessGUI extends JFrame {

	private JPanel contentPane;

	private Board board;

	private void setBoard(Board board) {
		this.board = board;
	}

	private Board getBoard() {
		return this.board;
	}

	/**
	 * Create the frame.
	 */
	public ChessGUI(Board board) {
		setBoard(board);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);

		contentPane = new JPanel();
		contentPane.setLayout(null);

		setContentPane(contentPane);

		for (Square square : board) {
			contentPane.add(square);
		}

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resizeBoard();
			}
		});

		updateGUI();
	}

	public void updateGUI() {
		for (Square square : getBoard()) {
			square.updateIcon();
		}
	}

	public void resizeBoard() {
		resizeBoard(Colour.WHITE);
	}

	public void resizeBoard(Colour perspective) {
		int width = contentPane.getWidth();
		int height = contentPane.getHeight();

		int xMid = width / 2;
		int yMid = height / 2;
		int scale = Math.min(width, height) / 10;
		scale = Math.max(10, scale);

		for (Square square : getBoard()) {
			char file = square.getFile();
			int rank = square.getRank();

			int fileIndex = Square.fileToIndex(file);
			int rankIndex = Square.rankToIndex(rank);

			int x, y;
			if (perspective == Colour.WHITE) {
				x = xMid - (4 - fileIndex) * scale;
				y = yMid + (3 - rankIndex) * scale;
			} else {
				x = xMid + (3 - fileIndex) * scale;
				y = yMid - (4 - rankIndex) * scale;
			}
			square.setBounds(x, y, scale, scale);

			if (square.isOccupied()) {
				Image image = square.getPiece().getImage().getScaledInstance(scale, scale, Image.SCALE_SMOOTH);
				square.setIcon(new ImageIcon(image));
			}
		}
	}
}
