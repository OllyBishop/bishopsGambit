package players;

import java.util.ArrayList;

import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Player {

	private Colour colour;
	private ArrayList<Piece> pieces;

	private void setColour(Colour colour) {
		this.colour = colour;
	}

	public Colour getColour() {
		return this.colour;
	}

	private void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}

	public ArrayList<Piece> getPieces() {
		return this.pieces;
	}

	public Player(Colour colour) {
		setColour(colour);

		int backRank = colour == Colour.WHITE ? 1 : 8;
		int pawnRank = colour == Colour.WHITE ? 2 : 7;

		ArrayList<Piece> pieces = new ArrayList<Piece>();

		pieces.add(new Pawn(colour, 'a', pawnRank));
		pieces.add(new Pawn(colour, 'b', pawnRank));
		pieces.add(new Pawn(colour, 'c', pawnRank));
		pieces.add(new Pawn(colour, 'd', pawnRank));
		pieces.add(new Pawn(colour, 'e', pawnRank));
		pieces.add(new Pawn(colour, 'f', pawnRank));
		pieces.add(new Pawn(colour, 'g', pawnRank));
		pieces.add(new Pawn(colour, 'h', pawnRank));

		pieces.add(new Rook(colour, 'a', backRank));
		pieces.add(new Rook(colour, 'h', backRank));

		pieces.add(new Knight(colour, 'b', backRank));
		pieces.add(new Knight(colour, 'g', backRank));

		pieces.add(new Bishop(colour, 'c', backRank));
		pieces.add(new Bishop(colour, 'f', backRank));

		pieces.add(new Queen(colour, 'd', backRank));

		pieces.add(new King(colour, 'e', backRank));

		setPieces(pieces);
	}

}
