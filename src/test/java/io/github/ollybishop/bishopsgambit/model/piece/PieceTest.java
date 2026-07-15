package io.github.ollybishop.bishopsgambit.model.piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import io.github.ollybishop.bishopsgambit.game.Game;
import io.github.ollybishop.bishopsgambit.testutil.SquareHelper;

class PieceTest
{
    private Game game;

    // =========================================================================================

    private final SquareHelper a1 = new SquareHelper( () -> game, 'a', '1' );
    private final SquareHelper a2 = new SquareHelper( () -> game, 'a', '2' );
    private final SquareHelper a3 = new SquareHelper( () -> game, 'a', '3' );
    private final SquareHelper a4 = new SquareHelper( () -> game, 'a', '4' );
    private final SquareHelper a5 = new SquareHelper( () -> game, 'a', '5' );
    private final SquareHelper a6 = new SquareHelper( () -> game, 'a', '6' );
    private final SquareHelper a7 = new SquareHelper( () -> game, 'a', '7' );
    private final SquareHelper a8 = new SquareHelper( () -> game, 'a', '8' );

    private final SquareHelper b1 = new SquareHelper( () -> game, 'b', '1' );
    private final SquareHelper b2 = new SquareHelper( () -> game, 'b', '2' );
    private final SquareHelper b3 = new SquareHelper( () -> game, 'b', '3' );
    private final SquareHelper b4 = new SquareHelper( () -> game, 'b', '4' );
    private final SquareHelper b5 = new SquareHelper( () -> game, 'b', '5' );
    private final SquareHelper b6 = new SquareHelper( () -> game, 'b', '6' );
    private final SquareHelper b7 = new SquareHelper( () -> game, 'b', '7' );
    private final SquareHelper b8 = new SquareHelper( () -> game, 'b', '8' );

    private final SquareHelper c1 = new SquareHelper( () -> game, 'c', '1' );
    private final SquareHelper c2 = new SquareHelper( () -> game, 'c', '2' );
    private final SquareHelper c3 = new SquareHelper( () -> game, 'c', '3' );
    private final SquareHelper c4 = new SquareHelper( () -> game, 'c', '4' );
    private final SquareHelper c5 = new SquareHelper( () -> game, 'c', '5' );
    private final SquareHelper c6 = new SquareHelper( () -> game, 'c', '6' );
    private final SquareHelper c7 = new SquareHelper( () -> game, 'c', '7' );
    private final SquareHelper c8 = new SquareHelper( () -> game, 'c', '8' );

    private final SquareHelper d1 = new SquareHelper( () -> game, 'd', '1' );
    private final SquareHelper d2 = new SquareHelper( () -> game, 'd', '2' );
    private final SquareHelper d3 = new SquareHelper( () -> game, 'd', '3' );
    private final SquareHelper d4 = new SquareHelper( () -> game, 'd', '4' );
    private final SquareHelper d5 = new SquareHelper( () -> game, 'd', '5' );
    private final SquareHelper d6 = new SquareHelper( () -> game, 'd', '6' );
    private final SquareHelper d7 = new SquareHelper( () -> game, 'd', '7' );
    private final SquareHelper d8 = new SquareHelper( () -> game, 'd', '8' );

    private final SquareHelper e1 = new SquareHelper( () -> game, 'e', '1' );
    private final SquareHelper e2 = new SquareHelper( () -> game, 'e', '2' );
    private final SquareHelper e3 = new SquareHelper( () -> game, 'e', '3' );
    private final SquareHelper e4 = new SquareHelper( () -> game, 'e', '4' );
    private final SquareHelper e5 = new SquareHelper( () -> game, 'e', '5' );
    private final SquareHelper e6 = new SquareHelper( () -> game, 'e', '6' );
    private final SquareHelper e7 = new SquareHelper( () -> game, 'e', '7' );
    private final SquareHelper e8 = new SquareHelper( () -> game, 'e', '8' );

    private final SquareHelper f1 = new SquareHelper( () -> game, 'f', '1' );
    private final SquareHelper f2 = new SquareHelper( () -> game, 'f', '2' );
    private final SquareHelper f3 = new SquareHelper( () -> game, 'f', '3' );
    private final SquareHelper f4 = new SquareHelper( () -> game, 'f', '4' );
    private final SquareHelper f5 = new SquareHelper( () -> game, 'f', '5' );
    private final SquareHelper f6 = new SquareHelper( () -> game, 'f', '6' );
    private final SquareHelper f7 = new SquareHelper( () -> game, 'f', '7' );
    private final SquareHelper f8 = new SquareHelper( () -> game, 'f', '8' );

    private final SquareHelper g1 = new SquareHelper( () -> game, 'g', '1' );
    private final SquareHelper g2 = new SquareHelper( () -> game, 'g', '2' );
    private final SquareHelper g3 = new SquareHelper( () -> game, 'g', '3' );
    private final SquareHelper g4 = new SquareHelper( () -> game, 'g', '4' );
    private final SquareHelper g5 = new SquareHelper( () -> game, 'g', '5' );
    private final SquareHelper g6 = new SquareHelper( () -> game, 'g', '6' );
    private final SquareHelper g7 = new SquareHelper( () -> game, 'g', '7' );
    private final SquareHelper g8 = new SquareHelper( () -> game, 'g', '8' );

    private final SquareHelper h1 = new SquareHelper( () -> game, 'h', '1' );
    private final SquareHelper h2 = new SquareHelper( () -> game, 'h', '2' );
    private final SquareHelper h3 = new SquareHelper( () -> game, 'h', '3' );
    private final SquareHelper h4 = new SquareHelper( () -> game, 'h', '4' );
    private final SquareHelper h5 = new SquareHelper( () -> game, 'h', '5' );
    private final SquareHelper h6 = new SquareHelper( () -> game, 'h', '6' );
    private final SquareHelper h7 = new SquareHelper( () -> game, 'h', '7' );
    private final SquareHelper h8 = new SquareHelper( () -> game, 'h', '8' );

