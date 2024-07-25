package test.java.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import main.java.board.Board;
import main.java.board.Square;
import main.java.game.Game;
import main.java.game.IllegalMoveException;
import main.java.game.UnoccupiedSquareException;
import main.java.pieces.Piece;
import main.java.player.Player;

class GameTest
{
    Game game;

    // ============================================================================================

    void move( String str )
    {
        game.move( str );
    }

    void move( String str1, String str2 )
    {
        move( str1 );
        move( str2 );
    }

    // ============================================================================================

    Player getWhite()
    {
        return game.getWhite();
    }

    Player getBlack()
    {
        return game.getBlack();
    }

    Board getBoard()
    {
        return game.getBoard();
    }

    Square getSquare( String squareStr )
    {
        return getBoard().getSquare( squareStr );
    }

    // ============================================================================================

    boolean pieceIsOccupying( Piece piece, String coords )
    {
        return piece == getSquare( coords ).getPiece();
    }

    void assertPieceIsOccupying( Piece piece, String coords )
    {
        assertTrue( pieceIsOccupying( piece, coords ) );
    }

    void assertPieceIsNotOccupying( Piece piece, String coords )
    {
        assertFalse( pieceIsOccupying( piece, coords ) );
    }

    // ============================================================================================

    boolean pieceCanMoveTo( Piece piece, String coords )
    {
        return piece.getMoves( getBoard() ).contains( getSquare( coords ) );
    }

    void assertPieceCanMoveTo( Piece piece, String coords )
    {
        assertTrue( pieceCanMoveTo( piece, coords ) );
    }

    void assertPieceCannotMoveTo( Piece piece, String coords )
    {
        assertFalse( pieceCanMoveTo( piece, coords ) );
    }

    // ============================================================================================

    void assertNumberOfTurnsTaken( int n )
    {
        assertEquals( game.numberOfTurnsTaken(), n );
    }

    void assertNumberOfLegalMoves( int n )
    {
        assertEquals( game.getActivePlayer().numberOfLegalMoves( getBoard() ), n );
    }

    // ============================================================================================

    void assertCheckmate()
    {
        assertTrue( game.getActivePlayer().isInCheckmate( getBoard() ) );
    }

    void assertStalemate()
    {
        assertTrue( game.getActivePlayer().isInStalemate( getBoard() ) );
    }

    // ============================================================================================

    @BeforeEach
    void printName( TestInfo testInfo )
    {
        System.out.println( "Running " + testInfo.getDisplayName() );
    }

    @BeforeEach
    void newGame()
    {
        game = new Game();
    }

    @AfterEach
    void printBoard()
    {
        getBoard().print();
    }

    // ============================================================================================

    @Test
    void unoccupiedSquare()
    {
        try
        {
            move( "e3e4" );
        }
        catch ( UnoccupiedSquareException e )
        {
            System.out.println( e.getMessage() );
            return;
        }

        fail();
    }

    @Test
    void illegalMove()
    {
        try
        {
            move( "e1e2" );
        }
        catch ( IllegalMoveException e )
        {
            System.out.println( e.getMessage() );
            return;
        }

        fail();
    }

    @Test
    void foolsMate()
    {
        move( "f2f3", "e7e5" );
        move( "g2g4", "d8h4" );

        assertNumberOfTurnsTaken( 4 );

        assertCheckmate();
    }

    @Test
    void scholarsMate()
    {
        move( "e2e4", "e7e5" );
        move( "d1h5", "b8c6" );
        move( "f1c4", "g8f6" );
        move( "h5f7" );

        assertNumberOfTurnsTaken( 7 );

        assertCheckmate();
    }

    @Test
    void kingsideCastling()
    {
        move( "e2e4", "e7e5" );
        move( "g1f3", "g8f6" );
        move( "f1c4", "f8c5" );
        move( "e1g1", "e8g8" );

        assertNumberOfTurnsTaken( 8 );

        assertPieceIsOccupying( getWhite().getKing(), "g1" );
        assertPieceIsOccupying( getWhite().getKingsideRook(), "f1" );

        assertPieceIsOccupying( getBlack().getKing(), "g8" );
        assertPieceIsOccupying( getBlack().getKingsideRook(), "f8" );
    }

    @Test
    void queensideCastling()
    {
        move( "d2d4", "d7d5" );
        move( "b1c3", "b8c6" );
        move( "c1f4", "c8f5" );
        move( "d1d2", "d8d7" );
        move( "e1c1", "e8c8" );

        assertNumberOfTurnsTaken( 10 );

        assertPieceIsOccupying( getWhite().getKing(), "c1" );
        assertPieceIsOccupying( getWhite().getQueensideRook(), "d1" );

        assertPieceIsOccupying( getBlack().getKing(), "c8" );
        assertPieceIsOccupying( getBlack().getQueensideRook(), "d8" );
    }

    @Test
    void enPassantOnlyLegalMove()
    {
        move( "e2e4", "e7e6" );
        move( "e4e5", "d8h4" );
        move( "e1e2", "b8c6" );
        move( "e2e3", "h4g3" );
        move( "e3e4", "d7d5" );

        assertNumberOfTurnsTaken( 10 );

        assertNumberOfLegalMoves( 1 );
    }

    @Test
    void enemyKnightBlocksKingsideCastling()
    {
        move( "e2e4", "g8f6" );
        move( "g1f3", "f6g4" );
        move( "f1c4", "g4e3" );
        move( "d2d3", "e3f1" );

        assertNumberOfTurnsTaken( 8 );

        assertPieceCannotMoveTo( getWhite().getKing(), "g1" );
    }

    @Test
    void enemyBishopBlocksKingsideCastling()
    {
        move( "e2e4", "b7b6" );
        move( "g1f3", "c8a6" );
        move( "d2d4", "a6f1" );

        assertNumberOfTurnsTaken( 6 );

        assertPieceCannotMoveTo( getWhite().getKing(), "g1" );
    }
}
