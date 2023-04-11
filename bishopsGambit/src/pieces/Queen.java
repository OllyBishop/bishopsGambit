package pieces;

import players.Colour;

public class Queen extends Piece {

	@Override
	public int getValue() {
		return 9;
	}

	public Queen(Colour colour, char startFile, int startRank) {
		super(colour, startFile, startRank);
	}

}
