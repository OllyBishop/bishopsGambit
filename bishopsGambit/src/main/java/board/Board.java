package main.java.board;

import java.util.ArrayList;
import java.util.List;

import main.java.pieces.King;
import main.java.pieces.Pawn;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.pieces.Rook;
import main.java.player.Player;

public class Board extends ArrayList<Square>
{
    // Castling rights
    private boolean whiteQueensideCastlingAllowed = true;
    private boolean whiteKingsideCastlingAllowed = true;
    private boolean blackQueensideCastlingAllowed = true;
    private boolean blackKingsideCastlingAllowed = true;

    private Pawn enPassantPawn = null;

    public boolean isWhiteQueensideCastlingAllowed()
    {
        return whiteQueensideCastlingAllowed;
    }

    public boolean isWhiteKingsideCastlingAllowed()
    {
        return whiteKingsideCastlingAllowed;
    }

    public boolean isBlackQueensideCastlingAllowed()
    {
        return blackQueensideCastlingAllowed;
    }

    public boolean isBlackKingsideCastlingAllowed()
    {
        return blackKingsideCastlingAllowed;
    }

    public Pawn getEnPassantPawn()
    {
        return enPassantPawn;
    }

    /**
     * Creates an ArrayList of 64 squares, comprising the chess board.
     */
    public Board()
    {
        for ( char file = 'a'; file <= 'h'; file++ )
            for ( char rank = '1'; rank <= '8'; rank++ )
                add( new Square( file, rank ) );
    }

    /**
     * Returns a list of all pieces currently on the board.
     * 
     * @return a list of all pieces currently on the board
     */
    public List<Piece> getPieces()
    {
        return stream().filter( Square::isOccupied )
                       .map( Square::getPiece )
                       .toList();
    }

    /**
     * Assigns each of the given player's pieces to its start square.
     * 
     * @param player the player whose pieces are to be assigned
     */
    public void assignPieces( Player player )
    {
        for ( Piece piece : player.getPieces() )
            piece.getStartSquare( this ).setPiece( piece );
    }

    /**
     * Returns a boolean indicating whether this board contains the given piece; i.e., whether the
     * given piece is currently occupying any square on this board.
     * 
     * @param piece the piece
     * @return {@code true} if this board contains the given piece; {@code false} otherwise
     */
    public boolean containsPiece( Piece piece )
    {
        return stream().anyMatch( sq -> sq.getPiece() == piece );
    }

    /**
     * Finds the square whose coordinates match the given string. For example, an input of "a1"
     * finds the square with file 'a' and rank '1'.
     * 
     * @param coords the coordinates of the desired square as a string
     * @return the square whose coordinates match the given string (if it exists); {@code null}
     *         otherwise
     */
    public Square getSquare( String coords )
    {
        return getSquare( coords.charAt( 0 ), coords.charAt( 1 ) );
    }

    /**
     * Finds the square with the given file and rank.
     * 
     * @param file the file of the square to be found
     * @param rank the rank of the square to be found
     * @return the square with the given file and rank (if it exists); {@code null} otherwise
     */
    public Square getSquare( char file, char rank )
    {
        if ( isValidSquare( file, rank ) )
            return get( Square.getIndex( file, rank ) );

        return null;
    }

    public static boolean isValidSquare( String coords )
    {
        return isValidSquare( coords.charAt( 0 ), coords.charAt( 1 ) );
    }

    private static boolean isValidSquare( char file, char rank )
    {
        return 'a' <= file && file <= 'h' && '1' <= rank && rank <= '8';
    }

    /**
     * Clones this board and moves the piece occupying the <b>from</b> square to the <b>to</b>
     * square. Also handles the following special moves (if applicable):
     * <ul>
     * <li>Castling – Moves the corresponding rook to the square adjacent to the king.</li>
     * <li>En passant – Removes the corresponding pawn.</li>
     * </ul>
     * 
     * @param from the square containing the piece to be moved
     * @param to   the destination square for the piece
     * @return the new {@code Board} object
     */
    public Board cloneAndMove( Square from, Square to )
    {
        Board newBoard = (Board) clone();
        newBoard.move( from, to );
        return newBoard;
    }

    private void move( Square from, Square to )
    {
        movePiece( from, to );

        Piece piece = from.getPiece();

        boolean enPassant = piece instanceof Pawn && piece.movedOneSquareDiagonallyForward( from, to ) && !to.isOccupied();
        boolean castling = piece instanceof King && piece.movedTwoSquaresHorizontally( from, to );

        if ( enPassant || castling )
        {
            int x = Integer.signum( to.fileDiff( from ) );
            Square s1 = from.travel( this, x, 0 );

            if ( enPassant )
                replace( s1, s1.clone() );

            if ( castling )
            {
                Rook rook = piece.getPlayer().getRook( x );
                Square r = rook.getSquare( this );
                movePiece( r, s1 );
            }
        }

        if ( piece instanceof Rook || piece instanceof King )
            revokeCastlingRights( piece );

        if ( to.isOccupied() && to.getPiece() instanceof Rook )
            revokeCastlingRights( to.getPiece() );

        updateEnPassantPawn( from, to );
    }