    // =========================================================================================

    private void makeMove( SquareHelper from, SquareHelper to )
    {
        makeMove( from, to, null );
    }

    private void makeMove( SquareHelper from, SquareHelper to, Piece.Type newType )
    {
        game.makeMove( from.getSquare(), to.getSquare(), newType );
    }

    // =========================================================================================

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

    // =========================================================================================
    // Initial Setup
    // =========================================================================================

    // White a-pawn

    @Test
    void initialSetup_whiteAPawn_controlledSquares()
    {
        PieceHelper whiteAPawn = a2.getPieceHelper();

        assertEquals( 1, whiteAPawn.getControlledSquares().size() );

        assertFalse( whiteAPawn.controls( a3 ) );
        assertFalse( whiteAPawn.controls( a4 ) );
        assertTrue( whiteAPawn.controls( b3 ) );
    }

    @Test
    void initialSetup_whiteAPawn_pseudoLegalMoves()
    {
        PieceHelper whiteAPawn = a2.getPieceHelper();

        assertEquals( 2, whiteAPawn.getPseudoLegalMoves().size() );

        assertTrue( whiteAPawn.canPseudoLegallyMoveTo( a3 ) );
        assertTrue( whiteAPawn.canPseudoLegallyMoveTo( a4 ) );
        assertFalse( whiteAPawn.canPseudoLegallyMoveTo( b3 ) );
    }

    // White e-pawn

    @Test
    void initialSetup_whiteEPawn_controlledSquares()
    {
        PieceHelper whiteEPawn = e2.getPieceHelper();

        assertEquals( 2, whiteEPawn.getControlledSquares().size() );

        assertTrue( whiteEPawn.controls( d3 ) );
        assertFalse( whiteEPawn.controls( e3 ) );
        assertFalse( whiteEPawn.controls( e4 ) );
        assertTrue( whiteEPawn.controls( f3 ) );
    }

    @Test
    void initialSetup_whiteEPawn_pseudoLegalMoves()
    {
        PieceHelper whiteEPawn = e2.getPieceHelper();

        assertEquals( 2, whiteEPawn.getPseudoLegalMoves().size() );

        assertFalse( whiteEPawn.canPseudoLegallyMoveTo( d3 ) );
        assertTrue( whiteEPawn.canPseudoLegallyMoveTo( e3 ) );
        assertTrue( whiteEPawn.canPseudoLegallyMoveTo( e4 ) );
        assertFalse( whiteEPawn.canPseudoLegallyMoveTo( f3 ) );
    }

    // White h-pawn

    @Test
    void initialSetup_whiteHPawn_controlledSquares()
    {
        PieceHelper whiteHPawn = h2.getPieceHelper();

        assertEquals( 1, whiteHPawn.getControlledSquares().size() );

        assertTrue( whiteHPawn.controls( g3 ) );
        assertFalse( whiteHPawn.controls( h3 ) );
        assertFalse( whiteHPawn.controls( h4 ) );
    }

    @Test
    void initialSetup_whiteHPawn_pseudoLegalMoves()
    {
        PieceHelper whiteHPawn = h2.getPieceHelper();

        assertEquals( 2, whiteHPawn.getPseudoLegalMoves().size() );

        assertFalse( whiteHPawn.canPseudoLegallyMoveTo( g3 ) );
        assertTrue( whiteHPawn.canPseudoLegallyMoveTo( h3 ) );
        assertTrue( whiteHPawn.canPseudoLegallyMoveTo( h4 ) );
    }

    // Black a-pawn

    @Test
    void initialSetup_blackAPawn_controlledSquares()
    {
        PieceHelper blackAPawn = a7.getPieceHelper();

        assertEquals( 1, blackAPawn.getControlledSquares().size() );

        assertFalse( blackAPawn.controls( a6 ) );
        assertFalse( blackAPawn.controls( a5 ) );
        assertTrue( blackAPawn.controls( b6 ) );
    }

    @Test
    void initialSetup_blackAPawn_pseudoLegalMoves()
    {
        PieceHelper blackAPawn = a7.getPieceHelper();

        assertEquals( 2, blackAPawn.getPseudoLegalMoves().size() );

        assertTrue( blackAPawn.canPseudoLegallyMoveTo( a6 ) );
        assertTrue( blackAPawn.canPseudoLegallyMoveTo( a5 ) );
        assertFalse( blackAPawn.canPseudoLegallyMoveTo( b6 ) );
    }

    // Black e-pawn

    @Test
    void initialSetup_blackEPawn_controlledSquares()
    {
        PieceHelper blackEPawn = e7.getPieceHelper();

        assertEquals( 2, blackEPawn.getControlledSquares().size() );

        assertTrue( blackEPawn.controls( d6 ) );
        assertFalse( blackEPawn.controls( e6 ) );
        assertFalse( blackEPawn.controls( e5 ) );
        assertTrue( blackEPawn.controls( f6 ) );
    }

    @Test
    void initialSetup_blackEPawn_pseudoLegalMoves()
    {
        PieceHelper blackEPawn = e7.getPieceHelper();

        assertEquals( 2, blackEPawn.getPseudoLegalMoves().size() );

        assertFalse( blackEPawn.canPseudoLegallyMoveTo( d6 ) );
        assertTrue( blackEPawn.canPseudoLegallyMoveTo( e6 ) );
        assertTrue( blackEPawn.canPseudoLegallyMoveTo( e5 ) );
        assertFalse( blackEPawn.canPseudoLegallyMoveTo( f6 ) );
    }

