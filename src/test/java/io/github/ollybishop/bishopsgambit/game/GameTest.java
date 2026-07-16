package io.github.ollybishop.bishopsgambit.game;

import static io.github.ollybishop.bishopsgambit.testutil.TestAssertions.assertSameNotNull;
import static io.github.ollybishop.bishopsgambit.testutil.TestAssertions.assertThrowsWithMessage;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.model.piece.Pawn;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;
import io.github.ollybishop.bishopsgambit.testutil.SquareHelper;

class GameTest
{
    private Game game;

    // ============================================================================================

    private final SquareHelper a1 = new SquareHelper( () -> game, 'a', '1' );
    private final SquareHelper a4 = new SquareHelper( () -> game, 'a', '4' );
    private final SquareHelper a5 = new SquareHelper( () -> game, 'a', '5' );
    private final SquareHelper a8 = new SquareHelper( () -> game, 'a', '8' );

    private final SquareHelper b7 = new SquareHelper( () -> game, 'b', '7' );

    private final SquareHelper c1 = new SquareHelper( () -> game, 'c', '1' );
    private final SquareHelper c4 = new SquareHelper( () -> game, 'c', '4' );
    private final SquareHelper c5 = new SquareHelper( () -> game, 'c', '5' );
    private final SquareHelper c8 = new SquareHelper( () -> game, 'c', '8' );

    private final SquareHelper d1 = new SquareHelper( () -> game, 'd', '1' );
    private final SquareHelper d4 = new SquareHelper( () -> game, 'd', '4' );
    private final SquareHelper d5 = new SquareHelper( () -> game, 'd', '5' );
    private final SquareHelper d8 = new SquareHelper( () -> game, 'd', '8' );

    private final SquareHelper e1 = new SquareHelper( () -> game, 'e', '1' );
    private final SquareHelper e4 = new SquareHelper( () -> game, 'e', '4' );
    private final SquareHelper e5 = new SquareHelper( () -> game, 'e', '5' );
    private final SquareHelper e7 = new SquareHelper( () -> game, 'e', '7' );
    private final SquareHelper e8 = new SquareHelper( () -> game, 'e', '8' );

    private final SquareHelper f1 = new SquareHelper( () -> game, 'f', '1' );
    private final SquareHelper f5 = new SquareHelper( () -> game, 'f', '5' );
    private final SquareHelper f8 = new SquareHelper( () -> game, 'f', '8' );

    private final SquareHelper g1 = new SquareHelper( () -> game, 'g', '1' );
    private final SquareHelper g4 = new SquareHelper( () -> game, 'g', '4' );
    private final SquareHelper g8 = new SquareHelper( () -> game, 'g', '8' );

    private final SquareHelper h1 = new SquareHelper( () -> game, 'h', '1' );
    private final SquareHelper h4 = new SquareHelper( () -> game, 'h', '4' );
    private final SquareHelper h5 = new SquareHelper( () -> game, 'h', '5' );
    private final SquareHelper h7 = new SquareHelper( () -> game, 'h', '7' );
    private final SquareHelper h8 = new SquareHelper( () -> game, 'h', '8' );

    // ============================================================================================

    private void makeMove( String uci )
    {
        game.makeMove( uci );
    }

    private void makeMove( String uci1, String uci2 )
    {
        makeMove( uci1 );
        makeMove( uci2 );
    }

    private void makeMove( SquareHelper from, SquareHelper to, Piece.Type newType )
    {
        game.makeMove( from.getSquare(), to.getSquare(), newType );
    }

    // ============================================================================================

    private int getNumberOfPliesPlayed()
    {
        return game.getNumberOfPliesPlayed();
    }

    private int getNumberOfLegalMoves()
    {
        return game.getActivePlayer().getNumberOfLegalMoves( game.getActiveBoard() );
    }

    private int getMaterialDifference()
    {
        return game.getActiveBoard().getMaterialDifference();
    }

    // ============================================================================================

    private boolean isActivePlayerInCheck()
    {
        return game.getActivePlayer().isInCheck( game.getActiveBoard() );
    }

    private boolean isActivePlayerCheckmated()
    {
        return game.getActivePlayer().isCheckmated( game.getActiveBoard() );
    }

    private boolean isActivePlayerStalemated()
    {
        return game.getActivePlayer().isStalemated( game.getActiveBoard() );
    }

