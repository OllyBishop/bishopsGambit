package board;

import java.util.ArrayList;

public class Board extends ArrayList<Square> {

	/**
	 * Creates an ArrayList of 64 squares, comprising the chess board.
	 */
	public Board() {
		for (char file = 'a'; file <= 'h'; file++) {
			for (int rank = 1; rank <= 8; rank++) {
				add(new Square(file, rank));
			}
		}
	}

	/**
	 * Finds the square with the given file and rank.
	 * 
	 * @param file the file of the square to be found
	 * @param rank the rank of the square to be found
	 * @return the square with the given file and rank
	 */
	public Square getSquare(char file, int rank) {
		int fileIndex = Square.fileToIndex(file);
		int rankIndex = Square.rankToIndex(rank);
		return get(fileIndex * 8 + rankIndex);
	}

}
