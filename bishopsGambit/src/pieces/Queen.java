package pieces;

import players.Colour;

public class Queen extends Piece {

	public Queen(Colour colour, char startingFile, int startingRank) {
		super(QUEEN_VALUE, colour, startingFile, startingRank);
	}

}
