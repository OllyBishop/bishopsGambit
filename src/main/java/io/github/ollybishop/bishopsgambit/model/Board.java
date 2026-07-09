package io.github.ollybishop.bishopsgambit.model;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.model.piece.Bishop;
import io.github.ollybishop.bishopsgambit.model.piece.King;
import io.github.ollybishop.bishopsgambit.model.piece.Knight;
import io.github.ollybishop.bishopsgambit.model.piece.Pawn;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;
import io.github.ollybishop.bishopsgambit.model.piece.Rook;

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
     * Constructs a chessboard containing 64 unoccupied squares.
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
     * Returns a boolean indicating whether this board contains the given piece; i.e., whether the given piece is currently
     * occupying any square on this board.
     * 
     * @param piece the piece
     * @return {@code true} if this board contains the given piece; {@code false} otherwise
     */
    boolean containsPiece( Piece piece )
    {
        return stream().anyMatch( square -> square.getPiece() == piece );
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

    public Square getSquare( Square origin, int dx, int dy )
    {
        char file = (char) ( origin.getFile() + dx );
        char rank = (char) ( origin.getRank() + dy );
        return getSquare( file, rank );
    }

    public static boolean isValidSquare( char file, char rank )
    {
        return 'a' <= file && file <= 'h' && '1' <= rank && rank <= '8';
    }

    /**
     * Clones this board and moves the piece occupying the <b>from</b> square to the <b>to</b> square. Also handles the
     * following special moves (if applicable):
     * <ul>
     * <li>Castling: Moves the corresponding rook to the square adjacent to the king.</li>
     * <li>En passant: Removes the corresponding pawn.</li>
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

        int dx = Integer.signum( to.fileDiff( from ) );
        Square adjacent = getSquare( from, dx, 0 );

        // En passant
        if ( piece instanceof Pawn && piece.movedOneSquareDiagonallyForward( from, to ) && to.isEmpty() )
        {
            replace( adjacent, adjacent.copyWithoutPiece() );
        }
        // Castling
        else if ( piece instanceof King && piece.movedTwoSquaresHorizontally( from, to ) )
        {
            Rook rook = piece.getPlayer().getRook( dx );
            Square rookSquare = rook.getSquare( this );
            movePiece( rookSquare, adjacent );
        }

        if ( piece instanceof Rook || piece instanceof King )
            revokeCastlingRights( piece );

        if ( to.getPiece() instanceof Rook )
            revokeCastlingRights( to.getPiece() );

        updateEnPassantPawn( from, to );
    }

    /**
     * Moves the piece occupying the <b>from</b> square to the <b>to</b> square.
     * <p>
     * To preserve board states, the given squares are cloned and the piece occupying the original <b>from</b> square is
     * assigned to the new <i>to</i> square. The new <i>from</i> square is left unoccupied.
     * 
     * @param from the square containing the piece to be moved
     * @param to   the destination square for the piece
     */
    private void movePiece( Square from, Square to )
    {
        Square newFrom = from.copyWithoutPiece();
        Square newTo = to.copyWithoutPiece();
        newTo.setPiece( from.getPiece() );
        replace( from, newFrom );
        replace( to, newTo );
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

    public Piece promote( Pawn pawn, Piece.Type newType )
    {
        Square square = pawn.getSquare( this );
        Piece newPiece = Piece.newInstance(
            newType,
            pawn.getPlayer(),
            square.getFile(),
            square.getRank() );
        square.setPiece( newPiece );
        return newPiece;
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

    public boolean isCastlingAllowed( Rook rook )
    {
        return switch ( rook.getColour() )
        {
            case WHITE -> switch ( rook.getBoardSide() )
            {
                case QUEENSIDE -> whiteQueensideCastlingAllowed;
                case KINGSIDE -> whiteKingsideCastlingAllowed;
            };

            case BLACK -> switch ( rook.getBoardSide() )
            {
                case QUEENSIDE -> blackQueensideCastlingAllowed;
                case KINGSIDE -> blackKingsideCastlingAllowed;
            };
        };
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
        return from.getPiece().getLegalMoves( this ).contains( to );
    }

    public boolean moveWouldLeavePlayerInCheck( Player player, Square from, Square to )
    {
        return player.isInCheck( cloneAndMove( from, to ) );
    }

    /**
     * Returns the material difference on this board from White's perspective.
     * <p>
     * A positive value means White is ahead, while a negative value means Black is ahead.
     *
     * @return the total white material value minus the total black material value
     */
    public int getMaterialDifference()
    {
        return getPieces().stream()
                          .mapToInt( piece -> piece.getPlayerSign() * piece.getValue() )
                          .sum();
    }

    public boolean hasInsufficientMaterial()
    {
        // Get all non-king pieces currently on the board
        List<Piece> pieces = getPieces().stream()
                                        .filter( piece -> !( piece instanceof King ) )
                                        .toList();

        switch ( pieces.size() )
        {
            // King versus king
            case 0:
                return true;

            // King and knight/bishop versus king
            case 1:
                return pieces.get( 0 ) instanceof Knight ||
                       pieces.get( 0 ) instanceof Bishop;

            // King and bishop versus king and bishop with the bishops on the same colour
            case 2:
                if ( pieces.get( 0 ) instanceof Bishop &&
                     pieces.get( 1 ) instanceof Bishop )
                {
                    Bishop first = (Bishop) pieces.get( 0 );
                    Bishop second = (Bishop) pieces.get( 1 );

                    boolean differentPlayers = first.getColour() != second.getColour();
                    boolean sameSquareShade = first.getSquareShade() == second.getSquareShade();

                    return differentPlayers && sameSquareShade;
                }

                return false;

            default:
                return false;
        }
    }

    public void print()
    {
        Printer.print( this );
    }

    public static enum Side
    {
        QUEENSIDE, KINGSIDE
    }

    private static class Printer
    {
        /*
         * For each box-drawing character below, n is the number of line segments obtained by splitting the character at its
         * endpoints and junctions.
         * 
         * To convert a light box-drawing character to its heavy equivalent, add (1 << n) - 1, i.e. one less than two to the
         * power of n.
         */

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
                row.append( HORIZONTAL )
                   .append( HORIZONTAL )
                   .append( HORIZONTAL );

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
                {
                    Square square = board.getSquare( file, rank );

                    pieceRow.append( ' ' )
                            .append( square.isOccupied() ? square.getPiece().toChar() : ' ' )
                            .append( ' ' )
                            .append( VERTICAL );
                }

                System.out.println( pieceRow );

                if ( rank > '1' )
                    System.out.println( INNER_ROW );
            }

            System.out.println( LOWER_ROW );
        }
    }
}