    // Black h-pawn

    @Test
    void initialSetup_blackHPawn_controlledSquares()
    {
        PieceHelper blackHPawn = h7.getPieceHelper();

        assertEquals( 1, blackHPawn.getControlledSquares().size() );

        assertTrue( blackHPawn.controls( g6 ) );
        assertFalse( blackHPawn.controls( h6 ) );
        assertFalse( blackHPawn.controls( h5 ) );
    }

    @Test
    void initialSetup_blackHPawn_pseudoLegalMoves()
    {
        PieceHelper blackHPawn = h7.getPieceHelper();

        assertEquals( 2, blackHPawn.getPseudoLegalMoves().size() );

        assertFalse( blackHPawn.canPseudoLegallyMoveTo( g6 ) );
        assertTrue( blackHPawn.canPseudoLegallyMoveTo( h6 ) );
        assertTrue( blackHPawn.canPseudoLegallyMoveTo( h5 ) );
    }

    // White queenside knight

    @Test
    void initialSetup_whiteQueensideKnight_controlledSquares()
    {
        PieceHelper whiteQueensideKnight = b1.getPieceHelper();

        assertEquals( 3, whiteQueensideKnight.getControlledSquares().size() );

        assertTrue( whiteQueensideKnight.controls( a3 ) );
        assertTrue( whiteQueensideKnight.controls( c3 ) );
        assertTrue( whiteQueensideKnight.controls( d2 ) );
    }

    @Test
    void initialSetup_whiteQueensideKnight_pseudoLegalMoves()
    {
        PieceHelper whiteQueensideKnight = b1.getPieceHelper();

        assertEquals( 2, whiteQueensideKnight.getPseudoLegalMoves().size() );

        assertTrue( whiteQueensideKnight.canPseudoLegallyMoveTo( a3 ) );
        assertTrue( whiteQueensideKnight.canPseudoLegallyMoveTo( c3 ) );
        assertFalse( whiteQueensideKnight.canPseudoLegallyMoveTo( d2 ) );
    }

    // White kingside knight

    @Test
    void initialSetup_whiteKingsideKnight_controlledSquares()
    {
        PieceHelper whiteKingsideKnight = g1.getPieceHelper();

        assertEquals( 3, whiteKingsideKnight.getControlledSquares().size() );

        assertTrue( whiteKingsideKnight.controls( e2 ) );
        assertTrue( whiteKingsideKnight.controls( f3 ) );
        assertTrue( whiteKingsideKnight.controls( h3 ) );
    }

    @Test
    void initialSetup_whiteKingsideKnight_pseudoLegalMoves()
    {
        PieceHelper whiteKingsideKnight = g1.getPieceHelper();

        assertEquals( 2, whiteKingsideKnight.getPseudoLegalMoves().size() );

        assertFalse( whiteKingsideKnight.canPseudoLegallyMoveTo( e2 ) );
        assertTrue( whiteKingsideKnight.canPseudoLegallyMoveTo( f3 ) );
        assertTrue( whiteKingsideKnight.canPseudoLegallyMoveTo( h3 ) );
    }

    // Black queenside knight

    @Test
    void initialSetup_blackQueensideKnight_controlledSquares()
    {
        PieceHelper blackQueensideKnight = b8.getPieceHelper();

        assertEquals( 3, blackQueensideKnight.getControlledSquares().size() );

        assertTrue( blackQueensideKnight.controls( a6 ) );
        assertTrue( blackQueensideKnight.controls( c6 ) );
        assertTrue( blackQueensideKnight.controls( d7 ) );
    }

    @Test
    void initialSetup_blackQueensideKnight_pseudoLegalMoves()
    {
        PieceHelper blackQueensideKnight = b8.getPieceHelper();

        assertEquals( 2, blackQueensideKnight.getPseudoLegalMoves().size() );

        assertTrue( blackQueensideKnight.canPseudoLegallyMoveTo( a6 ) );
        assertTrue( blackQueensideKnight.canPseudoLegallyMoveTo( c6 ) );
        assertFalse( blackQueensideKnight.canPseudoLegallyMoveTo( d7 ) );
    }

    // Black kingside knight

    @Test
    void initialSetup_blackKingsideKnight_controlledSquares()
    {
        PieceHelper blackKingsideKnight = g8.getPieceHelper();

        assertEquals( 3, blackKingsideKnight.getControlledSquares().size() );

        assertTrue( blackKingsideKnight.controls( e7 ) );
        assertTrue( blackKingsideKnight.controls( f6 ) );
        assertTrue( blackKingsideKnight.controls( h6 ) );
    }

    @Test
    void initialSetup_blackKingsideKnight_pseudoLegalMoves()
    {
        PieceHelper blackKingsideKnight = g8.getPieceHelper();

        assertEquals( 2, blackKingsideKnight.getPseudoLegalMoves().size() );

        assertFalse( blackKingsideKnight.canPseudoLegallyMoveTo( e7 ) );
        assertTrue( blackKingsideKnight.canPseudoLegallyMoveTo( f6 ) );
        assertTrue( blackKingsideKnight.canPseudoLegallyMoveTo( h6 ) );
    }

    // White queenside bishop

    @Test
    void initialSetup_whiteQueensideBishop_controlledSquares()
    {
        PieceHelper whiteQueensideBishop = c1.getPieceHelper();

        assertEquals( 2, whiteQueensideBishop.getControlledSquares().size() );

        assertTrue( whiteQueensideBishop.controls( b2 ) );
        assertTrue( whiteQueensideBishop.controls( d2 ) );
    }

