package io.github.ollybishop.bishopsgambit.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.border.Border;

import io.github.ollybishop.bishopsgambit.game.Game;
import io.github.ollybishop.bishopsgambit.io.Images;
import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;

public class ApplicationFrame extends JFrame
{
    // =========================================================================================
    // Cursors
    // =========================================================================================

    private static final Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor();
    private static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor( Cursor.HAND_CURSOR );

    // =========================================================================================
    // Components and Layouts
    // =========================================================================================

    /**
     * The frame's main content panel.
     * <p>
     * This is the topmost layout container within the frame and acts as the root container for the application's UI components.
     */
    private final JPanel contentPane = new JPanel();
    private final BorderLayout contentLayout = new BorderLayout();

    /**
     * The menu panel displayed above the tabletop.
     * <p>
     * This panel uses a simple flow layout and contains the 'New Game', 'Flip View' and 'Lock View' buttons.
     */
    private final JPanel topMenuPane = new JPanel();

    private final JButton newGameButton = new JButton( "New Game" );
    private final JButton flipViewButton = new JButton( "Flip View" );
    private final JToggleButton lockViewButton = new JToggleButton( "Lock View" );

    /**
     * The menu panel displayed below the tabletop.
     * <p>
     * This panel uses a simple flow layout and contains the 'Previous Move' and 'Next Move' buttons.
     */
    private final JPanel bottomMenuPane = new JPanel();

    private final JButton previousMoveButton = new JButton( "Previous Move" );
    private final JButton nextMoveButton = new JButton( "Next Move" );

    /**
     * The main game area containing the chessboard and both captured-piece panels.
     * <p>
     * Separate layouts are used for the White and Black board orientations.
     */
    private final JPanel tabletopPane = new JPanel();
    private final BorderLayout tabletopLayoutWhite = new BorderLayout();
    private final BorderLayout tabletopLayoutBlack = new BorderLayout();

    private final JPanel chessboardPane = new JPanel();
    private final GridBagLayout chessboardLayoutWhite = new GridBagLayout();
    private final GridBagLayout chessboardLayoutBlack = new GridBagLayout();

    private final JPanel capturedPiecesPaneWhite = new SortedJPanel();
    private final JPanel capturedPiecesPaneBlack = new SortedJPanel();
    private final FlowLayout capturedPiecesLayoutWhite = new FlowLayout();
    private final FlowLayout capturedPiecesLayoutBlack = new FlowLayout();

    /**
     * The UI components representing the squares on the chessboard.
     */
    private final List<SquareComponent> squareComponents = createSquareComponents();

    /**
     * The UI components representing the pieces in the current game.
     */
    private final List<PieceComponent> pieceComponents = new ArrayList<>();

    // =========================================================================================
    // Component State
    // =========================================================================================

    private SquareComponent fromSquare;
    private SquareComponent toSquare;

    private SquareComponent checkSquare;

    // =========================================================================================
    // Game Context
    // =========================================================================================

    private Game game;

    /**
     * The index of the board state currently displayed in the UI.
     * <p>
     * A value of {@code 0} represents the initial board setup; subsequent values represent the board state after each
     * successive ply (half-move) has been played.
     */
    private int boardIndex;

    // =========================================================================================
    // Factory Methods
    // =========================================================================================

    private static List<SquareComponent> createSquareComponents()
    {
        List<SquareComponent> squareComponents = new ArrayList<>();

        for ( char file = 'a'; file <= 'h'; file++ )
            for ( char rank = '1'; rank <= '8'; rank++ )
                squareComponents.add( new SquareComponent( file, rank ) );

        return Collections.unmodifiableList( squareComponents );
    }

    // =========================================================================================
    // Package-Private Accessors
    // =========================================================================================

    JPanel getChessboardPane()
    {
        return chessboardPane;
    }

    List<SquareComponent> getSquareComponents()
    {
        return squareComponents;
    }

    SquareComponent getFromSquare()
    {
        return fromSquare;
    }

    SquareComponent getToSquare()
    {
        return toSquare;
    }

    // =========================================================================================
    // Model/UI Lookup Methods
    // =========================================================================================

    private Square getSquare( SquareComponent squareComponent )
    {
        return getActiveBoard().get( squareComponent.getIndex() );
    }

    private SquareComponent getSquareComponent( Square square )
    {
        return squareComponents.get( square.getIndex() );
    }

    private SquareComponent getSquareComponent( Board board, Piece piece )
    {
        Square square = piece.getSquare( board );

        if ( square == null )
            return null;

        return getSquareComponent( square );
    }

