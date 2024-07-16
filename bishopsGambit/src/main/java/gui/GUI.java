package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.java.board.Board;
import main.java.board.Square;
import main.java.game.Game;
import main.java.io.Images;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.player.Player;

public class GUI extends JFrame
{
    // ============================================================================================
    // Instance Variables
    // ============================================================================================

    private final JPanel contentPane = new JPanel();
    private final BorderLayout contentLayoutWhite = new BorderLayout();
    private final BorderLayout contentLayoutBlack = new BorderLayout();

    private final JPanel chessboardPane = new JPanel();
    private final GridBagLayout chessboardLayoutWhite = new GridBagLayout();
    private final GridBagLayout chessboardLayoutBlack = new GridBagLayout();

    private final JPanel capturedPiecesPaneWhite = new OrderedJPanel();
    private final JPanel capturedPiecesPaneBlack = new OrderedJPanel();
    private final FlowLayout capturedPiecesLayoutWhite = new FlowLayout();
    private final FlowLayout capturedPiecesLayoutBlack = new FlowLayout();

    private final List<SquareComp> squareComps = new ArrayList<>();
    private final List<PieceComp> pieceComps = new ArrayList<>();

    private SquareComp from;
    private SquareComp to;
    private SquareComp check;

    private Game game;

    // ============================================================================================
    // Getters and Setters
    // ============================================================================================

    private Game getGame()
    {
        return this.game;
    }

    private void setGame( Game game )
    {
        this.game = game;
    }

    // ============================================================================================
    // Map Methods
    // ============================================================================================

    private Square getSquare( SquareComp squareComp )
    {
        return getBoard().get( squareComp.getIndex() );
    }

    private SquareComp getSquareComp( Square square )
    {
        return squareComps.get( square.getIndex() );
    }

    private SquareComp getSquareComp( Board board, Piece piece )
    {
        Square square = piece.getSquare( board );

        if ( square != null )
            return getSquareComp( square );

        return null;
    }

    // ============================================================================================
    // Derived Methods
    // ============================================================================================

    private Board getBoard()
    {
        return getGame().getBoard();
    }

    /**
     * Returns the player whose turn it currently is, based on the number of turns taken.
     * 
     * @return White if the number of turns taken is even; Black if it is odd
     */
    private Player getActivePlayer()
    {
        return getGame().getActivePlayer();
    }

    /**
     * Returns the opponent of the player whose turn it currently is, based on the number of turns
     * taken.
     * 
     * @return Black if the number of turns taken is even; White if it is odd
     */
    private Player getInactivePlayer()
    {
        return getGame().getInactivePlayer();
    }

    private List<Square> getMoves( SquareComp squareComp )
    {
        return getSquare( squareComp ).getPiece().getMoves( getBoard() );
    }

    // ============================================================================================
    // Constructor and Associated Methods
    // ============================================================================================

    public GUI( Game game )
    {
        setGame( game );

        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 640, 960 );

        initialiseComponents();
        addComponentsToFrame();
        createLayouts();

        addSquareClickedListeners();
        addKeyPressedListener();
        addFrameResizedListener();

