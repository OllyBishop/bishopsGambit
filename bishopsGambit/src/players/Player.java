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

	private Colour colour;
	private int direction;
	private List<Piece> pieces;
	private King king;

	private void setColour(Colour colour) {
		this.colour = colour;
	}

	public Colour getColour() {
		return this.colour;
	}

	private void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return this.direction;
	}

	private void setPieces(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public List<Piece> getPieces() {
		return this.pieces;
	}

	private void setKing(King king) {
		this.king = king;
	}

	public King getKing() {
		return this.king;
	}

	public Player(Colour colour) {
		setColour(colour);

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

		setDirection(direction);

		List<Piece> pieces = new ArrayList<Piece>();

		pieces.add(new Pawn(this, 'a', pawnRank));
		pieces.add(new Pawn(this, 'b', pawnRank));
		pieces.add(new Pawn(this, 'c', pawnRank));
		pieces.add(new Pawn(this, 'd', pawnRank));
		pieces.add(new Pawn(this, 'e', pawnRank));
		pieces.add(new Pawn(this, 'f', pawnRank));
		pieces.add(new Pawn(this, 'g', pawnRank));
		pieces.add(new Pawn(this, 'h', pawnRank));

		pieces.add(new Rook(this, 'a', backRank));
		pieces.add(new Rook(this, 'h', backRank));

		pieces.add(new Knight(this, 'b', backRank));
		pieces.add(new Knight(this, 'g', backRank));

		pieces.add(new Bishop(this, 'c', backRank));
		pieces.add(new Bishop(this, 'f', backRank));

		pieces.add(new Queen(this, 'd', backRank));

		setKing(new King(this, 'e', backRank));
		pieces.add(getKing());

		setPieces(pieces);
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
		int noOfMoves = 0;
		for (Piece p : getPieces()) {
			noOfMoves += p.getMoves(board).size();
		}
		return noOfMoves;
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
