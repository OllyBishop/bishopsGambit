package test.java.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static test.java.Assertions.assertSameNotNull;
import static test.java.Assertions.assertThrowsWithMessage;

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
    private Game game;

    private void makeMove( String... ucis )
    {
        for ( String uci : ucis )
            game.makeMove( uci );
    }

    // ============================================================================================

    private Board getBoard()
    {
        return game.getBoard();
    }

    private Player getActivePlayer()
    {
        return game.getActivePlayer();
    }

    private Square getSquare( String coords )
    {
        return getBoard().getSquare( coords );
    }

    private Piece getPiece( String coords )
    {
        return getSquare( coords ).getPiece();
    }

    // ============================================================================================

    private int numberOfMovesMade()
    {
        return game.getNumberOfMovesMade();
    }

    private int numberOfLegalMoves()
    {
        return getActivePlayer().getNumberOfLegalMoves( getBoard() );
    }

    private int materialDifference()
    {
        return getBoard().getMaterialDifference();
    }

    private boolean inCheck()
    {
        return getActivePlayer().isInCheck( getBoard() );
    }

    private boolean inCheckmate()
    {
        return getActivePlayer().isInCheckmate( getBoard() );
    }

    private boolean inStalemate()
    {
        return getActivePlayer().isInStalemate( getBoard() );
    }

    private boolean insufficientMaterial()
    {
        return getBoard().hasInsufficientMaterial();
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
        assertThrowsWithMessage( UnoccupiedSquareException.class,
                                 () -> makeMove( "e3e4" ),
                                 "Cannot make a move from the unoccupied square e3." );
    }

    @Test
    void illegalMove()
    {
        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1e2" ),
                                 "The White King occupying e1 cannot legally move to e2." );
    }

    @Test
    void foolsMate()
    {
        makeMove( "f2f3", "e7e5" );
        makeMove( "g2g4", "d8h4" );

        assertEquals( numberOfMovesMade(), 4 );
        assertEquals( numberOfLegalMoves(), 0 );
        assertEquals( materialDifference(), 0 );

        assertTrue( inCheck() );
        assertTrue( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );
    }

    @Test
    void scholarsMate()
    {
        makeMove( "e2e4", "e7e5" );
        makeMove( "d1h5", "b8c6" );
        makeMove( "f1c4", "g8f6" );
        makeMove( "h5f7" );

        assertEquals( numberOfMovesMade(), 7 );
        assertEquals( numberOfLegalMoves(), 0 );
        assertEquals( materialDifference(), 1 );

        assertTrue( inCheck() );
        assertTrue( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );
    }

    @Test
    void kingsideCastling()
    {
        Piece whiteKing = getPiece( "e1" );
        Piece blackKing = getPiece( "e8" );
        Piece whiteKingsideRook = getPiece( "h1" );
        Piece blackKingsideRook = getPiece( "h8" );

        makeMove( "e2e4", "e7e5" );
        makeMove( "g1f3", "g8f6" );
        makeMove( "f1c4", "f8c5" );
        makeMove( "e1g1", "e8g8" );

        assertEquals( numberOfMovesMade(), 8 );
        assertEquals( numberOfLegalMoves(), 30 );
        assertEquals( materialDifference(), 0 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );

        assertSameNotNull( whiteKing, getPiece( "g1" ) );
        assertSameNotNull( blackKing, getPiece( "g8" ) );
        assertSameNotNull( whiteKingsideRook, getPiece( "f1" ) );
        assertSameNotNull( blackKingsideRook, getPiece( "f8" ) );
    }

    @Test
    void queensideCastling()
    {
        Piece whiteKing = getPiece( "e1" );
        Piece blackKing = getPiece( "e8" );
        Piece whiteQueensideRook = getPiece( "a1" );
        Piece blackQueensideRook = getPiece( "a8" );

        makeMove( "d2d4", "d7d5" );
        makeMove( "b1c3", "b8c6" );
        makeMove( "c1f4", "c8f5" );
        makeMove( "d1d2", "d8d7" );
        makeMove( "e1c1", "e8c8" );

        assertEquals( numberOfMovesMade(), 10 );
        assertEquals( numberOfLegalMoves(), 30 );
        assertEquals( materialDifference(), 0 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );

        assertSameNotNull( whiteKing, getPiece( "c1" ) );
        assertSameNotNull( blackKing, getPiece( "c8" ) );
        assertSameNotNull( whiteQueensideRook, getPiece( "d1" ) );
        assertSameNotNull( blackQueensideRook, getPiece( "d8" ) );
    }

    @Test
    void enPassantIllegalMove()
    {
        makeMove( "d2d4", "e7e6" );
        makeMove( "d4d5", "e6e5" );

        assertEquals( numberOfMovesMade(), 4 );
        assertEquals( numberOfLegalMoves(), 29 );
        assertEquals( materialDifference(), 0 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "d5e6" ),
                                 "The White Pawn occupying d5 cannot legally move to e6." );
    }

    @Test
    void enPassantOnlyLegalMoveToEscapeCheck()
    {
        makeMove( "e2e4", "e7e6" );
        makeMove( "e4e5", "d8h4" );
        makeMove( "e1e2", "b8c6" );
        makeMove( "e2e3", "h4g3" );
        makeMove( "e3e4", "d7d5" );

        assertEquals( numberOfMovesMade(), 10 );
        assertEquals( numberOfLegalMoves(), 1 );
        assertEquals( materialDifference(), 0 );

        assertTrue( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );

        assertDoesNotThrow( () -> makeMove( "e5d6" ) );
    }

    @Test
    void enemyKnightBlocksKingsideCastling()
    {
        makeMove( "e2e4", "g8f6" );
        makeMove( "g1f3", "f6g4" );
        makeMove( "f1c4", "g4e3" );
        makeMove( "d2d3", "e3f1" );

        assertEquals( numberOfMovesMade(), 8 );
        assertEquals( numberOfLegalMoves(), 37 );
        assertEquals( materialDifference(), 0 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "The White King occupying e1 cannot legally move to g1." );
    }

    @Test
    void enemyBishopBlocksKingsideCastling()
    {
        makeMove( "e2e4", "b7b6" );
        makeMove( "g1f3", "c8a6" );
        makeMove( "d2d4", "a6f1" );

        assertEquals( numberOfMovesMade(), 6 );
        assertEquals( numberOfLegalMoves(), 32 );
        assertEquals( materialDifference(), -3 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertFalse( insufficientMaterial() );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "The White King occupying e1 cannot legally move to g1." );
    }

    @Test
    void shortestStalemate()
    {
        makeMove( "e2e3", "a7a5" );
        makeMove( "d1h5", "a8a6" );
        makeMove( "h5a5", "h7h5" );
        makeMove( "h2h4", "a6h6" );
        makeMove( "a5c7", "f7f6" );
        makeMove( "c7d7", "e8f7" );
        makeMove( "d7b7", "d8d3" );
        makeMove( "b7b8", "d3h7" );
        makeMove( "b8c8", "f7g6" );
        makeMove( "c8e6" );

        assertEquals( numberOfMovesMade(), 19 );
        assertEquals( numberOfLegalMoves(), 0 );
        assertEquals( materialDifference(), 10 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertTrue( inStalemate() );
        assertFalse( insufficientMaterial() );
    }

    @Test
    void insufficientMaterialKingVersusKing()
    {
        makeMove( "e2e4", "d7d5" );
        makeMove( "e4d5", "d8d5" );
        makeMove( "f1d3", "d5a2" );
        makeMove( "d3h7", "a2b1" );
        makeMove( "h7g8", "b1c2" );
        makeMove( "g8f7", "e8f7" );
        makeMove( "a1a7", "c2c1" );
        makeMove( "a7b7", "h8h2" );
        makeMove( "b7b8", "h2g2" );
        makeMove( "d1c1", "g2g1" );
        makeMove( "h1g1", "a8b8" );
        makeMove( "c1c7", "b8b2" );
        makeMove( "c7c8", "b2d2" );
        makeMove( "c8f8", "f7f8" );
        makeMove( "g1g7", "d2f2" );
        makeMove( "g7e7", "f8e7" );
        makeMove( "e1f2" );

        assertEquals( numberOfMovesMade(), 33 );
        assertEquals( numberOfLegalMoves(), 8 );
        assertEquals( materialDifference(), 0 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertTrue( insufficientMaterial() );
    }

    @Test
    void insufficientMaterialKingAndBishopVersusKingAndBishopWithBishopsOnSameColour()
    {
        makeMove( "e2e4", "d7d5" );
        makeMove( "e4d5", "d8d5" );
        makeMove( "f1d3", "d5a2" );
        makeMove( "d3h7", "a2b1" );
        makeMove( "h7g8", "b1c2" );
        makeMove( "g8f7", "e8f7" );
        makeMove( "a1a7", "h8h2" );
        makeMove( "a7b7", "h2g2" );
        makeMove( "b7b8", "g2g1" );
        makeMove( "h1g1", "a8b8" );
        makeMove( "d1c2", "b8b2" );
        makeMove( "c2c7", "b2d2" );
        makeMove( "c7c8", "d2f2" );
        makeMove( "g1g7", "f7g7" );
        makeMove( "e1f2", "g7f6" );
        makeMove( "c8d7", "f6e5" );
        makeMove( "d7e7", "f8e7" );

        assertEquals( numberOfMovesMade(), 34 );
        assertEquals( numberOfLegalMoves(), 15 );
        assertEquals( materialDifference(), 0 );

        assertFalse( inCheck() );
        assertFalse( inCheckmate() );
        assertFalse( inStalemate() );
        assertTrue( insufficientMaterial() );
    }
}
