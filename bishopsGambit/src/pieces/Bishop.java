package pieces;

import players.Colour;

public class Bishop extends Piece {

	@Override
	public int getValue() {
		return 3;
	}

	public Bishop(Colour colour, char startFile, int startRank) {
		super(colour, startFile, startRank);
	}

}
