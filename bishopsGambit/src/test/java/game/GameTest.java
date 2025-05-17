package test.java.game;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static test.java.Assertions.assertSameNotNull;
import static test.java.Assertions.assertThrowsWithMessage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import main.java.board.Board;
import main.java.board.Square;
import main.java.game.Game;
import main.java.game.Game.Status;
import main.java.game.IllegalMoveException;
import main.java.game.InvalidPromotionException;
import main.java.game.UnoccupiedSquareException;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.player.Player;
import main.java.player.Player.Colour;

class GameTest
{
    private Game game;

    private void makeMove( String... ucis )
    {
        for ( String uci : ucis )
            game.makeMove( uci );
    }

    private void makeMove( Square from, Square to, Typ newType )
    {
        game.makeMove( from, to, newType );
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

    private int getNumberOfTurnsTaken()
    {
        return game.getNumberOfTurnsTaken();
    }

    private int getNumberOfLegalMoves()
    {
        return getActivePlayer().getNumberOfLegalMoves( getBoard() );
    }

    private int getMaterialDifference()
    {
        return getBoard().getMaterialDifference();
    }

    private Status getStatus()
    {
        return game.getStatus();
    }

    private boolean isInCheck()
    {
        return getActivePlayer().isInCheck( getBoard() );
    }

    private boolean isCheckmated()
    {
        return getActivePlayer().isCheckmated( getBoard() );
    }

    private boolean isStalemated()
    {
        return getActivePlayer().isStalemated( getBoard() );
    }

    private boolean hasInsufficientMaterial()
    {
        return getBoard().hasInsufficientMaterial();
    }

    private boolean isWhiteQueensideCastlingAllowed()
    {
        return getBoard().isWhiteQueensideCastlingAllowed();
    }

    private boolean isWhiteKingsideCastlingAllowed()
    {
        return getBoard().isWhiteKingsideCastlingAllowed();
    }

    private boolean isBlackQueensideCastlingAllowed()
    {
        return getBoard().isBlackQueensideCastlingAllowed();
    }

    private boolean isBlackKingsideCastlingAllowed()
    {
        return getBoard().isBlackKingsideCastlingAllowed();
    }

    // ============================================================================================

    @BeforeEach
    void printName( TestInfo testInfo )
    {
        System.out.println( "Running test " + testInfo.getDisplayName() + "..." );
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
        System.out.println();
    }

    // ============================================================================================

    @Test
    void fromSquareUnoccupied_unoccupiedSquareException()
    {
        assertThrowsWithMessage( UnoccupiedSquareException.class,
                                 () -> makeMove( "e3e4" ),
                                 "Cannot make a move from the unoccupied square e3." );
    }

    @Test
    void toSquareOccupied_byFriendlyPiece_illegalMoveException()
    {
        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1e2" ),
                                 "The White King occupying e1 cannot legally move to e2." );
    }

