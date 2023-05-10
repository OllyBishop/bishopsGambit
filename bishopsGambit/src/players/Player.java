package players;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Square;
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

	private final List<Piece> pieces = new ArrayList<Piece>();;
	private final Rook queenRook;
	private final Rook kingRook;
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

	public Rook getQueenRook() {
		return this.queenRook;
	}

	public Rook getKingRook() {
		return this.kingRook;
	}

	public King getKing() {
		return this.king;
	}

	public Player(Colour colour) {
		this.colour = colour;

		int direction = 0;
		int backRank = 0;
		int pawnRank = 0;

		switch (colour) {
		case WHITE:
			direction = 1;
			backRank = 1;
			pawnRank = 2;
			break;
		case BLACK:
			direction = -1;
			backRank = 8;
			pawnRank = 7;
			break;
		}

		this.direction = direction;

		new Pawn(this, 'a', pawnRank);
		new Pawn(this, 'b', pawnRank);
		new Pawn(this, 'c', pawnRank);
		new Pawn(this, 'd', pawnRank);
		new Pawn(this, 'e', pawnRank);
		new Pawn(this, 'f', pawnRank);
		new Pawn(this, 'g', pawnRank);
		new Pawn(this, 'h', pawnRank);

		this.queenRook = new Rook(this, 'a', backRank);
		this.kingRook = new Rook(this, 'h', backRank);

		new Knight(this, 'b', backRank);
		new Knight(this, 'g', backRank);

		new Bishop(this, 'c', backRank);
		new Bishop(this, 'f', backRank);

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
		for (Piece piece : getPieces()) {
			if (!piece.isCaptured())
				numberOfMoves += piece.getMoves(board).size();
		}
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

	public Rook getCastlingRook(int x) {
		Rook rook = null;
		switch (x) {
		case -1:
			rook = getQueenRook();
			break;
		case 1:
			rook = getKingRook();
			break;
		}
		return rook;
	}

	public Square getCastlingSquare(Board board, int x) {
		return board.getSquare((char) (getKing().getStartFile() + x), getKing().getStartRank());
	}

}
