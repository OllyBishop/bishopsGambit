package pieces;

import players.Colour;

public class Knight extends Piece {

	@Override
	public int getValue() {
		return 3;
	}

	public Knight(Colour colour, char startFile, int startRank) {
		super(colour, startFile, startRank);
	}

}
