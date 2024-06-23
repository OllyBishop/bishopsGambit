package main.java.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import main.java.board.Board;
import main.java.board.Square;
import main.java.game.Game;
import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.player.Player;
import main.java.util.ComponentUtils;

public class GUI extends JFrame
{
    private enum Layer
    {
        LEGAL_MOVE_LABELS,
        PIECE_LABELS,
        COORDINATE_LABELS,
        SQUARE_BUTTONS;

        private int getZOrder()
        {
            return Layer.values().length - ordinal();
        }
    }

    // Fields --------------------------------------------------------- //
    private final JLayeredPane contentPane = new JLayeredPane();

    private final Map<PieceLabel, SquareButton> boardMap = new HashMap<>();

    private final List<PieceLabel> pieceLbls = new ArrayList<>();
    private final List<SquareButton> squareBtns = new ArrayList<>();

    private int xMid;
    private int yMid;
    private int scale;

    private Border paddedBorder;
    private Border checkBorder;

    private SquareButton from;
    private SquareButton to;
    private SquareButton check;

    private Game game;
    // ---------------------------------------------------------------- //

    // Getters and setters -------------------------------------------- //
    private Game getGame()
    {
        return this.game;
    }

    private void setGame( Game game )
    {
        this.game = game;
    }
    // ---------------------------------------------------------------- //

    // Map methods ---------------------------------------------------- //
    private Square getSquare( SquareButton squareBtn )
    {
        return getBoard().get( squareBtn.getIndex() );
    }

    private SquareButton getSquareBtn( Square square )
    {
        return squareBtns.get( square.getIndex() );
    }

    private SquareButton getSquareBtn( Board board, Piece piece )
    {
        return getSquareBtn( piece.getSquare( board ) );
    }

    private void updateBoardMap()
    {
        Board board;
        if ( to == null )
            board = getBoard();
        else
            board = getBoard().move( getSquare( from ), getSquare( to ) );

        for ( PieceLabel pieceLbl : pieceLbls )
        {
            Piece piece = pieceLbl.getPiece();
            if ( board.containsPiece( piece ) )
                boardMap.put( pieceLbl, getSquareBtn( board, piece ) );
            else
                boardMap.put( pieceLbl, null );
        }
    }
    // ---------------------------------------------------------------- //

    // Derived getters ------------------------------------------------ //
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

    private List<Square> getMoves( SquareButton squareBtn )
    {
        return getSquare( squareBtn ).getPiece().getMoves( getBoard() );
    }
    // ---------------------------------------------------------------- //

    private void addToLayer( JComponent component, Layer layer )
    {
        contentPane.add( component, layer.getZOrder(), 0 );
    }

    private void createPieceLabel( Piece piece )
    {
        PieceLabel pieceLbl = new PieceLabel( piece );
        pieceLbl.setSize( scale, scale );
        pieceLbls.add( pieceLbl );
        addToLayer( pieceLbl, Layer.PIECE_LABELS );
    }