    @Test
    void checkmate_foolsMate()
    {
        makeMove( "f2f3", "e7e5" );
        makeMove( "g2g4", "d8h4" );

        assertEquals( 4, getNumberOfTurnsTaken() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertTrue( isInCheck() );
        assertTrue( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.CHECKMATE );
    }

    @Test
    void checkmate_scholarsMate()
    {
        makeMove( "e2e4", "e7e5" );
        makeMove( "d1h5", "b8c6" );
        makeMove( "f1c4", "g8f6" );
        makeMove( "h5f7" );

        assertEquals( 7, getNumberOfTurnsTaken() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 1, getMaterialDifference() );

        assertTrue( isInCheck() );
        assertTrue( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.CHECKMATE );
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

        assertEquals( 8, getNumberOfTurnsTaken() );
        assertEquals( 30, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.DEFAULT );

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

        assertEquals( 10, getNumberOfTurnsTaken() );
        assertEquals( 30, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.DEFAULT );

        assertSameNotNull( whiteKing, getPiece( "c1" ) );
        assertSameNotNull( blackKing, getPiece( "c8" ) );
        assertSameNotNull( whiteQueensideRook, getPiece( "d1" ) );
        assertSameNotNull( blackQueensideRook, getPiece( "d8" ) );
    }

    @Test
    void enPassant_illegalMoveException()
    {
        makeMove( "d2d4", "e7e6" );
        makeMove( "d4d5", "e6e5" );

        assertEquals( 4, getNumberOfTurnsTaken() );
        assertEquals( 29, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.DEFAULT );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "d5e6" ),
                                 "The White Pawn occupying d5 cannot legally move to e6." );
    }

    @Test
    void enPassant_onlyLegalMove_inCheck()
    {
        makeMove( "e2e4", "e7e6" );
        makeMove( "e4e5", "d8h4" );
        makeMove( "e1e2", "b8c6" );
        makeMove( "e2e3", "h4g3" );
        makeMove( "e3e4", "d7d5" );

        assertEquals( 10, getNumberOfTurnsTaken() );
        assertEquals( 1, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertTrue( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.CHECK );

        assertDoesNotThrow( () -> makeMove( "e5d6" ) );
    }

    @Test
    void kingsideCastling_fSquareOccupied_byEnemyKnight_illegalMoveException()
    {
        makeMove( "e2e4", "g8f6" );
        makeMove( "g1f3", "f6g4" );
        makeMove( "f1c4", "g4e3" );
        makeMove( "d2d3", "e3f1" );

        assertEquals( 8, getNumberOfTurnsTaken() );
        assertEquals( 37, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.DEFAULT );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "The White King occupying e1 cannot legally move to g1." );

        Piece piece = getPiece( "f1" );
        assertEquals( piece.getColour(), Colour.BLACK );
        assertEquals( piece.getType(), Typ.KNIGHT );
    }

    @Test
    void kingsideCastling_fSquareOccupied_byEnemyBishop_illegalMoveException()
    {
        makeMove( "e2e4", "b7b6" );
        makeMove( "g1f3", "c8a6" );
        makeMove( "d2d4", "a6f1" );

        assertEquals( 6, getNumberOfTurnsTaken() );
        assertEquals( 32, getNumberOfLegalMoves() );
        assertEquals( -3, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.DEFAULT );

        assertThrowsWithMessage( IllegalMoveException.class,
                                 () -> makeMove( "e1g1" ),
                                 "The White King occupying e1 cannot legally move to g1." );

        Piece piece = getPiece( "f1" );
        assertEquals( piece.getColour(), Colour.BLACK );
        assertEquals( piece.getType(), Typ.BISHOP );
    }

    @Test
    void stalemate_nineteenTurns()
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

        assertEquals( 19, getNumberOfTurnsTaken() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 10, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertTrue( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.STALEMATE );
    }

    @Test
    void stalemate_twentyFourTurns_noCaptures()
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

        assertEquals( 24, getNumberOfTurnsTaken() );
        assertEquals( 0, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertTrue( isStalemated() );
        assertFalse( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.STALEMATE );
    }

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

        assertEquals( 33, getNumberOfTurnsTaken() );
        assertEquals( 8, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.INSUFFICIENT_MATERIAL );
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

        assertEquals( 35, getNumberOfTurnsTaken() );
        assertEquals( 8, getNumberOfLegalMoves() );
        assertEquals( 3, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.INSUFFICIENT_MATERIAL );
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

        assertEquals( 33, getNumberOfTurnsTaken() );
        assertEquals( 17, getNumberOfLegalMoves() );
        assertEquals( -3, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.INSUFFICIENT_MATERIAL );
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

        assertEquals( 34, getNumberOfTurnsTaken() );
        assertEquals( 15, getNumberOfLegalMoves() );
        assertEquals( 0, getMaterialDifference() );

        assertFalse( isInCheck() );
        assertFalse( isCheckmated() );
        assertFalse( isStalemated() );
        assertTrue( hasInsufficientMaterial() );
        assertEquals( getStatus(), Status.INSUFFICIENT_MATERIAL );
    }

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

    @Test
    void pawnPromotion_newPieceTypeQueen()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8q", "g2h1q" );

        Piece newPieceWhite = getPiece( "a8" );
        assertEquals( newPieceWhite.getColour(), Colour.WHITE );
        assertEquals( newPieceWhite.getType(), Typ.QUEEN );

        Piece newPieceBlack = getPiece( "h1" );
        assertEquals( newPieceBlack.getColour(), Colour.BLACK );
        assertEquals( newPieceBlack.getType(), Typ.QUEEN );
    }

    @Test
    void pawnPromotion_newPieceTypeRook()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8r", "g2h1r" );

        Piece newPieceWhite = getPiece( "a8" );
        assertEquals( newPieceWhite.getColour(), Colour.WHITE );
        assertEquals( newPieceWhite.getType(), Typ.ROOK );

        Piece newPieceBlack = getPiece( "h1" );
        assertEquals( newPieceBlack.getColour(), Colour.BLACK );
        assertEquals( newPieceBlack.getType(), Typ.ROOK );
    }

    @Test
    void pawnPromotion_newPieceTypeBishop()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8b", "g2h1b" );

        Piece newPieceWhite = getPiece( "a8" );
        assertEquals( newPieceWhite.getColour(), Colour.WHITE );
        assertEquals( newPieceWhite.getType(), Typ.BISHOP );

        Piece newPieceBlack = getPiece( "h1" );
        assertEquals( newPieceBlack.getColour(), Colour.BLACK );
        assertEquals( newPieceBlack.getType(), Typ.BISHOP );
    }

    @Test
    void pawnPromotion_newPieceTypeKnight()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );
        makeMove( "b7a8n", "g2h1n" );

        Piece newPieceWhite = getPiece( "a8" );
        assertEquals( newPieceWhite.getColour(), Colour.WHITE );
        assertEquals( newPieceWhite.getType(), Typ.KNIGHT );

        Piece newPieceBlack = getPiece( "h1" );
        assertEquals( newPieceBlack.getColour(), Colour.BLACK );
        assertEquals( newPieceBlack.getType(), Typ.KNIGHT );
    }

    @Test
    void pawnPromotion_newPieceTypePawn_invalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        Square from = getSquare( "b7" );
        Square to = getSquare( "a8" );

        // Attempt to promote a pawn to a pawn
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( from, to, Typ.PAWN ),
                                 "The new piece type (Pawn) must be one of Knight, Bishop, Rook or Queen." );
    }

    @Test
    void pawnPromotion_newPieceTypeKing_invalidPromotionException()
    {
        makeMove( "c2c4", "f7f5" );
        makeMove( "c4c5", "f5f4" );
        makeMove( "c5c6", "f4f3" );
        makeMove( "c6b7", "f3g2" );

        Square from = getSquare( "b7" );
        Square to = getSquare( "a8" );

        // Attempt to promote a pawn to a king
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( from, to, Typ.KING ),
                                 "The new piece type (King) must be one of Knight, Bishop, Rook or Queen." );
    }

    @Test
    void pawnPromotion_newPieceTypeNull_invalidPromotionException()
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
    void pawnPromotion_promotingPieceNotAPawn_invalidPromotionException()
    {
        makeMove( "g2g3", "g7g6" );
        makeMove( "f1g2", "b7b6" );

        // Attempt to promote a bishop by moving it to the last rank
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "g2a8q" ),
                                 "The promoting piece (White Bishop) must be a Pawn." );
    }

    @Test
    void pawnPromotion_promotionSquareNotOnLastRank_invalidPromotionException()
    {
        // Attempt to promote a pawn without moving it to the last rank
        assertThrowsWithMessage( InvalidPromotionException.class,
                                 () -> makeMove( "c2c4q" ),
                                 "The promotion square (c4) must be on White's last rank." );
    }
}