    @Test
    void initialSetup_whiteQueensideBishop_pseudoLegalMoves()
    {
        PieceHelper whiteQueensideBishop = c1.getPieceHelper();

        assertEquals( 0, whiteQueensideBishop.getPseudoLegalMoves().size() );

        assertFalse( whiteQueensideBishop.canPseudoLegallyMoveTo( b2 ) );
        assertFalse( whiteQueensideBishop.canPseudoLegallyMoveTo( d2 ) );
    }

    // White kingside bishop

    @Test
    void initialSetup_whiteKingsideBishop_controlledSquares()
    {
        PieceHelper whiteKingsideBishop = f1.getPieceHelper();

        assertEquals( 2, whiteKingsideBishop.getControlledSquares().size() );

        assertTrue( whiteKingsideBishop.controls( e2 ) );
        assertTrue( whiteKingsideBishop.controls( g2 ) );
    }

    @Test
    void initialSetup_whiteKingsideBishop_pseudoLegalMoves()
    {
        PieceHelper whiteKingsideBishop = f1.getPieceHelper();

        assertEquals( 0, whiteKingsideBishop.getPseudoLegalMoves().size() );

        assertFalse( whiteKingsideBishop.canPseudoLegallyMoveTo( e2 ) );
        assertFalse( whiteKingsideBishop.canPseudoLegallyMoveTo( g2 ) );
    }

    // Black queenside bishop

    @Test
    void initialSetup_blackQueensideBishop_controlledSquares()
    {
        PieceHelper blackQueensideBishop = c8.getPieceHelper();

        assertEquals( 2, blackQueensideBishop.getControlledSquares().size() );

        assertTrue( blackQueensideBishop.controls( b7 ) );
        assertTrue( blackQueensideBishop.controls( d7 ) );
    }

    @Test
    void initialSetup_blackQueensideBishop_pseudoLegalMoves()
    {
        PieceHelper blackQueensideBishop = c8.getPieceHelper();

        assertEquals( 0, blackQueensideBishop.getPseudoLegalMoves().size() );

        assertFalse( blackQueensideBishop.canPseudoLegallyMoveTo( b7 ) );
        assertFalse( blackQueensideBishop.canPseudoLegallyMoveTo( d7 ) );
    }

    // Black kingside bishop

    @Test
    void initialSetup_blackKingsideBishop_controlledSquares()
    {
        PieceHelper blackKingsideBishop = f8.getPieceHelper();

        assertEquals( 2, blackKingsideBishop.getControlledSquares().size() );

        assertTrue( blackKingsideBishop.controls( e7 ) );
        assertTrue( blackKingsideBishop.controls( g7 ) );
    }

    @Test
    void initialSetup_blackKingsideBishop_pseudoLegalMoves()
    {
        PieceHelper blackKingsideBishop = f8.getPieceHelper();

        assertEquals( 0, blackKingsideBishop.getPseudoLegalMoves().size() );

        assertFalse( blackKingsideBishop.canPseudoLegallyMoveTo( e7 ) );
        assertFalse( blackKingsideBishop.canPseudoLegallyMoveTo( g7 ) );
    }

    // White queenside rook

    @Test
    void initialSetup_whiteQueensideRook_controlledSquares()
    {
        PieceHelper whiteQueensideRook = a1.getPieceHelper();

        assertEquals( 2, whiteQueensideRook.getControlledSquares().size() );

        assertTrue( whiteQueensideRook.controls( a2 ) );
        assertTrue( whiteQueensideRook.controls( b1 ) );
    }

    @Test
    void initialSetup_whiteQueensideRook_pseudoLegalMoves()
    {
        PieceHelper whiteQueensideRook = a1.getPieceHelper();

        assertEquals( 0, whiteQueensideRook.getPseudoLegalMoves().size() );

        assertFalse( whiteQueensideRook.canPseudoLegallyMoveTo( a2 ) );
        assertFalse( whiteQueensideRook.canPseudoLegallyMoveTo( b1 ) );
    }

    // White kingside rook

    @Test
    void initialSetup_whiteKingsideRook_controlledSquares()
    {
        PieceHelper whiteKingsideRook = h1.getPieceHelper();

        assertEquals( 2, whiteKingsideRook.getControlledSquares().size() );

        assertTrue( whiteKingsideRook.controls( g1 ) );
        assertTrue( whiteKingsideRook.controls( h2 ) );
    }

    @Test
    void initialSetup_whiteKingsideRook_pseudoLegalMoves()
    {
        PieceHelper whiteKingsideRook = h1.getPieceHelper();

        assertEquals( 0, whiteKingsideRook.getPseudoLegalMoves().size() );

        assertFalse( whiteKingsideRook.canPseudoLegallyMoveTo( g1 ) );
        assertFalse( whiteKingsideRook.canPseudoLegallyMoveTo( h2 ) );
    }

    // Black queenside rook

    @Test
    void initialSetup_blackQueensideRook_controlledSquares()
    {
        PieceHelper blackQueensideRook = a8.getPieceHelper();

        assertEquals( 2, blackQueensideRook.getControlledSquares().size() );

        assertTrue( blackQueensideRook.controls( a7 ) );
        assertTrue( blackQueensideRook.controls( b8 ) );
    }

    @Test
    void initialSetup_blackQueensideRook_pseudoLegalMoves()
    {
        PieceHelper blackQueensideRook = a8.getPieceHelper();

        assertEquals( 0, blackQueensideRook.getPseudoLegalMoves().size() );

        assertFalse( blackQueensideRook.canPseudoLegallyMoveTo( a7 ) );
        assertFalse( blackQueensideRook.canPseudoLegallyMoveTo( b8 ) );
    }

    // Black kingside rook