    // =========================================================================================
    // Game Delegates
    // =========================================================================================

    private int getLatestBoardIndex()
    {
        return game.getNumberOfPliesPlayed();
    }

    private Board getBoard( int index )
    {
        return game.getBoard( index );
    }

    private Board getActiveBoard()
    {
        return game.getActiveBoard();
    }

    private Player getActivePlayer()
    {
        return game.getActivePlayer();
    }

    // =========================================================================================
    // Frame Setup
    // =========================================================================================

    public ApplicationFrame()
    {
        // Give the contentPane initial focus to prevent other components from receiving focus on launch
        setFocusTraversalPolicy( new LayoutFocusTraversalPolicy()
        {
            @Override
            public Component getInitialComponent( Window window )
            {
                return contentPane;
            }
        } );

        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 640, 960 );

        addComponentsToFrame();
        configureLayouts();

        addChessboardMouseListener();
        addMenuButtonActionListeners();
        registerMoveConfirmationInput();
        addFrameResizedListener();

        newGame();
    }

    private void addComponentsToFrame()
    {
        add( contentPane );

        contentPane.add( tabletopPane );
        contentPane.add( topMenuPane );
        contentPane.add( bottomMenuPane );

        topMenuPane.add( newGameButton );
        topMenuPane.add( flipViewButton );
        topMenuPane.add( lockViewButton );

        bottomMenuPane.add( previousMoveButton );
        bottomMenuPane.add( nextMoveButton );

        tabletopPane.add( chessboardPane );
        tabletopPane.add( capturedPiecesPaneWhite );
        tabletopPane.add( capturedPiecesPaneBlack );

        for ( SquareComponent squareComponent : squareComponents )
            chessboardPane.add( squareComponent );
    }

    private void configureLayouts()
    {
        contentLayout.addLayoutComponent( tabletopPane, BorderLayout.CENTER );
        contentLayout.addLayoutComponent( topMenuPane, BorderLayout.NORTH );
        contentLayout.addLayoutComponent( bottomMenuPane, BorderLayout.SOUTH );

        tabletopLayoutWhite.addLayoutComponent( chessboardPane, BorderLayout.CENTER );
        tabletopLayoutWhite.addLayoutComponent( capturedPiecesPaneWhite, BorderLayout.NORTH );
        tabletopLayoutWhite.addLayoutComponent( capturedPiecesPaneBlack, BorderLayout.SOUTH );

        tabletopLayoutBlack.addLayoutComponent( chessboardPane, BorderLayout.CENTER );
        tabletopLayoutBlack.addLayoutComponent( capturedPiecesPaneWhite, BorderLayout.SOUTH );
        tabletopLayoutBlack.addLayoutComponent( capturedPiecesPaneBlack, BorderLayout.NORTH );

        for ( SquareComponent squareComponent : squareComponents )
        {
            GridBagConstraints chessboardConstraintsWhite = new GridBagConstraints();
            GridBagConstraints chessboardConstraintsBlack = new GridBagConstraints();

            char file = squareComponent.getFile();
            char rank = squareComponent.getRank();

            chessboardConstraintsWhite.gridx = file - 'a';
            chessboardConstraintsWhite.gridy = '8' - rank;

            chessboardConstraintsBlack.gridx = 'h' - file;
            chessboardConstraintsBlack.gridy = rank - '1';

            chessboardLayoutWhite.setConstraints( squareComponent, chessboardConstraintsWhite );
            chessboardLayoutBlack.setConstraints( squareComponent, chessboardConstraintsBlack );
        }

        contentPane.setLayout( contentLayout );

        capturedPiecesPaneWhite.setLayout( capturedPiecesLayoutWhite );
        capturedPiecesPaneBlack.setLayout( capturedPiecesLayoutBlack );
    }

    private void addChessboardMouseListener()
    {
        chessboardPane.addMouseListener( new MouseAdapter()
        {
            /**
             * The square that was most recently pressed by the user.
             */
            SquareComponent pressedSquare;

            /**
             * Indicates whether the most recently pressed square was already selected before the press.
             * <p>
             * Used to support click-to-toggle selection without interfering with drag-and-drop behaviour. Also used to deselect
             * the {@code from} square when a move preview is cancelled by dragging the piece back onto its original square.
             */
            boolean pressedSquareWasPreselected;

            @Override
            public void mousePressed( MouseEvent e )
            {
                if ( boardIndex < getLatestBoardIndex() )
                    return;

                Component comp = chessboardPane.getComponentAt( e.getPoint() );

                if ( comp instanceof SquareComponent )
                {
                    pressedSquare = (SquareComponent) comp;

                    pressedSquareWasPreselected = pressedSquare == fromSquare || pressedSquare == toSquare;

                    boolean ownPieceWasSelected = getSquare( pressedSquare ).isOccupiedBy( getActivePlayer() );

                    // No squares were preselected
                    if ( fromSquare == null )
                    {
                        if ( ownPieceWasSelected )
                            selectFromSquare( pressedSquare );
                    }
                    // Only a 'from' square was preselected
                    else if ( toSquare == null )
                    {
                        if ( pressedSquare != fromSquare )
                            if ( pressedSquare.hasMoveMarker() )
                                selectToSquare( pressedSquare );
                            else
                            {
                                deselectFromSquare();

                                if ( ownPieceWasSelected )
                                    selectFromSquare( pressedSquare );
                            }
                    }
                    // Both 'from' and 'to' squares were preselected
                    else
                    {
                        if ( pressedSquare != toSquare )
                            if ( pressedSquare != fromSquare && ownPieceWasSelected )
                            {
                                deselectFromSquare();
                                selectFromSquare( pressedSquare );
                            }
                            else
                                deselectFromSquare();

                        deselectToSquare();
                    }

                    afterMouseEvent();
                }
            }

            @Override
            public void mouseReleased( MouseEvent e )
            {
                if ( boardIndex < getLatestBoardIndex() )
                    return;

                // No squares were preselected
                if ( fromSquare == null )
                    return;

                Component comp = chessboardPane.getComponentAt( e.getPoint() );

                if ( comp instanceof SquareComponent )
                {
                    SquareComponent releasedSquare = (SquareComponent) comp;

                    // Only a 'from' square was preselected
                    if ( toSquare == null )
                    {
                        if ( releasedSquare == fromSquare )
                        {
                            if ( pressedSquareWasPreselected )
                                deselectFromSquare();
                        }
                        else if ( releasedSquare != pressedSquare )
                            if ( releasedSquare.hasMoveMarker() )
                                selectToSquare( releasedSquare );
                            else
                                deselectFromSquare();
                    }
                    // Both 'from' and 'to' squares were preselected
                    else
                    {
                        if ( releasedSquare != toSquare )
                            deselectFromAndToSquares();
                    }
                }
                // The mouse was released outside the board
                else
                {
                    deselectFromAndToSquares();
                }

                afterMouseEvent();
            }

            private void afterMouseEvent()
            {
                updateMoveMarkers();
                updateBoardState();
                updateCheckBorder();
            }
        } );
    }

    private void selectFromSquare( SquareComponent squareComponent )
    {
        fromSquare = squareComponent;
        fromSquare.select();
    }

    private void selectToSquare( SquareComponent squareComponent )
    {
        toSquare = squareComponent;
        toSquare.select();
    }

    private void deselectFromSquare()
    {
        if ( fromSquare != null )
        {
            fromSquare.deselect();
            fromSquare = null;
        }
    }

    private void deselectToSquare()
    {
        if ( toSquare != null )
        {
            toSquare.deselect();
            toSquare = null;
        }
    }

    private void deselectFromAndToSquares()
    {
        deselectFromSquare();
        deselectToSquare();
    }

    private void updateMoveMarkers()
    {
        for ( SquareComponent squareComponent : squareComponents )
            squareComponent.showMoveMarker( false );

        if ( fromSquare != null && toSquare == null )
        {
            for ( Square square : getSquare( fromSquare ).getPiece().getLegalMoves( getActiveBoard() ) )
                getSquareComponent( square ).showMoveMarker( true );
        }
    }

    private void addMenuButtonActionListeners()
    {
        newGameButton.addActionListener( e ->
        {
            int option = JOptionPane.showConfirmDialog(
                rootPane,
                "Are you sure you want to start a new game?",
                "New Game",
                JOptionPane.YES_NO_OPTION );

            if ( option == JOptionPane.YES_OPTION )
                newGame();
        } );

        flipViewButton.addActionListener( e ->
        {
            if ( tabletopPane.getLayout() == tabletopLayoutWhite )
                orientTabletop( Player.Colour.BLACK );
            else
                orientTabletop( Player.Colour.WHITE );
        } );

        previousMoveButton.addActionListener( e ->
        {
            deselectFromAndToSquares();

            boardIndex--;
            updateBoardState();
            updatePieceCursors();
        } );

        nextMoveButton.addActionListener( e ->
        {
            boardIndex++;
            updateBoardState();
            updatePieceCursors();
        } );
    }

    private void registerMoveConfirmationInput()
    {
        InputMap inputMap = contentPane.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW );
        ActionMap actionMap = contentPane.getActionMap();

        inputMap.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "moveConfirmedAction" );

        actionMap.put( "moveConfirmedAction", new AbstractAction()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                if ( toSquare == null )
                    return;

                makeMove();

                updateCheckSquare();
                updatePieceCursors();
                orientTabletop();

                checkIfGameIsOver();
            }
        } );
    }

    private void addFrameResizedListener()
    {
        addComponentListener( new ComponentAdapter()
        {
            /**
             * Called once after the frame is initialised, and again whenever the frame is resized.
             */
            @Override
            public void componentResized( ComponentEvent e )
            {
                rescalePieceContainers();
                updateCheckBorder();
            }
        } );
    }

    void newGame()
    {
        deselectFromAndToSquares();

        if ( checkSquare != null )
        {
            checkSquare.resetBorder();
            checkSquare = null;
        }

        // Remove all existing pieces from the UI
        for ( PieceComponent pieceComponent : pieceComponents )
        {
            Container parent = pieceComponent.getParent();

            if ( parent != null )
                parent.remove( pieceComponent );
        }

        pieceComponents.clear();

        boardIndex = 0;

        game = new Game();

        for ( Piece piece : getActiveBoard().getPieces() )
            createPieceComponent( piece );

        updateBoardState();
        updatePieceCursors();
        orientTabletop();
    }

    private void createPieceComponent( Piece piece )
    {
        pieceComponents.add( new PieceComponent( piece ) );
    }

    // =========================================================================================
    // Methods for handling changes to the underlying game state
    // =========================================================================================

    private void makeMove()
    {
        Square from = getSquare( fromSquare );
        Square to = getSquare( toSquare );

        deselectFromAndToSquares();

        if ( from.getPiece().canPromote( to ) )
        {
            int i = JOptionPane.showOptionDialog(
                rootPane,
                "Select a piece to promote to.",
                "Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                Images.createIcon( getActivePlayer().getColour(), Piece.Type.PAWN ),
                Piece.Type.PROMOTION_TYPES,
                null );

            Piece.Type newType = i == JOptionPane.CLOSED_OPTION
                ? Piece.Type.QUEEN
                : Piece.Type.PROMOTION_TYPES[ i ];

            Piece newPiece = game.makeMove( from, to, newType );

            createPieceComponent( newPiece );
        }
        else
        {
            game.makeMove( from, to, null );
        }

        boardIndex = getLatestBoardIndex();

        // TODO: This can be removed once promoted pieces are included in move previews
        updateBoardState();
    }

    private void checkIfGameIsOver()
    {
        if ( game.isGameOver() )
        {
            Icon icon;

            if ( game.getStatus() == Game.Status.CHECKMATE )
                icon = Images.createIcon( getActivePlayer().getColour().opposite(), Piece.Type.KING );
            else
                icon = null;

            JOptionPane.showMessageDialog( this, game.getGameOverMessage(), "Game Over", JOptionPane.PLAIN_MESSAGE, icon );
        }
    }

    // =========================================================================================
    // Methods for managing the position, size and visibility of UI components
    // =========================================================================================

    private void updateBoardState()
    {
        Board board;

        if ( toSquare == null )
            board = getBoard( boardIndex );
        else
            board = getActiveBoard().cloneAndMove( getSquare( fromSquare ), getSquare( toSquare ) );

        for ( PieceComponent pieceComponent : pieceComponents )
        {
            SquareComponent squareComponent = getSquareComponent( board, pieceComponent.getPiece() );

            // The piece is no longer on the board, so ensure it is in the appropriate captured pieces pane
            if ( squareComponent == null )
            {
                switch ( pieceComponent.getPiece().getColour() )
                {
                    case WHITE:
                        capturedPiecesPaneWhite.add( pieceComponent );
                        break;

                    case BLACK:
                        capturedPiecesPaneBlack.add( pieceComponent );
                        break;
                }
            }
            else
            {
                squareComponent.addPieceComponent( pieceComponent );
            }
        }

        rescalePieces();

        // Repaint the tabletop pane to clear artifacts left by relocated pieces
        tabletopPane.repaint();

        previousMoveButton.setEnabled( boardIndex > 0 );
        nextMoveButton.setEnabled( boardIndex < getLatestBoardIndex() );

        // Check for layering issues caused by incorrect component indexing
        for ( SquareComponent squareComponent : squareComponents )
            squareComponent.checkForLayeringIssues();
    }

    private void rescalePieceContainers()
    {
        int scale = getUiScale();

        for ( SquareComponent squareComponent : squareComponents )
            squareComponent.setScale( scale );

        Dimension dimension = new Dimension( scale * 8, scale );

        capturedPiecesPaneWhite.setPreferredSize( dimension );
        capturedPiecesPaneBlack.setPreferredSize( dimension );

        /*
         * FlowLayout does not support vertical alignment of its components. To centre them vertically, set the vertical gap to
         * half the unused vertical space in the pane:
         * 
         * vgap = (pane height - piece height) / 2 = (scale - scale * 3 / 5) / 2 = scale / 5
         */
        int gap = scale / 5;

        capturedPiecesLayoutWhite.setVgap( gap );
        capturedPiecesLayoutBlack.setVgap( gap );

        // Horizontal space between pieces
        capturedPiecesLayoutWhite.setHgap( -gap );
        capturedPiecesLayoutBlack.setHgap( -gap );

        rescalePieces();
    }

    private void rescalePieces()
    {
        int scale = getUiScale();

        for ( PieceComponent pieceComponent : pieceComponents )
        {
            if ( pieceComponent.getParent() instanceof SquareComponent )
                pieceComponent.setScale( scale );
            else
                pieceComponent.setScale( scale * 3 / 5 );
        }
    }

    private int getUiScale()
    {
        int min = Math.min( contentPane.getWidth(), contentPane.getHeight() );
        return Math.max( 10, min / 8 );
    }

    private void updateCheckSquare()
    {
        if ( game.getStatus() == Game.Status.CHECK ||
             game.getStatus() == Game.Status.CHECKMATE )
            checkSquare = getSquareComponent( getActiveBoard(), getActivePlayer().getKing() );
        else
            checkSquare = null;

        updateCheckBorder();
    }

    private void updateCheckBorder()
    {
        if ( checkSquare == null )
            return;

        if ( toSquare == null )
        {
            int thickness = Math.max( 1, checkSquare.getWidth() / 20 );
            Border border = BorderFactory.createLineBorder( Color.RED, thickness );
            checkSquare.setBorder( border );
        }
        else
        {
            checkSquare.resetBorder();
        }
    }

    /**
     * Should be called whenever any of the following occurs:
     * <ul>
     * <li>a new game is started,</li>
     * <li>the active player changes after a move is made,</li>
     * <li>the 'Previous Move' button is clicked, or</li>
     * <li>the 'Next Move' button is clicked.</li>
     * </ul>
     */
    private void updatePieceCursors()
    {
        for ( PieceComponent pieceComponent : pieceComponents )
        {
            if ( boardIndex == getLatestBoardIndex() &&
                 pieceComponent.getPiece().getPlayer() == getActivePlayer() )
                pieceComponent.setCursor( HAND_CURSOR );
            else
                pieceComponent.setCursor( DEFAULT_CURSOR );
        }
    }

    /**
     * Orients the tabletop from the active player's perspective, provided the 'Lock View' button is not selected.
     */
    private void orientTabletop()
    {
        if ( !lockViewButton.isSelected() )
            orientTabletop( getActivePlayer().getColour() );
    }

    /**
     * Orients the tabletop from the perspective of the player with the given colour. For example, if {@code colour} is
     * {@link Player.Colour#WHITE}, the board is oriented with 'a1' in the lower-left corner and Black's captured pieces
     * displayed below.
     * 
     * @param colour the colour of the player whose perspective is used
     */
    private void orientTabletop( Player.Colour colour )
    {
        switch ( colour )
        {
            case WHITE:
                tabletopPane.setLayout( tabletopLayoutWhite );
                chessboardPane.setLayout( chessboardLayoutWhite );

                for ( SquareComponent squareComponent : squareComponents )
                {
                    squareComponent.showRank( 'a' );
                    squareComponent.showFile( '1' );
                }

                break;

            case BLACK:
                tabletopPane.setLayout( tabletopLayoutBlack );
                chessboardPane.setLayout( chessboardLayoutBlack );

                for ( SquareComponent squareComponent : squareComponents )
                {
                    squareComponent.showRank( 'h' );
                    squareComponent.showFile( '8' );
                }

                break;
        }
    }
}
