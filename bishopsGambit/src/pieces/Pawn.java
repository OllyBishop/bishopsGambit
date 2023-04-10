package pieces;

import players.Colour;

public class Pawn extends Piece {

	public Pawn(Colour colour, char startingFile, int startingRank) {
		super(PAWN_VALUE, colour, startingFile, startingRank);
	}

}
