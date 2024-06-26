package main.java.game;

import java.util.ArrayList;
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
        Player currentPlayer = getCurrentPlayer();
        int n = currentPlayer.numberOfLegalMoves( board );
        System.out.printf( "%s has %d legal move%s.", currentPlayer, n, n == 1 ? "" : "s" );

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
    public Player getCurrentPlayer()
    {
        return numberOfTurnsTaken() % 2 == 0 ? getWhite() : getBlack();
    }

    /**
     * Returns the opponent of the player whose turn it currently is, based on the number of turns
     * taken.
     * 
     * @return Black if the number of turns taken is even; White if it is odd
     */
    public Player getCurrentOpponent()
    {
        return numberOfTurnsTaken() % 2 == 0 ? getBlack() : getWhite();
    }

    public Piece move( String fromStr, String toStr )
    {
        return move( fromStr, toStr, null );
    }

    public Piece move( String fromStr, String toStr, Typ promotionType )
    {
        Board board = getBoard();
        Square from = board.getSquare( fromStr );
        Square to = board.getSquare( toStr );
        return move( from, to, promotionType );
    }

    public Piece move( Square from, Square to, Typ promotionType )
    {
        if ( !from.isOccupied() )
            throw new UnoccupiedSquareException( from );

        Board board = getBoard();

        if ( !board.isLegalMove( from, to ) )
            throw new IllegalMoveException( from, to );

        // Disable en passant capture of opponent's pawns
        getCurrentOpponent().getPieces()
                            .stream()
                            .filter( pc -> pc instanceof Pawn )
                            .forEach( pc -> ((Pawn) pc).setEnPassant( false ) );

        Piece piece = from.getPiece();
        Board newBoard = board.move( from, to );

        Piece promotedPiece = null;

        if ( piece instanceof Pawn )
        {
            // Enable en passant capture of this pawn
            if ( piece.movedTwoSquaresForward( from, to ) )
            {
                ((Pawn) piece).setEnPassant( true );
            }

            // Pawn promotion
            else if ( promotionType != null )
            {
                char toFile = to.getFile();
                int toRank = to.getRank();

                switch ( promotionType )
                {
                    case KNIGHT:
                        promotedPiece = new Knight( getCurrentPlayer(), toFile, toRank );
                        break;

                    case BISHOP:
                        promotedPiece = new Bishop( getCurrentPlayer(), toFile, toRank );
                        break;

                    case ROOK:
                        promotedPiece = new Rook( getCurrentPlayer(), toFile, toRank );
                        break;

                    case QUEEN:
                        promotedPiece = new Queen( getCurrentPlayer(), toFile, toRank );
                        break;

                    default:
                        throw new RuntimeException( "Cannot promote to a piece of type '" + promotionType + "'." );
                }

                newBoard.getSquare( toFile, toRank ).setPiece( promotedPiece );
            }
        }

        for ( Piece pc : board.getPieces() )
        {
            if ( !newBoard.containsPiece( pc ) )
            {
                // If piece is no longer on the board, set it as captured
                pc.setCaptured( true );
            }
            else if ( pc.getSquare( newBoard ) != pc.getSquare( board ) )
            {
                // If piece is now on a different square, set it as moved
                pc.setMoved( true );
            }
        }

        addBoard( newBoard );

        return promotedPiece;
    }
}
