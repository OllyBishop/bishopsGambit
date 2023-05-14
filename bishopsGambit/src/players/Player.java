package players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final Map<Integer, Rook> rooks = new HashMap<Integer, Rook>();
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

	private Map<Integer, Rook> getRooks() {
		return this.rooks;
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

		Pawn aPawn = new Pawn(this, 'a', pawnRank);
		Pawn bPawn = new Pawn(this, 'b', pawnRank);
		Pawn cPawn = new Pawn(this, 'c', pawnRank);
		Pawn dPawn = new Pawn(this, 'd', pawnRank);
		Pawn ePawn = new Pawn(this, 'e', pawnRank);
		Pawn fPawn = new Pawn(this, 'f', pawnRank);
		Pawn gPawn = new Pawn(this, 'g', pawnRank);
		Pawn hPawn = new Pawn(this, 'h', pawnRank);

		Knight queenKnight = new Knight(this, 'b', backRank);
		Knight kingKnight = new Knight(this, 'g', backRank);

		Bishop queenBishop = new Bishop(this, 'c', backRank);
		Bishop kingBishop = new Bishop(this, 'f', backRank);

		Rook queenRook = new Rook(this, 'a', backRank);
		Rook kingRook = new Rook(this, 'h', backRank);

		Queen queen = new Queen(this, 'd', backRank);

		King king = new King(this, 'e', backRank);

		getPieces().add(aPawn);
		getPieces().add(bPawn);
		getPieces().add(cPawn);
		getPieces().add(dPawn);
		getPieces().add(ePawn);
		getPieces().add(fPawn);
		getPieces().add(gPawn);
		getPieces().add(hPawn);

		getPieces().add(queenKnight);
		getPieces().add(kingKnight);

		getPieces().add(queenBishop);
		getPieces().add(kingBishop);

		getPieces().add(queenRook);
		getPieces().add(kingRook);

		getPieces().add(queen);

		getPieces().add(king);

		getRooks().put(-1, queenRook);
		getRooks().put(1, kingRook);

		this.king = king;
	}

	public String getName() {
		return getColour().toString();
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

	public Rook getRook(int x) {
		return getRooks().get(x);
	}

	public Square getCastlingSquare(Board board, int x) {
		return board.getSquare((char) (getKing().getStartFile() + x), getKing().getStartRank());
	}

}
