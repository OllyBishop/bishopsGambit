package players;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import utils.StringUtils;

public class Player {

	public enum Colour {
		WHITE, BLACK;

		@Override
		public String toString() {
			return StringUtils.toUpperCamelCase(name());
		}
	}

	private final Colour colour;
	private final int direction;

	private final List<Piece> pieces = new ArrayList<>();
	private final Rook queensideRook;
	private final Rook kingsideRook;
	private final King king;

	public Colour getColour() {
		return this.colour;
	}

	public int getDirection() {
		return this.direction;
	}

	public List<Piece> getPieces() {
		return this.pieces;
	}

	public Rook getRook(int x) {
		if (x == -1)
			return getQueensideRook();
		if (x == 1)
			return getKingsideRook();
		return null;
	}

	public Rook getQueensideRook() {
		return this.queensideRook;
	}

	public Rook getKingsideRook() {
		return this.kingsideRook;
	}

	public King getKing() {
		return this.king;
	}

	public Player(Colour colour) {
		this.colour = colour;

		int backRank = 0;
		int pawnRank = 0;

		switch (colour) {
		case WHITE:
			backRank = 1;
			pawnRank = 2;
			break;
		case BLACK:
			backRank = 8;
			pawnRank = 7;
			break;
		}

		this.direction = Integer.signum(pawnRank - backRank);

		new Pawn(this, 'a', pawnRank);
		new Pawn(this, 'b', pawnRank);
		new Pawn(this, 'c', pawnRank);
		new Pawn(this, 'd', pawnRank);
		new Pawn(this, 'e', pawnRank);
		new Pawn(this, 'f', pawnRank);
		new Pawn(this, 'g', pawnRank);
		new Pawn(this, 'h', pawnRank);

		new Knight(this, 'b', backRank);
		new Knight(this, 'g', backRank);

		new Bishop(this, 'c', backRank);
		new Bishop(this, 'f', backRank);

		this.queensideRook = new Rook(this, 'a', backRank);
		this.kingsideRook = new Rook(this, 'h', backRank);

		new Queen(this, 'd', backRank);

		this.king = new King(this, 'e', backRank);
	}

	/**
	 * Returns a boolean indicating whether or not this player's king is currently
	 * in check.
	 * 
	 * @param board the chess board
	 * @return <code>true</code> if this player's king is currently in check,
	 *         <code>false</code> otherwise
	 */
	public boolean inCheck(Board board) {
		return getKing().isTargeted(board);
	}

	/**
	 * Returns a boolean indicating whether or not this player is currently in
	 * checkmate.
	 * 
	 * @param board the chess board
	 * @return <code>true</code> if this player is currently in checkmate,
	 *         <code>false</code> otherwise
	 */
	public boolean inCheckmate(Board board) {
		return inCheck(board) && noMoves(board);
	}

	/**
	 * Returns a boolean indicating whether or not this player is currently in
	 * stalemate.
	 * 
	 * @param board the chess board
	 * @return <code>true</code> if this player is currently in stalemate,
	 *         <code>false</code> otherwise
	 */
	public boolean inStalemate(Board board) {
		return !inCheck(board) && noMoves(board);
	}

	/**
	 * Calculates the number of legal moves this player can make.
	 * 
	 * @param board the chess board
	 * @return the number of legal moves this player can make
	 */
	public int numberOfMoves(Board board) {
		int numberOfMoves = 0;
		for (Piece piece : getPieces())
			if (!piece.isCaptured())
				numberOfMoves += piece.getMoves(board).size();
		return numberOfMoves;
	}

	/**
	 * Returns a boolean indicating whether or not this player has any legal moves.
	 * 
	 * @param board the chess board
	 * @return <code>true</code> if this player has no legal moves,
	 *         <code>false</code> otherwise
	 */
	private boolean noMoves(Board board) {
		return numberOfMoves(board) == 0;
	}

}
