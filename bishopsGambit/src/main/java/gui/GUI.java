package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
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
import javax.swing.border.Border;

import main.java.board.Board;
import main.java.board.Square;
import main.java.game.Game;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.player.Player;

public class GUI extends JFrame
{
    // ============================================================================================
    // Instance Variables
    // ============================================================================================

    private final JPanel contentPane = new JPanel();
    private final JPanel chessBoardPane = new JPanel();
    private final JPanel capturedPieces = new JPanel();

    private final GridBagLayout whiteBoardLayout = new GridBagLayout();
    private final GridBagLayout blackBoardLayout = new GridBagLayout();

    private final List<SquareComp> squareComps = new ArrayList<>();
    private final List<PieceComp> pieceComps = new ArrayList<>();

    private int scale;

    private Border lineBorder;

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
    private Player getCurrentPlayer()
    {
        return getGame().getCurrentPlayer();
    }

    /**
     * Returns the opponent of the player whose turn it currently is, based on the number of turns
     * taken.
     * 
     * @return Black if the number of turns taken is even; White if it is odd
     */
    private Player getCurrentOpponent()
    {
        return getGame().getCurrentOpponent();
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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize( screenSize.width / 2, screenSize.height );

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

    private PieceComp createPieceComp( Piece piece )
    {
        PieceComp pieceComp = new PieceComp( piece );
        pieceComps.add( pieceComp );
        return pieceComp;
    }

    private void addComponentsToFrame()
    {
        add( contentPane );
        contentPane.add( chessBoardPane );

        for ( SquareComp squareComp : squareComps )
            chessBoardPane.add( squareComp );
    }

    private void createLayouts()
    {
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.addLayoutComponent( chessBoardPane, BorderLayout.CENTER );
        contentPane.setLayout( borderLayout );

        for ( SquareComp squareComp : squareComps )
        {
            GridBagConstraints whiteBoardConstraints = new GridBagConstraints();
            GridBagConstraints blackBoardConstraints = new GridBagConstraints();

            char file = squareComp.getFile();
            char rank = squareComp.getRank();

            whiteBoardConstraints.gridx = file - 'a';
            whiteBoardConstraints.gridy = '8' - rank;

            blackBoardConstraints.gridx = 'h' - file;
            blackBoardConstraints.gridy = rank - '1';

            whiteBoardLayout.setConstraints( squareComp, whiteBoardConstraints );
            blackBoardLayout.setConstraints( squareComp, blackBoardConstraints );
        }
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
             * This method is called once when the frame is initialised, and each time the frame is resized
             * thereafter.
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

        if ( square.isOccupiedBy( getCurrentPlayer() ) )
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
        switch ( getCurrentPlayer().getColour() )
        {
            case WHITE:
                chessBoardPane.setLayout( whiteBoardLayout );
                for ( SquareComp squareComp : squareComps )
                {
                    squareComp.showRank( 'a' );
                    squareComp.showFile( '1' );
                }
                break;

            case BLACK:
                chessBoardPane.setLayout( blackBoardLayout );
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
                                                  to.getIcon(),
                                                  options,
                                                  null );

            if ( i == JOptionPane.CLOSED_OPTION )
                promType = Typ.QUEEN;
            else
                promType = options[ i ];
        }

        Piece promPiece = getGame().move( fromSquare, toSquare, promType );

        if ( promPiece != null )
        {
            PieceComp promPieceComp = createPieceComp( promPiece );
            promPieceComp.setScale( scale );
        }

        from = from.deselect();
        to = to.deselect();

        // TODO: Can remove this once promotion previews are implemented
        positionPieces();
    }

    private void postMove()
    {
        Board board = getBoard();
        Player currentPlayer = getCurrentPlayer();

        boolean currentPlayerIsInCheck = currentPlayer.isInCheck( board );

        if ( currentPlayerIsInCheck )
            check = getSquareComp( board, currentPlayer.getKing() );
        else
            check = null;

        if ( currentPlayer.hasNoLegalMoves( board ) )
        {
            String message;
            Icon icon;

            if ( currentPlayerIsInCheck )
            {
                Player currentOpponent = getCurrentOpponent();

                message = currentOpponent + " wins by checkmate.";
                icon = getSquareComp( board, currentOpponent.getKing() ).getIcon();
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
        int width = contentPane.getWidth();
        int height = contentPane.getHeight();

        scale = Math.max( 10, Math.min( width, height ) / 8 );

        lineBorder = BorderFactory.createLineBorder( Color.red, Math.max( 1, scale / 20 ) );

        for ( SquareComp squareComp : squareComps )
            squareComp.setScale( scale );

        for ( PieceComp pieceComp : pieceComps )
            pieceComp.setScale( scale );
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
                capturedPieces.add( pieceComp );
            else
                squareComp.add( pieceComp );
        }

        chessBoardPane.repaint();
    }

    private void updateCheckBorder()
    {
        if ( check != null )
        {
            if ( to == null )
                check.setBorder( lineBorder );
            else
                check.setBorder( BorderFactory.createEmptyBorder() );
        }
    }
}
