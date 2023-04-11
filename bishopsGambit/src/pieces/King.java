package pieces;

import players.Colour;

public class King extends Piece {

	@Override
	public int getValue() {
		return 0;
	}

	public King(Colour colour, char startFile, int startRank) {
		super(colour, startFile, startRank);
	}

}