        orientBoard();
        positionPieces();
    }

    private void initialiseComponents()
    {
        for ( Square square : getBoard() )
            squareComps.add( new SquareComp( square ) );

        for ( Piece piece : getBoard().getPieces() )
            createPieceComp( piece );
    }

    private void createPieceComp( Piece piece )
    {
        pieceComps.add( new PieceComp( piece ) );
    }

    private void addComponentsToFrame()
    {
        add( contentPane );
        contentPane.add( chessboardPane );
        contentPane.add( capturedPiecesPaneWhite );
        contentPane.add( capturedPiecesPaneBlack );

        for ( SquareComp squareComp : squareComps )
            chessboardPane.add( squareComp );
    }

    private void createLayouts()
    {
        contentLayoutWhite.addLayoutComponent( chessboardPane, BorderLayout.CENTER );
        contentLayoutWhite.addLayoutComponent( capturedPiecesPaneWhite, BorderLayout.NORTH );
        contentLayoutWhite.addLayoutComponent( capturedPiecesPaneBlack, BorderLayout.SOUTH );

        contentLayoutBlack.addLayoutComponent( chessboardPane, BorderLayout.CENTER );
        contentLayoutBlack.addLayoutComponent( capturedPiecesPaneWhite, BorderLayout.SOUTH );
        contentLayoutBlack.addLayoutComponent( capturedPiecesPaneBlack, BorderLayout.NORTH );

        for ( SquareComp squareComp : squareComps )
        {
            GridBagConstraints chessboardConstraintsWhite = new GridBagConstraints();
            GridBagConstraints chessboardConstraintsBlack = new GridBagConstraints();

            char file = squareComp.getFile();
            char rank = squareComp.getRank();

            chessboardConstraintsWhite.gridx = file - 'a';
            chessboardConstraintsWhite.gridy = '8' - rank;

            chessboardConstraintsBlack.gridx = 'h' - file;
            chessboardConstraintsBlack.gridy = rank - '1';

            chessboardLayoutWhite.setConstraints( squareComp, chessboardConstraintsWhite );
            chessboardLayoutBlack.setConstraints( squareComp, chessboardConstraintsBlack );
        }

        capturedPiecesPaneWhite.setLayout( capturedPiecesLayoutWhite );
        capturedPiecesPaneBlack.setLayout( capturedPiecesLayoutBlack );
    }

    private void addSquareClickedListeners()
    {
        for ( SquareComp squareComp : squareComps )
        {
            squareComp.addMouseListener( new MouseAdapter()
            {
                @Override
                public void mouseReleased( MouseEvent e )
                {
                    if ( squareComp.contains( e.getPoint() ) )
                        doClickAction( squareComp );
                }
            } );
        }
    }

    private void addKeyPressedListener()
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( new KeyEventDispatcher()
        {
            @Override
            public boolean dispatchKeyEvent( KeyEvent e )
            {
                if ( e.getKeyCode() == KeyEvent.VK_SPACE &&
                     e.getID() == KeyEvent.KEY_RELEASED &&
                     from != null &&
                     to != null )
                {
                    makeMove();
                    postMove();
                    orientBoard();
                    updateCheckBorder();
                }

                return true;
            }
        } );
    }

    private void addFrameResizedListener()
    {
        addComponentListener( new ComponentAdapter()
        {
            /**
             * This method is called once when the frame is initialised (after the constructor) and each
             * time the frame is resized thereafter.
             */
            @Override
            public void componentResized( ComponentEvent e )
            {
                updateScale();
                updateCheckBorder();
            }
        } );
    }

    // ============================================================================================
    // Methods
    // ============================================================================================

    private void doClickAction( SquareComp squareComp )
    {
        if ( from != null && to == null )
            for ( SquareComp sqComp : squareComps )
                sqComp.showCircle( false );

        Square square = getSquare( squareComp );

        boolean deselectFrom = false;
        boolean deselectTo = true;
        boolean selectFrom = false;
        boolean selectTo = false;

        if ( square.isOccupiedBy( getActivePlayer() ) )
        {
            deselectFrom = true;

            if ( from != squareComp )
                selectFrom = true;
        }
        else if ( to != null )
        {
            if ( to != squareComp )
                deselectFrom = true;
        }
        else if ( from != null )
        {
            if ( getMoves( from ).contains( square ) )
                selectTo = true;
            else
                deselectFrom = true;
        }

        SquareComp toBefore = to;

        if ( deselectFrom && from != null )
            from = from.deselect();

        if ( deselectTo && to != null )
            to = to.deselect();

        if ( selectFrom )
            from = squareComp.select();

        if ( selectTo )
            to = squareComp.select();

        // If 'to' was selected (or unselected), preview the selected move (or undo the preview)
        if ( to != toBefore )
        {
            positionPieces();
            updateCheckBorder();
        }

        if ( from != null && to == null )
            for ( Square sq : getMoves( from ) )
                getSquareComp( sq ).showCircle( true );
    }

    private void orientBoard()
    {
        switch ( getActivePlayer().getColour() )
        {
            case WHITE:
                contentPane.setLayout( contentLayoutWhite );
                chessboardPane.setLayout( chessboardLayoutWhite );

                for ( SquareComp squareComp : squareComps )
                {
                    squareComp.showRank( 'a' );
                    squareComp.showFile( '1' );
                }

                break;

            case BLACK:
                contentPane.setLayout( contentLayoutBlack );
                chessboardPane.setLayout( chessboardLayoutBlack );

                for ( SquareComp squareComp : squareComps )
                {
                    squareComp.showRank( 'h' );
                    squareComp.showFile( '8' );
                }

                break;
        }
    }

    private void makeMove()
    {
        Square fromSquare = getSquare( from );
        Square toSquare = getSquare( to );

        Typ promType = null;

        if ( fromSquare.getPiece().canPromote( getBoard(), toSquare ) )
        {
            Typ[] options = new Typ[] { Typ.KNIGHT, Typ.BISHOP, Typ.ROOK, Typ.QUEEN };

            int i = JOptionPane.showOptionDialog( rootPane,
                                                  "Select a piece to promote to.",
                                                  "Promotion",
                                                  JOptionPane.DEFAULT_OPTION,
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  Images.getIcon( getActivePlayer().getColour(), Typ.PAWN ),
                                                  options,
                                                  null );

            if ( i == JOptionPane.CLOSED_OPTION )
                promType = Typ.QUEEN;
            else
                promType = options[ i ];
        }

        Piece promPiece = getGame().move( fromSquare, toSquare, promType );

        if ( promPiece != null )
            createPieceComp( promPiece );

        from = from.deselect();
        to = to.deselect();

        // TODO: Can remove this once promotion previews are implemented
        positionPieces();
    }

    private void postMove()
    {
        Board board = getBoard();
        Player activePlayer = getActivePlayer();

        boolean activePlayerIsInCheck = activePlayer.isInCheck( board );

        if ( activePlayerIsInCheck )
            check = getSquareComp( board, activePlayer.getKing() );
        else
            check = null;

        if ( activePlayer.hasNoLegalMoves( board ) )
        {
            String message;
            Icon icon;

            if ( activePlayerIsInCheck )
            {
                Player inactivePlayer = getInactivePlayer();

                message = inactivePlayer + " wins by checkmate.";
                icon = Images.getIcon( inactivePlayer.getColour(), Typ.KING );
            }
            else
            {
                message = "Game drawn by stalemate.";
                icon = null;
            }

            JOptionPane.showMessageDialog( this,
                                           message,
                                           "Game Over",
                                           JOptionPane.PLAIN_MESSAGE,
                                           icon );

            System.out.println( message );
        }
    }

    private void updateScale()
    {
        updateScale( false );
    }

    private void updateScale( boolean piecesOnly )
    {
        int width = contentPane.getWidth();
        int height = contentPane.getHeight();
        int min = Math.min( width, height );

        int scale = Math.max( 10, min / 8 );

        for ( PieceComp pieceComp : pieceComps )
        {
            if ( pieceComp.getParent() instanceof SquareComp )
                pieceComp.setScale( scale );
            else
                pieceComp.setScale( scale * 3 / 5 );
        }

        if ( piecesOnly )
            return;

        for ( SquareComp squareComp : squareComps )
        {
            squareComp.setScale( scale );
        }

        Dimension dimension = new Dimension( scale * 8, scale );

        capturedPiecesPaneWhite.setPreferredSize( dimension );
        capturedPiecesPaneBlack.setPreferredSize( dimension );

        /*
         * The FlowLayout class does not have an option to set the vertical alignment of its components.
         * To achieve this, we set the vertical gap to half of the remaining vertical space in the pane.
         * vgap = (paneHeight - compHeight) / 2 = (scale - scale * 3 / 5) / 2 = scale / 5
         */
        int gap = scale / 5;

        capturedPiecesLayoutWhite.setVgap( gap );
        capturedPiecesLayoutBlack.setVgap( gap );

        capturedPiecesLayoutWhite.setHgap( -gap );
        capturedPiecesLayoutBlack.setHgap( -gap );
    }

    private void positionPieces()
    {
        Board board;

        if ( to == null )
            board = getBoard();
        else
            board = getBoard().move( getSquare( from ), getSquare( to ) );

        for ( PieceComp pieceComp : pieceComps )
        {
            SquareComp squareComp = getSquareComp( board, pieceComp.getPiece() );

            if ( squareComp == null )
            {
                switch ( pieceComp.getPiece().getColour() )
                {
                    case WHITE:
                        capturedPiecesPaneWhite.add( pieceComp );
                        break;

                    case BLACK:
                        capturedPiecesPaneBlack.add( pieceComp );
                        break;
                }
            }
            else
            {
                squareComp.addPieceComp( pieceComp );
            }
        }

        updateScale( true );

        /*
         * Necessary to prevent UI issues. The selected piece may appear on both the 'from' and 'to'
         * square during move previews.
         */
        contentPane.repaint();
    }

    private void updateCheckBorder()
    {
        if ( check == null )
            return;

        if ( to == null )
        {
            int thickness = Math.max( 1, check.getWidth() / 20 );
            check.setBorder( BorderFactory.createLineBorder( Color.RED, thickness ) );
        }
        else
        {
            check.setBorder( BorderFactory.createEmptyBorder() );
        }
    }
}
