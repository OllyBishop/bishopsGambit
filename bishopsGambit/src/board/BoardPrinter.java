package board;

public class BoardPrinter {

	// For each box drawing char below, n is the number of "prongs" that char has
	// To convert box drawing chars from light to heavy, add (1 << n) - 1 to each

	// n = 1
	private static final char HORIZONTAL = '\u2500';
	private static final char VERTICAL = '\u2502';

	// n = 2
	private static final char DOWN_RIGHT = '\u250c';
	private static final char DOWN_LEFT = '\u2510';
	private static final char UP_RIGHT = '\u2514';
	private static final char UP_LEFT = '\u2518';

	// n = 3
	private static final char VERTICAL_RIGHT = '\u251c';
	private static final char VERTICAL_LEFT = '\u2524';
	private static final char DOWN_HORIZONTAL = '\u252c';
	private static final char UP_HORIZONTAL = '\u2534';

	// n = 4
	private static final char VERTICAL_HORIZONTAL = '\u253c';

	private static final String UPPER_ROW = getRow(DOWN_RIGHT, DOWN_HORIZONTAL, DOWN_LEFT);
	private static final String INNER_ROW = getRow(VERTICAL_RIGHT, VERTICAL_HORIZONTAL, VERTICAL_LEFT);
	private static final String LOWER_ROW = getRow(UP_RIGHT, UP_HORIZONTAL, UP_LEFT);

	private static String getRow(char right, char horizontal, char left) {
		StringBuilder row = new StringBuilder();

		row.append(right);
		for (char file = 'a'; file <= 'h'; file++) {
			for (int j = 0; j < 3; j++)
				row.append(HORIZONTAL);
			if (file < 'h')
				row.append(horizontal);
		}
		row.append(left);

		return row.toString();
	}

	public static void print(Board board) {
		System.out.println(UPPER_ROW);

		for (int rank = 8; rank >= 1; rank--) {
			StringBuilder pieceRow = new StringBuilder();

			pieceRow.append(VERTICAL);
			for (char file = 'a'; file <= 'h'; file++)
				pieceRow.append(' ').append(board.getSquare(file, rank).toChar()).append(' ').append(VERTICAL);

			System.out.println(pieceRow);

			if (rank > 1)
				System.out.println(INNER_ROW);
		}

		System.out.println(LOWER_ROW);
	}

}