    private boolean boardHasInsufficientMaterial()
    {
        return game.getActiveBoard().hasInsufficientMaterial();
    }

    private Game.Status getGameStatus()
    {
        return game.getStatus();
    }

    // ============================================================================================

    private boolean isWhiteQueensideCastlingAllowed()
    {
        return game.getActiveBoard().isWhiteQueensideCastlingAllowed();
    }

    private boolean isWhiteKingsideCastlingAllowed()
    {
        return game.getActiveBoard().isWhiteKingsideCastlingAllowed();
    }

    private boolean isBlackQueensideCastlingAllowed()
    {
        return game.getActiveBoard().isBlackQueensideCastlingAllowed();
    }

    private boolean isBlackKingsideCastlingAllowed()
    {
        return game.getActiveBoard().isBlackKingsideCastlingAllowed();
    }

    // ============================================================================================

    private void assertAllEnPassantPawns( int[] plies, SquareHelper[] expectedSquares )
    {
        if ( plies.length != expectedSquares.length )
            throw new IllegalArgumentException( "Arrays must have the same length." );

        // Indexed by ply number: { 0, 1, ..., game.getNumberOfPliesPlayed() }
        SquareHelper[] expectedSquaresByPly = new SquareHelper[ game.getNumberOfPliesPlayed() + 1 ];

        for ( int i = 0; i < plies.length; i++ )
        {
            int ply = plies[ i ];

            if ( ply < 0 || ply >= expectedSquaresByPly.length )
                throw new IllegalArgumentException( "Ply out of range: " + ply );

            expectedSquaresByPly[ ply ] = expectedSquares[ i ];
        }

        for ( int ply = 0; ply < expectedSquaresByPly.length; ply++ )
        {
            assertEnPassantPawn( ply, expectedSquaresByPly[ ply ] );
        }
    }

    private void assertEnPassantPawn( int ply, SquareHelper expectedSquare )
    {
        Board board = game.getBoard( ply );
        Pawn enPassantPawn = board.getEnPassantPawn();

        if ( expectedSquare == null )
        {
            assertNull( enPassantPawn );
        }
        else
        {
            Square square = board.getSquare( expectedSquare.getFile(), expectedSquare.getRank() );
            assertSame( square.getPiece(), enPassantPawn );
        }
    }

    // ============================================================================================

    @BeforeEach
    void printTestName( TestInfo testInfo )
    {
        System.out.println( "Running test " + testInfo.getDisplayName() + "..." );
    }

    @BeforeEach
    void createNewGame()
    {
        game = new Game();
    }

    @AfterEach
    void printFinalBoard()
    {
        game.getActiveBoard().print();
        System.out.println();
    }

    // ============================================================================================
    // Invalid Moves
    // ============================================================================================

    @Test
    void movingFromEmptySquare_throwsNoPieceOnSquareException()
    {
        assertThrowsWithMessage( NoPieceOnSquareException.class,
                                 () -> makeMove( "e3e4" ),
                                 "Cannot move a piece from empty square e3." );
    }