    @Test
    void initialSetup_blackKingsideRook_controlledSquares()
    {
        PieceHelper blackKingsideRook = h8.getPieceHelper();

        assertEquals( 2, blackKingsideRook.getControlledSquares().size() );

        assertTrue( blackKingsideRook.controls( g8 ) );
        assertTrue( blackKingsideRook.controls( h7 ) );
    }

    @Test
    void initialSetup_blackKingsideRook_pseudoLegalMoves()
    {
        PieceHelper blackKingsideRook = h8.getPieceHelper();

        assertEquals( 0, blackKingsideRook.getPseudoLegalMoves().size() );

        assertFalse( blackKingsideRook.canPseudoLegallyMoveTo( g8 ) );
        assertFalse( blackKingsideRook.canPseudoLegallyMoveTo( h7 ) );
    }

    // White queen

    @Test
    void initialSetup_whiteQueen_controlledSquares()
    {
        PieceHelper whiteQueen = d1.getPieceHelper();

        assertEquals( 5, whiteQueen.getControlledSquares().size() );

        assertTrue( whiteQueen.controls( c1 ) );
        assertTrue( whiteQueen.controls( c2 ) );
        assertTrue( whiteQueen.controls( d2 ) );
        assertTrue( whiteQueen.controls( e1 ) );
        assertTrue( whiteQueen.controls( e2 ) );
    }

    @Test
    void initialSetup_whiteQueen_pseudoLegalMoves()
    {
        PieceHelper whiteQueen = d1.getPieceHelper();

        assertEquals( 0, whiteQueen.getPseudoLegalMoves().size() );

        assertFalse( whiteQueen.canPseudoLegallyMoveTo( c1 ) );
        assertFalse( whiteQueen.canPseudoLegallyMoveTo( c2 ) );
        assertFalse( whiteQueen.canPseudoLegallyMoveTo( d2 ) );
        assertFalse( whiteQueen.canPseudoLegallyMoveTo( e1 ) );
        assertFalse( whiteQueen.canPseudoLegallyMoveTo( e2 ) );
    }

    // Black queen

    @Test
    void initialSetup_blackQueen_controlledSquares()
    {
        PieceHelper blackQueen = d8.getPieceHelper();

        assertEquals( 5, blackQueen.getControlledSquares().size() );

        assertTrue( blackQueen.controls( c8 ) );
        assertTrue( blackQueen.controls( c7 ) );
        assertTrue( blackQueen.controls( d7 ) );
        assertTrue( blackQueen.controls( e8 ) );
        assertTrue( blackQueen.controls( e7 ) );
    }

    @Test
    void initialSetup_blackQueen_pseudoLegalMoves()
    {
        PieceHelper blackQueen = d8.getPieceHelper();

        assertEquals( 0, blackQueen.getPseudoLegalMoves().size() );

        assertFalse( blackQueen.canPseudoLegallyMoveTo( c8 ) );
        assertFalse( blackQueen.canPseudoLegallyMoveTo( c7 ) );
        assertFalse( blackQueen.canPseudoLegallyMoveTo( d7 ) );
        assertFalse( blackQueen.canPseudoLegallyMoveTo( e8 ) );
        assertFalse( blackQueen.canPseudoLegallyMoveTo( e7 ) );
    }

    // White king

    @Test
    void initialSetup_whiteKing_controlledSquares()
    {
        PieceHelper whiteKing = e1.getPieceHelper();

        assertEquals( 5, whiteKing.getControlledSquares().size() );

        assertTrue( whiteKing.controls( d1 ) );
        assertTrue( whiteKing.controls( d2 ) );
        assertTrue( whiteKing.controls( e2 ) );
        assertTrue( whiteKing.controls( f1 ) );
        assertTrue( whiteKing.controls( f2 ) );
    }

    @Test
    void initialSetup_whiteKing_pseudoLegalMoves()
    {
        PieceHelper whiteKing = e1.getPieceHelper();

        assertEquals( 0, whiteKing.getPseudoLegalMoves().size() );

        assertFalse( whiteKing.canPseudoLegallyMoveTo( d1 ) );
        assertFalse( whiteKing.canPseudoLegallyMoveTo( d2 ) );
        assertFalse( whiteKing.canPseudoLegallyMoveTo( e2 ) );
        assertFalse( whiteKing.canPseudoLegallyMoveTo( f1 ) );
        assertFalse( whiteKing.canPseudoLegallyMoveTo( f2 ) );
    }

    // Black king

    @Test
    void initialSetup_blackKing_controlledSquares()
    {
        PieceHelper blackKing = e8.getPieceHelper();

        assertEquals( 5, blackKing.getControlledSquares().size() );

        assertTrue( blackKing.controls( d8 ) );
        assertTrue( blackKing.controls( d7 ) );
        assertTrue( blackKing.controls( e7 ) );
        assertTrue( blackKing.controls( f8 ) );
        assertTrue( blackKing.controls( f7 ) );
    }

    @Test
    void initialSetup_blackKing_pseudoLegalMoves()
    {
        PieceHelper blackKing = e8.getPieceHelper();

        assertEquals( 0, blackKing.getPseudoLegalMoves().size() );

        assertFalse( blackKing.canPseudoLegallyMoveTo( d8 ) );
        assertFalse( blackKing.canPseudoLegallyMoveTo( d7 ) );
        assertFalse( blackKing.canPseudoLegallyMoveTo( e7 ) );
        assertFalse( blackKing.canPseudoLegallyMoveTo( f8 ) );
        assertFalse( blackKing.canPseudoLegallyMoveTo( f7 ) );
    }

    // =========================================================================================
    // Developed Positions
    // =========================================================================================

    // Bishop blockers

