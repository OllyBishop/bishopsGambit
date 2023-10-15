package core;

import board.Square;

public class IllegalMoveException extends RuntimeException {

	public IllegalMoveException(Square from, Square to) {
		super(String.format("Cannot move %s from %s to %s because it is an illegal move.", from.getPiece(), from, to));
	}

}
