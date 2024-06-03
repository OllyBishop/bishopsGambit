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

public class Player {

	private final Colour colour;
	private final int direction;

	private final List<Piece> pieces = new ArrayList<>();
	private final Rook queensideRook;
	private final Rook kingsideRook;
	private final King king;

	public Player(Colour colour) {
		int backRank;
		int pawnRank;

		switch (colour) {
		case WHITE:
			backRank = 1;
			pawnRank = 2;
			break;
		case BLACK:
			backRank = 8;
			pawnRank = 7;
			break;
		default:
			throw new IllegalArgumentException("Colour must be either 'WHITE' or 'BLACK'.");
		}

		this.colour = colour;
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

	public Colour getColour() {
		return this.colour;
	}

	public int getDirection() {
		return this.direction;
	}

	public List<Piece> getPieces() {
		return this.pieces;
	}

	public Rook getQueensideRook() {
		return this.queensideRook;
	}

	public Rook getKingsideRook() {
		return this.kingsideRook;
	}

	public Rook getRook(int x) {
		if (x == -1)
			return getQueensideRook();
		if (x == 1)
			return getKingsideRook();
		return null;
	}

	public King getKing() {
		return this.king;
	}

	@Override
	public String toString() {
		return getColour().toString();
	}

	/**
	 * Returns a boolean indicating whether or not this player's king is currently
	 * in check.
	 * 
	 * @param board the chess board
	 * @return {@code true} if this player's king is currently in check;
	 *         {@code false} otherwise
	 */
	public boolean inCheck(Board board) {
		return getKing().isTargeted(board);
	}

	/**
	 * Returns a boolean indicating whether or not this player is currently in
	 * checkmate.
	 * 
	 * @param board the chess board
	 * @return {@code true} if this player is currently in checkmate; {@code false}
	 *         otherwise
	 */
	public boolean inCheckmate(Board board) {
		return noLegalMoves(board) && inCheck(board);
	}

	/**
	 * Returns a boolean indicating whether or not this player is currently in
	 * stalemate.
	 * 
	 * @param board the chess board
	 * @return {@code true} if this player is currently in stalemate; {@code false}
	 *         otherwise
	 */
	public boolean inStalemate(Board board) {
		return noLegalMoves(board) && !inCheck(board);
	}

	/**
	 * Calculates the number of legal moves this player can make.
	 * 
	 * @param board the chess board
	 * @return the number of legal moves this player can make
	 */
	public int numberOfLegalMoves(Board board) {
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
	 * @return {@code true} if this player has no legal moves; {@code false}
	 *         otherwise
	 */
	public boolean noLegalMoves(Board board) {
		return numberOfLegalMoves(board) == 0;
	}

	public enum Colour {
		WHITE, BLACK;

		@Override
		public String toString() {
			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}

}