    /**
     * Moves the piece occupying the <b>from</b> square to the <b>to</b> square. The from and to
     * squares are cloned, then the piece is unassigned from the new from square and assigned to the
     * new to square.
     * 
     * @param from the square containing the piece to be moved
     * @param to   the destination square for the piece
     */
    private void movePiece( Square from, Square to )
    {
        replace( from, from.clone() );
        replace( to, to.clone( from.getPiece() ) );
    }

    /**
     * Replaces the square <b>s1</b> on this board with the square <b>s2</b>.
     * 
     * @param s1 the old square
     * @param s2 the new square
     */
    private void replace( Square s1, Square s2 )
    {
        int index = indexOf( s1 );
        remove( index );
        add( index, s2 );
    }

    public Piece promote( Pawn pawn, Typ promType )
    {
        Square square = pawn.getSquare( this );
        Piece promPiece = Piece.newInstance( promType,
                                             pawn.getPlayer(),
                                             square.getFile(),
                                             square.getRank() );
        square.setPiece( promPiece );
        return promPiece;
    }

    private void revokeCastlingRights( Piece piece )
    {
        Player player = piece.getPlayer();

        switch ( player.getColour() )
        {
            case WHITE:
                if ( piece == player.getQueensideRook() ||
                     piece == player.getKing() )
                    whiteQueensideCastlingAllowed = false;

                if ( piece == player.getKingsideRook() ||
                     piece == player.getKing() )
                    whiteKingsideCastlingAllowed = false;

                break;

            case BLACK:
                if ( piece == player.getQueensideRook() ||
                     piece == player.getKing() )
                    blackQueensideCastlingAllowed = false;

                if ( piece == player.getKingsideRook() ||
                     piece == player.getKing() )
                    blackKingsideCastlingAllowed = false;

                break;
        }
    }

    private void updateEnPassantPawn( Square from, Square to )
    {
        Piece piece = from.getPiece();

        if ( piece instanceof Pawn && piece.movedTwoSquaresForward( from, to ) )
            // Allow this pawn to be captured en passant on the next turn
            enPassantPawn = (Pawn) piece;
        else
            enPassantPawn = null;
    }

    public boolean isLegalMove( Square from, Square to )
    {
        return from.getPiece().getMoves( this ).contains( to );
    }

    public int getMaterialDifference()
    {
        return getPieces().stream()
                          .mapToInt( pc -> pc.getSign() * pc.getValue() )
                          .sum();
    }

    public boolean hasInsufficientMaterial()
    {
        // All pieces currently on the board (excluding the Kings)
        List<Piece> pieces = getPieces().stream().filter( pc -> !pc.isType( Typ.KING ) ).toList();

        return switch ( pieces.size() )
        {
            // King versus King
            case 0 -> true;

            // King and Bishop versus King, King and Knight versus King
            case 1 -> pieces.stream().allMatch( pc -> pc.isType( Typ.BISHOP, Typ.KNIGHT ) );

            // King and Bishop versus King and Bishop with the Bishops on the same colour
            case 2 -> pieces.stream().allMatch( pc -> pc.isType( Typ.BISHOP ) ) &&
                      pieces.stream().map( Piece::getPlayer ).distinct().count() > 1 &&
                      pieces.stream().map( pc -> pc.getSquare( this ).getParity() ).distinct().count() == 1;

            default -> false;
        };
    }

    public void print()
    {
        Printer.print( this );
    }

    private static class Printer
    {
        // For each box-drawing char below, n is the number of "prongs" that char has.
        // To convert box-drawing chars from light to heavy, add (1 << n) - 1 to each.

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

        private static final String UPPER_ROW = getRow( DOWN_RIGHT, DOWN_HORIZONTAL, DOWN_LEFT );
        private static final String INNER_ROW = getRow( VERTICAL_RIGHT, VERTICAL_HORIZONTAL, VERTICAL_LEFT );
        private static final String LOWER_ROW = getRow( UP_RIGHT, UP_HORIZONTAL, UP_LEFT );

        private static String getRow( char right, char horizontal, char left )
        {
            StringBuilder row = new StringBuilder();

            row.append( right );

            for ( char file = 'a'; file <= 'h'; file++ )
            {
                for ( int j = 0; j < 3; j++ )
                    row.append( HORIZONTAL );

                if ( file < 'h' )
                    row.append( horizontal );
            }

            row.append( left );

            return row.toString();
        }

        private static void print( Board board )
        {
            System.out.println( UPPER_ROW );

            for ( char rank = '8'; rank >= '1'; rank-- )
            {
                StringBuilder pieceRow = new StringBuilder();

                pieceRow.append( VERTICAL );

                for ( char file = 'a'; file <= 'h'; file++ )
                    pieceRow.append( ' ' )
                            .append( board.getSquare( file, rank ).toChar() )
                            .append( ' ' )
                            .append( VERTICAL );

                System.out.println( pieceRow );

                if ( rank > '1' )
                    System.out.println( INNER_ROW );
            }

            System.out.println( LOWER_ROW );
        }
    }
}