    @Test
    void developedPosition_whiteBishop_controlledSquares_stopAtFirstPieceInEachDirection()
    {
        makeMove( e2, e4 );
        makeMove( b7, b5 );
        makeMove( f1, d3 );

        PieceHelper whiteBishop = d3.getPieceHelper();

        assertEquals( 6, whiteBishop.getControlledSquares().size() );

        assertFalse( whiteBishop.controls( a6 ) );
        assertFalse( whiteBishop.controls( b1 ) );
        assertTrue( whiteBishop.controls( b5 ) );
        assertTrue( whiteBishop.controls( c2 ) );
        assertTrue( whiteBishop.controls( c4 ) );
        assertTrue( whiteBishop.controls( e2 ) );
        assertTrue( whiteBishop.controls( e4 ) );
        assertTrue( whiteBishop.controls( f1 ) );
        assertFalse( whiteBishop.controls( f5 ) );
    }

    @Test
    void developedPosition_whiteBishop_pseudoLegalMoves_includeEnemyBlockersButExcludeFriendlyBlockers()
    {
        makeMove( e2, e4 );
        makeMove( b7, b5 );
        makeMove( f1, d3 );

        PieceHelper whiteBishop = d3.getPieceHelper();

        assertEquals( 4, whiteBishop.getPseudoLegalMoves().size() );

        assertFalse( whiteBishop.canPseudoLegallyMoveTo( a6 ) );
        assertFalse( whiteBishop.canPseudoLegallyMoveTo( b1 ) );
        assertTrue( whiteBishop.canPseudoLegallyMoveTo( b5 ) );
        assertFalse( whiteBishop.canPseudoLegallyMoveTo( c2 ) );
        assertTrue( whiteBishop.canPseudoLegallyMoveTo( c4 ) );
        assertTrue( whiteBishop.canPseudoLegallyMoveTo( e2 ) );
        assertFalse( whiteBishop.canPseudoLegallyMoveTo( e4 ) );
        assertTrue( whiteBishop.canPseudoLegallyMoveTo( f1 ) );
        assertFalse( whiteBishop.canPseudoLegallyMoveTo( f5 ) );
    }

    // Pawn captures and blockers

    @Test
    void developedPosition_whitePawn_controlledSquares_includeBothDiagonals()
    {
        makeMove( e2, e4 );
        makeMove( d7, d5 );
        makeMove( f2, f4 );
        makeMove( a7, a6 );
        makeMove( f4, f5 );

        PieceHelper whitePawn = e4.getPieceHelper();

        assertEquals( 2, whitePawn.getControlledSquares().size() );

        assertTrue( whitePawn.controls( d5 ) );
        assertFalse( whitePawn.controls( e5 ) );
        assertTrue( whitePawn.controls( f5 ) );
    }

    @Test
    void developedPosition_whitePawn_pseudoLegalMoves_includeForwardMoveAndEnemyCaptureOnly()
    {
        makeMove( e2, e4 );
        makeMove( d7, d5 );
        makeMove( f2, f4 );
        makeMove( a7, a6 );
        makeMove( f4, f5 );

        PieceHelper whitePawn = e4.getPieceHelper();

        assertEquals( 2, whitePawn.getPseudoLegalMoves().size() );

        assertTrue( whitePawn.canPseudoLegallyMoveTo( d5 ) );
        assertTrue( whitePawn.canPseudoLegallyMoveTo( e5 ) );
        assertFalse( whitePawn.canPseudoLegallyMoveTo( f5 ) );
    }

    // Knight movement

    @Test
    void developedPosition_whiteKnight_controlledSquares_includeAllEightJumpSquares()
    {
        makeMove( g1, f3 );
        makeMove( b8, c6 );
        makeMove( f3, d4 );
        makeMove( g8, f6 );

        PieceHelper whiteKnight = d4.getPieceHelper();

        assertEquals( 8, whiteKnight.getControlledSquares().size() );

        assertTrue( whiteKnight.controls( b3 ) );
        assertTrue( whiteKnight.controls( b5 ) );
        assertTrue( whiteKnight.controls( c2 ) );
        assertTrue( whiteKnight.controls( c6 ) );
        assertTrue( whiteKnight.controls( e2 ) );
        assertTrue( whiteKnight.controls( e6 ) );
        assertTrue( whiteKnight.controls( f3 ) );
        assertTrue( whiteKnight.controls( f5 ) );
    }