    /**
     * Create the frame.
     */
    public GUI( Game game )
    {
        setGame( game );

        setDefaultCloseOperation( EXIT_ON_CLOSE );
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds( 0, 0, screenSize.width / 2, screenSize.height );

        contentPane.setLayout( null );
        setContentPane( contentPane );

        // Create square buttons
        for ( Square square : getBoard() )
        {
            SquareButton squareBtn = new SquareButton( square );
            squareBtns.add( squareBtn );
            addToLayer( squareBtn, Layer.SQUARE_BUTTONS );
            addToLayer( squareBtn.getFileLbl(), Layer.COORDINATE_LABELS );
            addToLayer( squareBtn.getRankLbl(), Layer.COORDINATE_LABELS );
            addToLayer( squareBtn.getLegalMoveLbl(), Layer.LEGAL_MOVE_LABELS );
        }

        // Create piece labels
        for ( Piece piece : getBoard().getPieces() )
        {
            createPieceLabel( piece );
        }

        updateBoardMap();

        for ( SquareButton squareBtn : squareBtns )
        {
            squareBtn.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent e )
                {
                    if ( from != null && to == null )
                        for ( SquareButton sqBtn : squareBtns )
                            sqBtn.getLegalMoveLbl().setVisible( false );

                    // The square of the button that was pressed
                    Square square = getSquare( squareBtn );

                    boolean deselectFrom = false;
                    boolean deselectTo = true;
                    SquareButton selectFrom = null;
                    SquareButton selectTo = null;

                    if ( square.isOccupiedBy( getCurrentPlayer() ) )
                    {
                        deselectFrom = true;
                        if ( from != squareBtn )
                            selectFrom = squareBtn;
                    }

                    else if ( from != null )
                    {
                        if ( to == null )
                            if ( getMoves( from ).contains( square ) )
                                selectTo = squareBtn;
                            else
                                deselectFrom = true;
                        else if ( to != squareBtn )
                            deselectFrom = true;
                    }

                    SquareButton toBefore = to;

                    if ( deselectFrom && from != null )
                        from = from.deselect();
                    if ( deselectTo && to != null )
                        to = to.deselect();
                    if ( selectFrom != null )
                        from = selectFrom.select();
                    if ( selectTo != null )
                        to = selectTo.select();

                    // If assignment of 'to' changed, update map and reposition pieces
                    if ( to != toBefore )
                    {
                        updateBoardMap();
                        positionPieces();
                        updateCheckBorder();
                    }

                    if ( from != null && to == null )
                        for ( Square sq : getMoves( from ) )
                            getSquareBtn( sq ).getLegalMoveLbl().setVisible( true );
                }
            } );
        }

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( new KeyEventDispatcher()
        {
            @Override
            public boolean dispatchKeyEvent( KeyEvent e )
            {
                boolean enterKeyReleased = e.getKeyCode() == KeyEvent.VK_SPACE && e.getID() == KeyEvent.KEY_RELEASED;
                boolean moveSelected = from != null && to != null;

                if ( enterKeyReleased && moveSelected )
                {
                    makeMove();
                    postMove();
                    refreshBoard();
                }

                return enterKeyReleased;
            }

            private void makeMove()
            {
                Square fromSquare = getSquare( from );
                Square toSquare = getSquare( to );

                Typ promotionType = null;

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
                        promotionType = Typ.QUEEN;
                    else
                        promotionType = options[ i ];
                }

                Piece promotedPiece = game.move( fromSquare, toSquare, promotionType );

                if ( promotedPiece != null )
                    createPieceLabel( promotedPiece );

                from = from.deselect();
                to = to.deselect();

                // TODO: Can remove this once promotion previews are implemented
                updateBoardMap();
            }

            private void postMove()
            {
                Board board = getBoard();
                Player currentPlayer = getCurrentPlayer();

                if ( currentPlayer.isInCheck( board ) )
                    check = getSquareBtn( board, currentPlayer.getKing() );
                else
                    check = null;

                if ( currentPlayer.hasNoLegalMoves( board ) )
                {
                    String message;
                    Icon icon;

                    if ( check != null )
                    {
                        Player currentOpponent = getCurrentOpponent();

                        message = currentOpponent + " won by checkmate.";
                        icon = getSquareBtn( board, currentOpponent.getKing() ).getIcon();
                    }
                    else
                    {
                        message = "Game drawn by stalemate.";
                        icon = null;
                    }

                    JOptionPane.showMessageDialog( GUI.this,
                                                   message,
                                                   "Game Over",
                                                   JOptionPane.PLAIN_MESSAGE,
                                                   icon );

                    System.out.println( message );
                }
            }
        } );

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
                refreshBoard();
            }

            private void updateScale()
            {
                int width = contentPane.getWidth();
                int height = contentPane.getHeight();

                xMid = width / 2;
                yMid = height / 2;
                scale = Math.max( 10, Math.min( width, height ) / 8 );

                paddedBorder = BorderFactory.createEmptyBorder( scale / 40, scale / 20, scale / 40, scale / 20 );
                checkBorder = BorderFactory.createLineBorder( Color.red, Math.max( 1, scale / 20 ) );

                for ( SquareButton squareBtn : squareBtns )
                {
                    squareBtn.setSize( scale, scale );

                    squareBtn.getFileLbl().setBorder( paddedBorder );
                    squareBtn.getRankLbl().setBorder( paddedBorder );

                    ComponentUtils.resizeFont( squareBtn, scale );
                    ComponentUtils.resizeFont( squareBtn.getFileLbl(), scale / 5 );
                    ComponentUtils.resizeFont( squareBtn.getRankLbl(), scale / 5 );
                    ComponentUtils.resizeFont( squareBtn.getLegalMoveLbl(), scale );
                }

                for ( PieceLabel pieceLbl : pieceLbls )
                {
                    pieceLbl.setSize( scale, scale );
                }
            }
        } );
    }

    /**
     * Repositions the chess board (squares, files, ranks, and pieces) relative to the dimensions of
     * the application window. The board is oriented to the current player's perspective.
     */
    private void refreshBoard()
    {
        refreshBoard( getCurrentPlayer() );
    }

    /**
     * Repositions the chess board (squares, files, ranks, and pieces) relative to the dimensions of
     * the application window. The board is oriented to the given player's perspective.
     * 
     * @param perspective the player to whose perspective the board is oriented
     */
    private void refreshBoard( Player perspective )
    {
        positionSquares( perspective );
        positionPieces();
        paintPieces();
        updateCheckBorder();
    }

    private void positionSquares( Player perspective )
    {
        for ( SquareButton squareBtn : squareBtns )
        {
            char file = squareBtn.getFile();
            int rank = squareBtn.getRank();

            int fileIndex = Square.getFileIndex( file );
            int rankIndex = Square.getRankIndex( rank );

            int x = xMid;
            int y = yMid;

            switch ( perspective.getColour() )
            {
                case WHITE:
                    x -= (4 - fileIndex) * scale;
                    y += (3 - rankIndex) * scale;
                    squareBtn.getRankLbl().setVisible( file == 'a' );
                    squareBtn.getFileLbl().setVisible( rank == 1 );
                    break;

                case BLACK:
                    x += (3 - fileIndex) * scale;
                    y -= (4 - rankIndex) * scale;
                    squareBtn.getRankLbl().setVisible( file == 'h' );
                    squareBtn.getFileLbl().setVisible( rank == 8 );
                    break;
            }

            squareBtn.setLocation( x, y );
        }
    }

    private void positionPieces()
    {
        for ( var entry : boardMap.entrySet() )
        {
            PieceLabel pieceLbl = entry.getKey();
            SquareButton squareBtn = entry.getValue();

            if ( squareBtn == null )
            {
                pieceLbl.setVisible( false );
            }
            else
            {
                pieceLbl.setVisible( true );
                pieceLbl.setLocation( squareBtn.getLocation() );
            }
        }
    }

    private void paintPieces()
    {
        for ( PieceLabel pieceLbl : pieceLbls )
            pieceLbl.paintIcon();
    }

    private void updateCheckBorder()
    {
        if ( check != null )
            if ( to == null )
                check.setBorder( checkBorder );
            else
                check.clearBorder();
    }
}
