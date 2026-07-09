package io.github.ollybishop.bishopsgambit.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import io.github.ollybishop.bishopsgambit.gui.util.ColorUtils;
import io.github.ollybishop.bishopsgambit.gui.util.ComponentUtils;
import io.github.ollybishop.bishopsgambit.io.FontFace;
import io.github.ollybishop.bishopsgambit.io.Fonts;
import io.github.ollybishop.bishopsgambit.model.Square;

class SquareComponent extends JLayeredPane
{
    private static final Color DARK_SQUARE_BG = new Color( 209, 139, 71 );
    private static final Color LIGHT_SQUARE_BG = new Color( 254, 206, 157 );

    private static final Color YELLOW_WHITE = ColorUtils.blend( Color.YELLOW, Color.WHITE );

    private static final Font ROBOTO_MEDIUM = Fonts.importFont( "Roboto", FontFace.MEDIUM );

    /*
     * Square instances are cloned throughout the game so that each board state can be stored independently. The number of
     * SquareComponent instances, however, remains fixed at 64.
     * 
     * The file and rank identify the board coordinate represented by this component. Because these fields are shared by Square
     * and SquareComponent, instances of the two classes can be mapped to one another for any given board state.
     */
    private final char file;
    private final char rank;

    private final Color defaultBackground;
    private final Color defaultForeground;

    private final JLabel fileLabel;
    private final JLabel rankLabel;
    private final MoveMarker moveMarker;

    char getFile()
    {
        return this.file;
    }

    char getRank()
    {
        return this.rank;
    }

    SquareComponent( char file, char rank )
    {
        this.file = file;
        this.rank = rank;

        Square.Shade squareShade = Square.getShade( file, rank );

        this.defaultBackground = squareShade == Square.Shade.DARK ? DARK_SQUARE_BG : LIGHT_SQUARE_BG;
        this.defaultForeground = squareShade == Square.Shade.DARK ? LIGHT_SQUARE_BG : DARK_SQUARE_BG;

        this.fileLabel = new JLabel( String.valueOf( file ) );
        fileLabel.setVerticalAlignment( SwingConstants.BOTTOM );
        fileLabel.setHorizontalAlignment( SwingConstants.RIGHT );
        fileLabel.setForeground( defaultForeground );
        fileLabel.setFont( ROBOTO_MEDIUM );

        this.rankLabel = new JLabel( String.valueOf( rank ) );
        rankLabel.setVerticalAlignment( SwingConstants.TOP );
        rankLabel.setHorizontalAlignment( SwingConstants.LEFT );
        rankLabel.setForeground( defaultForeground );
        rankLabel.setFont( ROBOTO_MEDIUM );

        this.moveMarker = new MoveMarker();

        add( fileLabel, DEFAULT_LAYER );
        add( rankLabel, DEFAULT_LAYER );
        add( moveMarker, MODAL_LAYER );

        resetBackground();
        showMoveMarker( false );

        setOpaque( true );
    }

    void addPieceComponent( PieceComponent pieceComponent )
    {
        add( pieceComponent, PALETTE_LAYER, 0 );

        // Reset location to (0, 0) relative to the parent square so the piece doesn't render off screen
        pieceComponent.setLocation( 0, 0 );
    }

    private void resetBackground()
    {
        setBackground( defaultBackground );
    }

    void showFile( char rank )
    {
        fileLabel.setVisible( this.rank == rank );
    }

    void showRank( char file )
    {
        rankLabel.setVisible( this.file == file );
    }

    void showMoveMarker( boolean b )
    {
        moveMarker.setVisible( b );
    }

    boolean hasMoveMarker()
    {
        return moveMarker.isVisible();
    }

    /**
     * Changes the background color of this component to its highlighted color.
     */
    void select()
    {
        setBackground( ColorUtils.blend( defaultBackground, YELLOW_WHITE, 1, 3 ) );
    }

    /**
     * Changes the background color of this button to its default color.
     */
    void deselect()
    {
        resetBackground();
    }

    /**
     * Sets the border of this button to an empty border.
     */
    void resetBorder()
    {
        setBorder( BorderFactory.createEmptyBorder() );
    }

    void setScale( int scale )
    {
        Dimension dimension = new Dimension( scale, scale );

        setMinimumSize( dimension );
        setPreferredSize( dimension );

        fileLabel.setSize( dimension );
        rankLabel.setSize( dimension );
        moveMarker.setSize( dimension );

        int x = scale / 20;
        int y = scale / 40;

        fileLabel.setLocation( -x, -y );
        rankLabel.setLocation( x, y );

        ComponentUtils.resizeFont( fileLabel, scale / 5 );
        ComponentUtils.resizeFont( rankLabel, scale / 5 );
    }

    int getIndex()
    {
        return Square.getIndex( file, rank );
    }

    void debugLayeringIssues()
    {
        int totalCount = getComponentCount();

        int defaultCount = getComponentCountInLayer( DEFAULT_LAYER );
        int paletteCount = getComponentCountInLayer( PALETTE_LAYER );
        int modalCount = getComponentCountInLayer( MODAL_LAYER );

        if ( defaultCount + paletteCount + modalCount < totalCount )
            System.err.println( String.format(
                "Total number of components (%d) does not equal the sum of the number of components in each layer (%d default, %d palette, %d modal) for square '%s%s'",
                totalCount,
                defaultCount,
                paletteCount,
                modalCount,
                getFile(),
                getRank() ) );
    }

    private class MoveMarker extends JComponent
    {
        private static final Color BLACK_TRANSLUCENT = ColorUtils.changeAlpha( Color.BLACK, 64 );

        private MoveMarker()
        {
            setBackground( null );
        }

        @Override
        protected void paintComponent( Graphics g )
        {
            super.paintComponent( g );

            int xMid = getWidth() / 2;
            int yMid = getHeight() / 2;

            int diameter = Math.min( getWidth(), getHeight() ) * 2 / 5;
            int radius = diameter / 2;

            g.setColor( BLACK_TRANSLUCENT );
            g.fillOval( xMid - radius, yMid - radius, diameter, diameter );
        }
    }
}