    @Test
    void developedPosition_whiteKnight_pseudoLegalMoves_canJumpButCannotLandOnFriendlyPieces()
    {
        makeMove( g1, f3 );
        makeMove( b8, c6 );
        makeMove( f3, d4 );
        makeMove( g8, f6 );

        PieceHelper whiteKnight = d4.getPieceHelper();

        assertEquals( 6, whiteKnight.getPseudoLegalMoves().size() );

        assertTrue( whiteKnight.canPseudoLegallyMoveTo( b3 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( b5 ) );
        assertFalse( whiteKnight.canPseudoLegallyMoveTo( c2 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( c6 ) );
        assertFalse( whiteKnight.canPseudoLegallyMoveTo( e2 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( e6 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( f3 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( f5 ) );
    }

    // Absolute pins

    @Test
    void developedPosition_pinnedBlackKnight_hasPseudoLegalMovesButNoLegalMoves()
    {
        makeMove( e2, e4 );
        makeMove( d7, d6 );
        makeMove( g1, f3 );
        makeMove( b8, c6 );
        makeMove( f1, b5 );

        PieceHelper blackKnight = c6.getPieceHelper();

        assertEquals( 8, blackKnight.getControlledSquares().size() );

        assertTrue( blackKnight.controls( a7 ) );
        assertTrue( blackKnight.controls( a5 ) );
        assertTrue( blackKnight.controls( b8 ) );
        assertTrue( blackKnight.controls( b4 ) );
        assertTrue( blackKnight.controls( d8 ) );
        assertTrue( blackKnight.controls( d4 ) );
        assertTrue( blackKnight.controls( e7 ) );
        assertTrue( blackKnight.controls( e5 ) );

        assertEquals( 5, blackKnight.getPseudoLegalMoves().size() );

        assertFalse( blackKnight.canPseudoLegallyMoveTo( a7 ) );
        assertTrue( blackKnight.canPseudoLegallyMoveTo( a5 ) );
        assertTrue( blackKnight.canPseudoLegallyMoveTo( b8 ) );
        assertTrue( blackKnight.canPseudoLegallyMoveTo( b4 ) );
        assertFalse( blackKnight.canPseudoLegallyMoveTo( d8 ) );
        assertTrue( blackKnight.canPseudoLegallyMoveTo( d4 ) );
        assertFalse( blackKnight.canPseudoLegallyMoveTo( e7 ) );
        assertTrue( blackKnight.canPseudoLegallyMoveTo( e5 ) );

        assertEquals( 0, blackKnight.getLegalMoves().size() );

        assertFalse( blackKnight.canLegallyMoveTo( a7 ) );
        assertFalse( blackKnight.canLegallyMoveTo( a5 ) );
        assertFalse( blackKnight.canLegallyMoveTo( b8 ) );
        assertFalse( blackKnight.canLegallyMoveTo( b4 ) );
        assertFalse( blackKnight.canLegallyMoveTo( d8 ) );
        assertFalse( blackKnight.canLegallyMoveTo( d4 ) );
        assertFalse( blackKnight.canLegallyMoveTo( e7 ) );
        assertFalse( blackKnight.canLegallyMoveTo( e5 ) );
    }

    @Test
    void developedPosition_pinnedWhiteKnight_hasPseudoLegalMovesButNoLegalMoves()
    {
        makeMove( b1, c3 );
        makeMove( e7, e5 );
        makeMove( d2, d3 );
        makeMove( f8, b4 );

        PieceHelper whiteKnight = c3.getPieceHelper();

        assertEquals( 8, whiteKnight.getControlledSquares().size() );

        assertTrue( whiteKnight.controls( a2 ) );
        assertTrue( whiteKnight.controls( a4 ) );
        assertTrue( whiteKnight.controls( b1 ) );
        assertTrue( whiteKnight.controls( b5 ) );
        assertTrue( whiteKnight.controls( d1 ) );
        assertTrue( whiteKnight.controls( d5 ) );
        assertTrue( whiteKnight.controls( e2 ) );
        assertTrue( whiteKnight.controls( e4 ) );

        assertEquals( 5, whiteKnight.getPseudoLegalMoves().size() );

        assertFalse( whiteKnight.canPseudoLegallyMoveTo( a2 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( a4 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( b1 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( b5 ) );
        assertFalse( whiteKnight.canPseudoLegallyMoveTo( d1 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( d5 ) );
        assertFalse( whiteKnight.canPseudoLegallyMoveTo( e2 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( e4 ) );

        assertEquals( 0, whiteKnight.getLegalMoves().size() );

        assertFalse( whiteKnight.canLegallyMoveTo( a2 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( a4 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( b1 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( b5 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( d1 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( d5 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( e2 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( e4 ) );
    }

    // Check responses

    @Test
    void developedPosition_whiteInCheck_canBlockWithSinglePawnMoveButNotDoublePawnMove()
    {
        makeMove( d2, d4 );
        makeMove( e7, e5 );
        makeMove( e2, e4 );
        makeMove( f8, b4 );

        PieceHelper whitePawn = c2.getPieceHelper();

        assertEquals( 2, whitePawn.getPseudoLegalMoves().size() );

        assertTrue( whitePawn.canPseudoLegallyMoveTo( c3 ) );
        assertTrue( whitePawn.canPseudoLegallyMoveTo( c4 ) );

        assertEquals( 1, whitePawn.getLegalMoves().size() );

        assertTrue( whitePawn.canLegallyMoveTo( c3 ) );
        assertFalse( whitePawn.canLegallyMoveTo( c4 ) );
    }

    @Test
    void developedPosition_whiteInCheck_unrelatedPseudoLegalKnightMovesAreNotLegal()
    {
        makeMove( d2, d4 );
        makeMove( e7, e5 );
        makeMove( e2, e4 );
        makeMove( f8, b4 );

        PieceHelper whiteKnight = g1.getPieceHelper();

        assertEquals( 3, whiteKnight.getControlledSquares().size() );

        assertTrue( whiteKnight.controls( e2 ) );
        assertTrue( whiteKnight.controls( f3 ) );
        assertTrue( whiteKnight.controls( h3 ) );

        assertEquals( 3, whiteKnight.getPseudoLegalMoves().size() );

        assertTrue( whiteKnight.canPseudoLegallyMoveTo( e2 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( f3 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( h3 ) );

        assertEquals( 0, whiteKnight.getLegalMoves().size() );

        assertFalse( whiteKnight.canLegallyMoveTo( e2 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( f3 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( h3 ) );
    }

    @Test
    void developedPosition_whiteInCheck_unrelatedQueensideKnightMovesAreNotLegal()
    {
        makeMove( f2, f3 );
        makeMove( e7, e5 );
        makeMove( g2, g4 );
        makeMove( d8, h4 );

        PieceHelper whiteKnight = b1.getPieceHelper();

        assertEquals( 3, whiteKnight.getControlledSquares().size() );

        assertTrue( whiteKnight.controls( a3 ) );
        assertTrue( whiteKnight.controls( c3 ) );
        assertTrue( whiteKnight.controls( d2 ) );

        assertEquals( 2, whiteKnight.getPseudoLegalMoves().size() );

        assertTrue( whiteKnight.canPseudoLegallyMoveTo( a3 ) );
        assertTrue( whiteKnight.canPseudoLegallyMoveTo( c3 ) );
        assertFalse( whiteKnight.canPseudoLegallyMoveTo( d2 ) );

        assertEquals( 0, whiteKnight.getLegalMoves().size() );

        assertFalse( whiteKnight.canLegallyMoveTo( a3 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( c3 ) );
        assertFalse( whiteKnight.canLegallyMoveTo( d2 ) );
    }

    // En passant

    @Test
    void developedPosition_whitePawn_enPassantCaptureEscapesCheck()
    {
        makeMove( e2, e4 );
        makeMove( e7, e6 );
        makeMove( e4, e5 );
        makeMove( a7, a6 );
        makeMove( e1, e2 );
        makeMove( a6, a5 );
        makeMove( e2, e3 );
        makeMove( h7, h6 );
        makeMove( e3, e4 );
        makeMove( f7, f5 );

        PieceHelper whitePawn = e5.getPieceHelper();

        assertEquals( 1, whitePawn.getPseudoLegalMoves().size() );

        assertFalse( whitePawn.canPseudoLegallyMoveTo( e6 ) );
        assertTrue( whitePawn.canPseudoLegallyMoveTo( f6 ) );

        assertEquals( 1, whitePawn.getLegalMoves().size() );

        assertFalse( whitePawn.canLegallyMoveTo( e6 ) );
        assertTrue( whitePawn.canLegallyMoveTo( f6 ) );
    }

    @Test
    void developedPosition_whitePawn_enPassantCaptureIsNotLegalAfterOneMoveDelay()
    {
        makeMove( e2, e4 );
        makeMove( a7, a6 );
        makeMove( e4, e5 );
        makeMove( d7, d5 );
        makeMove( g1, f3 );
        makeMove( g8, f6 );

        PieceHelper whitePawn = e5.getPieceHelper();

        assertFalse( whitePawn.canPseudoLegallyMoveTo( d6 ) );

        assertFalse( whitePawn.canLegallyMoveTo( d6 ) );
    }

    @Test
    void developedPosition_whitePawn_enPassantCaptureIsIllegalWhenItExposesKingToRook()
    {
        makeMove( e2, e3 );
        makeMove( a7, a5 );
        makeMove( f2, f4 );
        makeMove( a5, a4 );
        makeMove( d2, d4 );
        makeMove( a8, a5 );
        makeMove( d4, d5 );
        makeMove( h7, h6 );
        makeMove( e1, f2 );
        makeMove( g7, g6 );
        makeMove( f2, f3 );
        makeMove( g6, g5 );
        makeMove( f3, e4 );
        makeMove( h6, h5 );
        makeMove( e4, e5 );
        makeMove( c7, c5 );

        PieceHelper whitePawn = d5.getPieceHelper();

        assertEquals( 2, whitePawn.getPseudoLegalMoves().size() );

        assertTrue( whitePawn.canPseudoLegallyMoveTo( c6 ) );
        assertTrue( whitePawn.canPseudoLegallyMoveTo( d6 ) );

        assertEquals( 1, whitePawn.getLegalMoves().size() );

        assertFalse( whitePawn.canLegallyMoveTo( c6 ) );
        assertTrue( whitePawn.canLegallyMoveTo( d6 ) );
    }

    // Castling

    @Test
    void developedPosition_whiteKing_canCastleKingsideWhenPathIsClearAndSafe()
    {
        makeMove( g1, f3 );
        makeMove( g8, f6 );
        makeMove( g2, g3 );
        makeMove( g7, g6 );
        makeMove( f1, g2 );
        makeMove( f8, g7 );

        PieceHelper whiteKing = e1.getPieceHelper();

        assertTrue( whiteKing.canPseudoLegallyMoveTo( g1 ) );

        assertTrue( whiteKing.canLegallyMoveTo( g1 ) );
    }

    @Test
    void developedPosition_whiteKing_cannotCastleKingsideThroughControlledSquare()
    {
        makeMove( e2, e4 );
        makeMove( b7, b6 );
        makeMove( g1, f3 );
        makeMove( c8, a6 );
        makeMove( g2, g3 );
        makeMove( g8, f6 );
        makeMove( f1, g2 );
        makeMove( h7, h6 );

        PieceHelper whiteKing = e1.getPieceHelper();

        assertTrue( whiteKing.canPseudoLegallyMoveTo( g1 ) );

        assertFalse( whiteKing.canLegallyMoveTo( g1 ) );
    }

    @Test
    void developedPosition_whiteKing_cannotCastleKingsideWhileInCheck()
    {
        makeMove( d2, d3 );
        makeMove( e7, e5 );
        makeMove( g1, f3 );
        makeMove( g8, f6 );
        makeMove( g2, g3 );
        makeMove( a7, a6 );
        makeMove( f1, g2 );
        makeMove( f8, b4 );

        PieceHelper whiteKing = e1.getPieceHelper();

        assertTrue( whiteKing.canPseudoLegallyMoveTo( g1 ) );

        assertFalse( whiteKing.canLegallyMoveTo( g1 ) );
    }

    // King safety

    @Test
    void developedPosition_whiteKing_cannotCaptureProtectedCheckingPiece()
    {
        makeMove( d2, d3 );
        makeMove( a7, a5 );
        makeMove( e1, d2 );
        makeMove( e7, e5 );
        makeMove( d2, c3 );
        makeMove( f8, b4 );

        PieceHelper whiteKing = c3.getPieceHelper();

        assertTrue( whiteKing.canPseudoLegallyMoveTo( b4 ) );

        assertFalse( whiteKing.canLegallyMoveTo( b4 ) );
    }
}
