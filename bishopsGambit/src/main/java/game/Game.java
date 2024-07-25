package main.java.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.board.Board;
import main.java.board.Square;
import main.java.pieces.Bishop;
import main.java.pieces.Knight;
import main.java.pieces.Pawn;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.pieces.Queen;
import main.java.pieces.Rook;
import main.java.player.Player;
import main.java.player.Player.Colour;

public class Game
{
    private final List<Board> boards = new ArrayList<>();

    private final Player white = new Player( Colour.WHITE );
    private final Player black = new Player( Colour.BLACK );

    public Game()
    {
        Board board = new Board();
        board.assignPieces( getWhite() );
        board.assignPieces( getBlack() );
        addBoard( board );
    }

    private List<Board> getBoards()
    {
        return this.boards;
    }

    public Player getWhite()
    {
        return this.white;
    }

    public Player getBlack()
    {
        return this.black;
    }

    private void addBoard( Board board )
    {
        getBoards().add( board );
        printInfo( board );
    }

    private void printInfo( Board board )
    {
        Player activePlayer = getActivePlayer();
        int n = activePlayer.numberOfLegalMoves( board );

        if ( n == 1 )
            System.out.printf( "%s has 1 legal move.", activePlayer );
        else
            System.out.printf( "%s has %d legal moves.", activePlayer, n );

        int diff = board.getMaterialDiff();

        if ( diff != 0 )
            System.out.printf( " %s: +%d", diff > 0 ? getWhite() : getBlack(), Math.abs( diff ) );

        System.out.println();
    }

    public int numberOfTurnsTaken()
    {
        return getBoards().size() - 1;
    }

    public Board getBoard()
    {
        return getBoards().get( numberOfTurnsTaken() );
    }

    /**
     * Returns the player whose turn it currently is, based on the number of turns taken.
     * 
     * @return White if the number of turns taken is even; Black if it is odd
     */
    public Player getActivePlayer()
    {
        return numberOfTurnsTaken() % 2 == 0 ? getWhite() : getBlack();
    }

    /**
     * Returns the opponent of the player whose turn it currently is, based on the number of turns
     * taken.
     * 
     * @return Black if the number of turns taken is even; White if it is odd
     */
    public Player getInactivePlayer()
    {
        return numberOfTurnsTaken() % 2 == 0 ? getBlack() : getWhite();
    }

    private String[] validateUCI( String uci )
    {
        if ( uci == null )
            throw new IllegalArgumentException( "String cannot be null." );

        if ( uci.length() < 4 || uci.length() > 5 )
            throw new IllegalArgumentException( "String must be 4 or 5 characters in length." );

        String value1 = uci.substring( 0, 2 );
        String value2 = uci.substring( 2, 4 );
        String value3 = uci.substring( 4 );

        if ( !Board.isValidSquare( value1 ) )
            throw new IllegalArgumentException( "First pair of characters '" + value1 + "' must represent a valid square." );

        if ( !Board.isValidSquare( value2 ) )
            throw new IllegalArgumentException( "Second pair of characters '" + value2 + "' must represent a valid square." );

        if ( !Arrays.asList( "", "n", "b", "r", "q" ).contains( value3 ) )
            throw new IllegalArgumentException( "Fifth character '" + value3 + "' must be empty or represent a valid piece." );

        return new String[] { value1, value2, value3 };
    }

    /**
     * @param uci a string representing the move in Universal Chess Interface (UCI) notation
     * @return the promoted piece (if applicable); otherwise {@code null}
     */
    public Piece move( String uci )
    {
        String[] values = validateUCI( uci );

        Board board = getBoard();

        Square from = board.getSquare( values[ 0 ] );
        Square to = board.getSquare( values[ 1 ] );

        Typ promType = switch ( values[ 2 ] )
        {
            case "n" -> Typ.KNIGHT;
            case "b" -> Typ.BISHOP;
            case "r" -> Typ.ROOK;
            case "q" -> Typ.QUEEN;

            default -> null;
        };

        return move( from, to, promType );
    }

    public Piece move( Square from, Square to, Typ promType )
    {
        if ( !from.isOccupied() )
            throw new UnoccupiedSquareException( from );

        Board board = getBoard();

        if ( !board.isLegalMove( from, to ) )
            throw new IllegalMoveException( from, to );

        // If an opponent's pawn was capturable en passant, revoke this on the player's next turn
        getInactivePlayer().getPieces()
                           .stream()
                           .filter( pc -> pc instanceof Pawn )
                           .forEach( pc -> ((Pawn) pc).setCapturableEnPassant( false ) );

        Piece piece = from.getPiece();
        Board newBoard = board.move( from, to );

        Piece promPiece = null;

        if ( piece instanceof Pawn )
        {
            // Allow this pawn to be captured en passant on the opponent's next turn
            if ( piece.movedTwoSquaresForward( from, to ) )
            {
                ((Pawn) piece).setCapturableEnPassant( true );
            }

            // Pawn promotion
            else if ( promType != null )
            {
                char toFile = to.getFile();
                char toRank = to.getRank();

                promPiece = switch ( promType )
                {
                    case KNIGHT -> new Knight( getActivePlayer(), toFile, toRank );
                    case BISHOP -> new Bishop( getActivePlayer(), toFile, toRank );
                    case ROOK -> new Rook( getActivePlayer(), toFile, toRank );
                    case QUEEN -> new Queen( getActivePlayer(), toFile, toRank );

                    default -> throw new InvalidPromotionException( promType );
                };

                newBoard.getSquare( toFile, toRank ).setPiece( promPiece );
            }
        }

        for ( Piece pc : board.getPieces() )
        {
            // If piece is no longer on the board, set it as captured
            if ( !newBoard.containsPiece( pc ) )
                pc.setCaptured( true );

            // If piece is now on a different square, set it as moved
            else if ( pc.getSquare( newBoard ) != pc.getSquare( board ) )
                pc.setMoved( true );
        }

        addBoard( newBoard );

        return promPiece;
    }
}