    @Test
    void whiteToMove_movingBlackPiece_throwsIllegalMoveException()
    {
        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e7e5" ),
                                 "Black Pawn cannot be moved because it is White's turn." );
    }

    @Test
    void blackToMove_movingWhitePiece_throwsIllegalMoveException()
    {
        makeMove( "e2e4" );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "d2d4" ),
                                 "White Pawn cannot be moved because it is Black's turn." );
    }

    @Test
    void movingToSquareOccupiedByFriendlyPiece_throwsIllegalMoveException()
    {
        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1e2" ),
                                 "White King cannot legally move from e1 to e2." );
    }

    // ============================================================================================
    // Checkmate
    // ============================================================================================

    @Test
    void checkmate_foolsMate()
    {
        makeMove( "f2f3", "e7e5" );
        makeMove( "g2g4", "d8h4" );

        assertEquals( 4, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertTrue( isActivePlayerInCheck() );
        assertTrue( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.CHECKMATE );

        assertAllEnPassantPawns( new int[] { 2, 3 }, new SquareHelper[] { e5, g4 } );
    }

    @Test
    void checkmate_scholarsMate()
    {
        makeMove( "e2e4", "e7e5" );
        makeMove( "d1h5", "b8c6" );
        makeMove( "f1c4", "g8f6" );
        makeMove( "h5f7" );

        assertEquals( 7, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 1, getMaterialDifference() );

        assertTrue( isActivePlayerInCheck() );
        assertTrue( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.CHECKMATE );

        assertAllEnPassantPawns( new int[] { 1, 2 }, new SquareHelper[] { e4, e5 } );
    }

    // ============================================================================================
    // Castling
    // ============================================================================================

    @Test
    void kingsideCastling()
    {
        Piece whiteKing = e1.getPiece();
        Piece blackKing = e8.getPiece();
        Piece whiteKingsideRook = h1.getPiece();
        Piece blackKingsideRook = h8.getPiece();

        makeMove( "e2e4", "e7e5" );
        makeMove( "g1f3", "g8f6" );
        makeMove( "f1c4", "f8c5" );
        makeMove( "e1g1", "e8g8" );

        assertEquals( 8, getNumberOfPliesPlayed() );
        assertEquals( 30, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.DEFAULT );

        assertSameNotNull( whiteKing, g1.getPiece() );
        assertSameNotNull( blackKing, g8.getPiece() );
        assertSameNotNull( whiteKingsideRook, f1.getPiece() );
        assertSameNotNull( blackKingsideRook, f8.getPiece() );

        assertAllEnPassantPawns( new int[] { 1, 2 }, new SquareHelper[] { e4, e5 } );
    }

    @Test
    void queensideCastling()
    {
        Piece whiteKing = e1.getPiece();
        Piece blackKing = e8.getPiece();
        Piece whiteQueensideRook = a1.getPiece();
        Piece blackQueensideRook = a8.getPiece();

        makeMove( "d2d4", "d7d5" );
        makeMove( "b1c3", "b8c6" );
        makeMove( "c1f4", "c8f5" );
        makeMove( "d1d2", "d8d7" );
        makeMove( "e1c1", "e8c8" );

        assertEquals( 10, getNumberOfPliesPlayed() );
        assertEquals( 30, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.DEFAULT );

        assertSameNotNull( whiteKing, c1.getPiece() );
        assertSameNotNull( blackKing, c8.getPiece() );
        assertSameNotNull( whiteQueensideRook, d1.getPiece() );
        assertSameNotNull( blackQueensideRook, d8.getPiece() );

        assertAllEnPassantPawns( new int[] { 1, 2 }, new SquareHelper[] { d4, d5 } );
    }

    @Test
    void kingsideCastling_fSquareOccupied_byEnemyKnight_throwsIllegalMoveException()
    {
        makeMove( "e2e4", "g8f6" );
        makeMove( "g1f3", "f6g4" );
        makeMove( "f1c4", "g4e3" );
        makeMove( "d2d3", "e3f1" );

        Piece blackKingsideKnight = f1.getPiece();
        assertEquals( blackKingsideKnight.getColour(), Player.Colour.BLACK );
        assertEquals( blackKingsideKnight.getType(), Piece.Type.KNIGHT );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "White King cannot legally move from e1 to g1." );

        assertEquals( 8, getNumberOfPliesPlayed() );
        assertEquals( 37, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.DEFAULT );

        assertAllEnPassantPawns( new int[] { 1 }, new SquareHelper[] { e4 } );
    }

    @Test
    void kingsideCastling_fSquareOccupied_byEnemyBishop_throwsIllegalMoveException()
    {
        makeMove( "e2e4", "b7b6" );
        makeMove( "g1f3", "c8a6" );
        makeMove( "d2d4", "a6f1" );

        Piece blackQueensideBishop = f1.getPiece();
        assertEquals( blackQueensideBishop.getColour(), Player.Colour.BLACK );
        assertEquals( blackQueensideBishop.getType(), Piece.Type.BISHOP );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "White King cannot legally move from e1 to g1." );

        assertEquals( 6, getNumberOfPliesPlayed() );
        assertEquals( 32, getNumberOfLegalMoves() );
        assertEquals( -3, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.DEFAULT );

        assertAllEnPassantPawns( new int[] { 1, 5 }, new SquareHelper[] { e4, d4 } );
    }

    @Test
    void kingsideCastling_fSquareControlled_byEnemyPawn_throwsIllegalMoveException()
    {
        makeMove( "d2d4", "g8f6" );
        makeMove( "d4d5", "e7e5" );
        makeMove( "d5e6", "f8c5" );
        makeMove( "e6e7" );

        Piece whiteDPawn = e7.getPiece();
        assertEquals( whiteDPawn.getColour(), Player.Colour.WHITE );
        assertEquals( whiteDPawn.getType(), Piece.Type.PAWN );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e8g8" ),
                                 "Black King cannot legally move from e8 to g8." );

        assertAllEnPassantPawns( new int[] { 1, 4 }, new SquareHelper[] { d4, e5 } );
    }

    @Test
    void kingsideCastling_gSquareControlled_byEnemyPawn_throwsIllegalMoveException()
    {
        makeMove( "g2g4", "g8f6" );
        makeMove( "g4g5", "e7e5" );
        makeMove( "g5g6", "f8c5" );
        makeMove( "g6h7" );

        Piece whiteGPawn = h7.getPiece();
        assertEquals( whiteGPawn.getColour(), Player.Colour.WHITE );
        assertEquals( whiteGPawn.getType(), Piece.Type.PAWN );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e8g8" ),
                                 "Black King cannot legally move from e8 to g8." );

        assertAllEnPassantPawns( new int[] { 1, 4 }, new SquareHelper[] { g4, e5 } );
    }

    // ============================================================================================
    // En Passant
    // ============================================================================================

    @Test
    void enPassantCaptureAfterSingleSquarePawnMove_throwsIllegalMoveException()
    {
        makeMove( "d2d4", "e7e6" );
        makeMove( "d4d5", "e6e5" );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "d5e6" ),
                                 "White Pawn cannot legally move from d5 to e6." );

        assertEquals( 4, getNumberOfPliesPlayed() );
        assertEquals( 29, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.DEFAULT );

        assertAllEnPassantPawns( new int[] { 1 }, new SquareHelper[] { d4 } );
    }

    @Test
    void enPassantCaptureOfCheckingPawn_isOnlyLegalMove()
    {
        makeMove( "e2e4", "e7e6" );
        makeMove( "e4e5", "d8h4" );
        makeMove( "e1e2", "b8c6" );
        makeMove( "e2e3", "h4g3" );
        makeMove( "e3e4", "d7d5" );

        assertEquals( 10, getNumberOfPliesPlayed() );
        assertEquals( 1, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertTrue( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.CHECK );

        assertDoesNotThrow( () -> makeMove( "e5d6" ) );

        assertAllEnPassantPawns( new int[] { 1, 10 }, new SquareHelper[] { e4, d5 } );
    }

    // ============================================================================================
    // Stalemate
    // ============================================================================================

    @Test
    void stalemate_nineteenPlies()
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

        assertEquals( 19, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 10, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertTrue( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.STALEMATE );

        assertAllEnPassantPawns( new int[] { 2, 6, 7 }, new SquareHelper[] { a5, h5, h4 } );
    }

    @Test
    void stalemate_twentyFourPlies_noCaptures()
    {
        makeMove( "d2d4", "e7e5" ); // 1. d4, d5
        makeMove( "d1d2", "e5e4" ); // 2. Qd2, e4
        makeMove( "a2a4", "a7a5" ); // 3. a4, a5
        makeMove( "d2f4", "f7f5" ); // 4. Qf4, f5
        makeMove( "h2h3", "d8h4" ); // 5. h3, Qh4
        makeMove( "f4h2", "f8b4" ); // 6. Qh2, Bb4+
        makeMove( "b1d2", "d7d6" ); // 7. Nd2, d6
        makeMove( "a1a3", "c8e6" ); // 8. Ra3, Be6
        makeMove( "a3g3", "e6b3" ); // 9. Rg3, Bb3
        makeMove( "c2c4", "c7c5" ); // 10. c4, c5
        makeMove( "f2f3", "f5f4" ); // 11. f3, f4
        makeMove( "d4d5", "e4e3" ); // 12. d5, e3

        assertEquals( 24, getNumberOfPliesPlayed() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertTrue( isActivePlayerStalemated() );
        assertFalse( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.STALEMATE );

        assertAllEnPassantPawns( new int[] { 1, 2, 5, 6, 8, 19, 20 }, new SquareHelper[] { d4, e5, a4, a5, f5, c4, c5 } );
    }

    // ============================================================================================
    // Insufficient Material
    // ============================================================================================

    @Test
    void insufficientMaterial_kingVersusKing()
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

        assertEquals( 33, getNumberOfPliesPlayed() );
        assertEquals( 8, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertTrue( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.INSUFFICIENT_MATERIAL );

        assertAllEnPassantPawns( new int[] { 1, 2 }, new SquareHelper[] { e4, d5 } );
    }

    @Test
    void insufficientMaterial_kingAndKnightVersusKing()
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
        makeMove( "d1c1", "a8b8" );
        makeMove( "c1c7", "b8b2" );
        makeMove( "c7c8", "b2d2" );
        makeMove( "c8f8", "f7e6" );
        makeMove( "h1h7", "d2f2" );
        makeMove( "f8f2", "g2f2" );
        makeMove( "h7g7", "f2f3" );
        makeMove( "g7e7", "e6e7" );
        makeMove( "g1f3" );

        assertEquals( 35, getNumberOfPliesPlayed() );
        assertEquals( 8, getNumberOfLegalMoves() );
        assertEquals( 3, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertTrue( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.INSUFFICIENT_MATERIAL );

        assertAllEnPassantPawns( new int[] { 1, 2 }, new SquareHelper[] { e4, d5 } );
    }

    @Test
    void insufficientMaterial_kingAndBishopVersusKing()
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
        makeMove( "g1g7", "f7g7" );
        makeMove( "c8d7", "d2f2" );
        makeMove( "d7e7", "f8e7" );
        makeMove( "e1f2" );

        assertEquals( 33, getNumberOfPliesPlayed() );
        assertEquals( 17, getNumberOfLegalMoves() );
        assertEquals( -3, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertTrue( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.INSUFFICIENT_MATERIAL );

        assertAllEnPassantPawns( new int[] { 1, 2 }, new SquareHelper[] { e4, d5 } );
    }

    @Test
    void insufficientMaterial_kingAndBishopVersusKingAndBishop_bishopsOnSameColour()
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

        assertEquals( 34, getNumberOfPliesPlayed() );
        assertEquals( 15, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isActivePlayerInCheck() );
        assertFalse( isActivePlayerCheckmated() );
        assertFalse( isActivePlayerStalemated() );
        assertTrue( boardHasInsufficientMaterial() );
        assertEquals( getGameStatus(), Game.Status.INSUFFICIENT_MATERIAL );

        assertAllEnPassantPawns( new int[] { 1, 2 }, new SquareHelper[] { e4, d5 } );
    }

    // ============================================================================================
    // Revoke Castling Rights
    // ============================================================================================

    @Test
    void revokeCastlingRights_byMovingQueensideRook()
    {
        makeMove( "b1c3", "b8c6" );
        makeMove( "a1b1", "a8b8" );

        assertFalse( isWhiteQueensideCastlingAllowed() );
        assertTrue( isWhiteKingsideCastlingAllowed() );
        assertFalse( isBlackQueensideCastlingAllowed() );
        assertTrue( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byMovingKingsideRook()
    {
        makeMove( "g1f3", "g8f6" );
        makeMove( "h1g1", "h8g8" );

        assertTrue( isWhiteQueensideCastlingAllowed() );
        assertFalse( isWhiteKingsideCastlingAllowed() );
        assertTrue( isBlackQueensideCastlingAllowed() );
        assertFalse( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byMovingKing()
    {
        makeMove( "e2e4", "e7e5" );
        makeMove( "e1e2", "e8e7" );

        assertFalse( isWhiteQueensideCastlingAllowed() );
        assertFalse( isWhiteKingsideCastlingAllowed() );
        assertFalse( isBlackQueensideCastlingAllowed() );
        assertFalse( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byCapturingQueensideRook()
    {
        makeMove( "g2g3", "g7g6" );
        makeMove( "f1g2", "f8g7" );
        makeMove( "b2b3", "b7b6" );
        makeMove( "g2a8", "g7a1" );

        assertFalse( isWhiteQueensideCastlingAllowed() );
        assertTrue( isWhiteKingsideCastlingAllowed() );
        assertFalse( isBlackQueensideCastlingAllowed() );
        assertTrue( isBlackKingsideCastlingAllowed() );
    }

    @Test
    void revokeCastlingRights_byCapturingKingsideRook()
    {
        makeMove( "b2b3", "b7b6" );
        makeMove( "c1b2", "c8b7" );
        makeMove( "g2g3", "g7g6" );
        makeMove( "b2h8", "b7h1" );

        assertTrue( isWhiteQueensideCastlingAllowed() );
        assertFalse( isWhiteKingsideCastlingAllowed() );
        assertTrue( isBlackQueensideCastlingAllowed() );
        assertFalse( isBlackKingsideCastlingAllowed() );
    }

    // ============================================================================================
    // Pawn Promotion
    // ============================================================================================

    @Test
    void pawnPromotion_newPieceTypeQueen()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8q", "g2h1q" );

        Piece whitePromotedQueen = a8.getPiece();
        assertEquals( whitePromotedQueen.getColour(), Player.Colour.WHITE );
        assertEquals( whitePromotedQueen.getType(), Piece.Type.QUEEN );

        Piece blackPromotedQueen = h1.getPiece();
        assertEquals( blackPromotedQueen.getColour(), Player.Colour.BLACK );
        assertEquals( blackPromotedQueen.getType(), Piece.Type.QUEEN );
    }

    @Test
    void pawnPromotion_newPieceTypeRook()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8r", "g2h1r" );

        Piece whitePromotedRook = a8.getPiece();
        assertEquals( whitePromotedRook.getColour(), Player.Colour.WHITE );
        assertEquals( whitePromotedRook.getType(), Piece.Type.ROOK );

        Piece blackPromotedRook = h1.getPiece();
        assertEquals( blackPromotedRook.getColour(), Player.Colour.BLACK );
        assertEquals( blackPromotedRook.getType(), Piece.Type.ROOK );
    }

    @Test
    void pawnPromotion_newPieceTypeBishop()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8b", "g2h1b" );

        Piece whitePromotedBishop = a8.getPiece();
        assertEquals( whitePromotedBishop.getColour(), Player.Colour.WHITE );
        assertEquals( whitePromotedBishop.getType(), Piece.Type.BISHOP );

        Piece blackPromotedBishop = h1.getPiece();
        assertEquals( blackPromotedBishop.getColour(), Player.Colour.BLACK );
        assertEquals( blackPromotedBishop.getType(), Piece.Type.BISHOP );
    }

    @Test
    void pawnPromotion_newPieceTypeKnight()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8n", "g2h1n" );

        Piece whitePromotedKnight = a8.getPiece();
        assertEquals( whitePromotedKnight.getColour(), Player.Colour.WHITE );
        assertEquals( whitePromotedKnight.getType(), Piece.Type.KNIGHT );

        Piece blackPromotedKnight = h1.getPiece();
        assertEquals( blackPromotedKnight.getColour(), Player.Colour.BLACK );
        assertEquals( blackPromotedKnight.getType(), Piece.Type.KNIGHT );
    }

    @Test
    void pawnPromotion_newPieceTypePawn_throwsInvalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        // Attempt to promote a pawn to a pawn
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( b7, a8, Piece.Type.PAWN ),
                                 "The new piece type (Pawn) must be one of Knight, Bishop, Rook or Queen." );
    }

    @Test
    void pawnPromotion_newPieceTypeKing_throwsInvalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        // Attempt to promote a pawn to a king
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( b7, a8, Piece.Type.KING ),
                                 "The new piece type (King) must be one of Knight, Bishop, Rook or Queen." );
    }

    @Test
    void pawnPromotion_newPieceTypeNull_throwsInvalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        // Attempt to move a pawn to the last rank without promoting
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "b7a8" ),
                                 "Promotion is mandatory, but no new piece type was specified." );
    }

    @Test
    void pawnPromotion_promotingPieceNotAPawn_throwsInvalidPromotionException()
    {
        makeMove( "g2g3", "g7g6" );
        makeMove( "f1g2", "b7b6" );

        // Attempt to promote a bishop by moving it to the last rank
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "g2a8q" ),
                                 "The promoting piece (White Bishop) must be a Pawn." );
    }

    @Test
    void pawnPromotion_promotionSquareNotOnLastRank_throwsInvalidPromotionException()
    {
        // Attempt to promote a pawn without moving it to the last rank
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "c2c4q" ),
                                 "The promotion square (c4) must be on White's last rank." );
    }
}
