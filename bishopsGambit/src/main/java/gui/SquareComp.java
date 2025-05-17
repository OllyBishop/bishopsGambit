package main.java.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;

import main.java.board.Square;
import main.java.io.Fonts;
import main.java.io.Fonts.Weight;
import main.java.util.ColorUtils;
import main.java.util.ComponentUtils;

public class SquareComp extends JLayeredPane
{
    private static final Color DARK = new Color( 209, 139, 71 );
    private static final Color LIGHT = new Color( 254, 206, 157 );
    private static final Color HIGHLIGHT = ColorUtils.blend( Color.yellow, Color.white );

    private static final Font ROBOTO_MEDIUM = Fonts.importFont( "Roboto", Weight.MEDIUM );

    /*
     * New squares are created throughout the game, but the file and rank of a given square never
     * change. For this reason, we use the file and rank as identifiers for (rather than directly
     * associate a square with) each component.
     */
    private final char file;
    private final char rank;

    private final Color defaultBg;
    private final Color defaultFg;

    private final JLabel fileLbl;
    private final JLabel rankLbl;
    private final CircleComp circleComp;

    protected char getFile()
    {
        return this.file;
    }

    protected char getRank()
    {
        return this.rank;
    }

    protected SquareComp( char file, char rank )
    {
        this.file = file;
        this.rank = rank;

        this.defaultBg = Square.getParity( file, rank ) == 0 ? DARK : LIGHT;
        this.defaultFg = Square.getParity( file, rank ) == 0 ? LIGHT : DARK;

        this.fileLbl = new JLabel( String.valueOf( file ) );
        fileLbl.setVerticalAlignment( SwingConstants.BOTTOM );
        fileLbl.setHorizontalAlignment( SwingConstants.RIGHT );
        fileLbl.setForeground( defaultFg );
        fileLbl.setFont( ROBOTO_MEDIUM );

        this.rankLbl = new JLabel( String.valueOf( rank ) );
        rankLbl.setVerticalAlignment( SwingConstants.TOP );
        rankLbl.setHorizontalAlignment( SwingConstants.LEFT );
        rankLbl.setForeground( defaultFg );
        rankLbl.setFont( ROBOTO_MEDIUM );

        this.circleComp = new CircleComp();

        add( fileLbl, DEFAULT_LAYER );
        add( rankLbl, DEFAULT_LAYER );
        add( circleComp, MODAL_LAYER );

        resetBackground();
        showCircle( false );

        setOpaque( true );
    }

    protected void addPieceComp( PieceComp pieceComp )
    {
        add( pieceComp, PALETTE_LAYER );

        /*
         * Necessary to prevent UI issues. Without this, the selected piece may be positioned outside
         * the bounds of the square it occupies when a move preview is undone.
         */
        pieceComp.setLocation( 0, 0 );
    }

    private void resetBackground()
    {
        setBackground( defaultBg );
    }

    protected void showFile( char rank )
    {
        fileLbl.setVisible( this.rank == rank );
    }

    protected void showRank( char file )
    {
        rankLbl.setVisible( this.file == file );
    }

    protected void showCircle( boolean b )
    {
        circleComp.setVisible( b );
    }

    /**
     * Changes the background color of this component to its highlighted color.
     * 
     * @return {@code this}
     */
    protected SquareComp select()
    {
        setBackground( ColorUtils.blend( defaultBg, HIGHLIGHT, 1, 3 ) );
        return this;
    }

    /**
     * Changes the background color of this button to its default color.
     * 
     * @return {@code null}
     */
    protected SquareComp deselect()
    {
        resetBackground();
        return null;
    }

    /**
     * Sets the border of this button to an empty border.
     * 
     * @return {@code null}
     */
    protected SquareComp resetBorder()
    {
        setBorder( BorderFactory.createEmptyBorder() );
        return null;
    }

    protected void setScale( int scale )
    {
        Dimension dimension = new Dimension( scale, scale );

        setMinimumSize( dimension );
        setPreferredSize( dimension );

        fileLbl.setSize( dimension );
        rankLbl.setSize( dimension );
        circleComp.setSize( dimension );

        int x = scale / 20;
        int y = scale / 40;

        fileLbl.setLocation( -x, -y );
        rankLbl.setLocation( x, y );

        ComponentUtils.resizeFont( fileLbl, scale / 5 );
        ComponentUtils.resizeFont( rankLbl, scale / 5 );
    }

    protected int getIndex()
    {
        return Square.getIndex( file, rank );
    }

    private class CircleComp extends JComponent
    {
        private static final Color BLACK_TRANSLUCENT = ColorUtils.changeAlpha( Color.black, 63 );

        private CircleComp()
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