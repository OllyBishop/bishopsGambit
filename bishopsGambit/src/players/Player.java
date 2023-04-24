package players;

import java.util.ArrayList;

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
	private ArrayList<Piece> pieces;
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

	private void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public ArrayList<Piece> getPieces() {
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

		ArrayList<Piece> pieces = new ArrayList<Piece>();

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

	public boolean inCheck(Board board) {
		return getKing().isTargeted(board);
	}

}
