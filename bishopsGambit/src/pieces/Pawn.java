package pieces;

import players.Colour;

public class Pawn extends Piece {

	@Override
	public int getValue() {
		return 1;
	}

	public Pawn(Colour colour, char startFile, int startRank) {
		super(colour, startFile, startRank);
	}

}
