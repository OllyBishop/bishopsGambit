package pieces;

import players.Colour;

public class Rook extends Piece {

	@Override
	public int getValue() {
		return 5;
	}

	public Rook(Colour colour, char startFile, int startRank) {
		super(colour, startFile, startRank);
	}

}
