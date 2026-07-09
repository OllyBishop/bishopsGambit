package io.github.ollybishop.bishopsgambit.model.piece;

import java.util.ArrayList;
import java.util.List;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;

public class Pawn extends Piece
{
    public Pawn( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Type getType()
    {
        return Type.PAWN;
    }

    @Override
    public int getValue()
    {
        return 1;
    }

    @Override
    List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        List<Square> candidateSquares = new ArrayList<>();

        Square square = getSquare( board );
        int dy = getPlayerSign();

        Square oneRankForward = board.getSquare( square, 0, dy );

        if ( oneRankForward.isEmpty() )
        {
            candidateSquares.add( oneRankForward );

            Square twoRanksForward = board.getSquare( square, 0, 2 * dy );

            // twoRanksForward is always non-null when the pawn is on its start square
            if ( square == getStartSquare( board ) && twoRanksForward.isEmpty() )
                candidateSquares.add( twoRanksForward );
        }

        // Include controlled squares that are valid capture destinations
        List<Square> captureSquares = getControlledSquares( board ).stream()
                                                                   .filter( s -> isValidCaptureDestination( s, board ) )
                                                                   .toList();

        candidateSquares.addAll( captureSquares );

        return candidateSquares;
    }

    /**
     * Returns whether the given controlled square is a valid capture destination for this pawn.
     * <p>
     * A controlled square is a valid capture destination if it is occupied by an opposing piece, or if moving there would
     * capture the board's en passant pawn. This method does not check whether making the move would leave the moving player in
     * check.
     * 
     * @param controlledSquare the controlled destination square
     * @param board            the chessboard
     * @return {@code true} if the given controlled square is a valid capture destination; {@code false} otherwise
     */
    private boolean isValidCaptureDestination( Square controlledSquare, Board board )
    {
        if ( controlledSquare.isOccupiedByOpponentOf( getPlayer() ) )
            return true;

        Pawn enPassantPawn = board.getEnPassantPawn();

        if ( enPassantPawn == null )
            return false;

        char file = controlledSquare.getFile();
        char rank = getSquare( board ).getRank();

        return board.getSquare( file, rank ).getPiece() == enPassantPawn;
    }

    /**
     * For pawns, controlled squares are not simply candidate squares with friendly-occupied squares included. A pawn controls
     * its forward diagonals: two for non-edge pawns, or one for a-pawns and h-pawns.
     */
    @Override
    public List<Square> getControlledSquares( Board board )
    {
        List<Square> controlledSquares = new ArrayList<>();

        Square square = getSquare( board );
        int dy = getPlayerSign();

        for ( int dx : new int[] { -1, 1 } )
        {
            Square diagonal = board.getSquare( square, dx, dy );

            if ( diagonal != null )
                controlledSquares.add( diagonal );
        }

        return controlledSquares;
    }
}
