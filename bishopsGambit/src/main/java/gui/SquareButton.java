package main.java.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import main.java.board.Square;
import main.java.io.Fonts;
import main.java.util.ColorUtils;

public class SquareButton extends JButton
{
    private static final Color DARK = new Color( 209, 139, 71 );
    private static final Color LIGHT = new Color( 254, 206, 157 );
    private static final Color HIGHLIGHT = ColorUtils.blend( Color.yellow, Color.white );

    private static final Color BLACK_TRANSLUCENT = ColorUtils.changeAlpha( Color.black, 85 );
    private static final char BLACK_CIRCLE_CHAR = '\u25cf';

    /*
     * New squares are created throughout the game (two with each move), but the file and rank of a
     * given square never change. For this reason, we use the file and rank as identifiers for
     * (rather than directly associate a square with) each button.
     */
    private final char file;
    private final int rank;

    private final Color defaultBg;
    private final Color defaultFg;

    private final JLabel fileLbl;
    private final JLabel rankLbl;
    private final JLabel legalMoveLbl;

    public char getFile()
    {
        return this.file;
    }

    public int getRank()
    {
        return this.rank;
    }

    public JComponent getFileLbl()
    {
        return this.fileLbl;
    }

    public JComponent getRankLbl()
    {
        return this.rankLbl;
    }

    public JComponent getLegalMoveLbl()
    {
        return this.legalMoveLbl;
    }

    public SquareButton( Square square )
    {
        this( square.getFile(), square.getRank() );
    }

    private SquareButton( char file, int rank )
    {
        this.file = file;
        this.rank = rank;

        this.defaultBg = (file + rank) % 2 == 0 ? DARK : LIGHT;
        this.defaultFg = (file + rank) % 2 == 1 ? DARK : LIGHT;

        this.fileLbl = new JLabel( Character.toString( file ) );
        fileLbl.setVerticalAlignment( SwingConstants.BOTTOM );
        fileLbl.setHorizontalAlignment( SwingConstants.RIGHT );
        fileLbl.setForeground( defaultFg );
        fileLbl.setFont( Fonts.ROBOTO_MEDIUM );
        fileLbl.setBorder( BorderFactory.createEmptyBorder() );

        this.rankLbl = new JLabel( Integer.toString( rank ) );
        rankLbl.setVerticalAlignment( SwingConstants.TOP );
        rankLbl.setHorizontalAlignment( SwingConstants.LEFT );
        rankLbl.setForeground( defaultFg );
        rankLbl.setFont( Fonts.ROBOTO_MEDIUM );
        rankLbl.setBorder( BorderFactory.createEmptyBorder() );

        this.legalMoveLbl = new JLabel( Character.toString( BLACK_CIRCLE_CHAR ), CENTER );
        legalMoveLbl.setForeground( BLACK_TRANSLUCENT );
        legalMoveLbl.setVisible( false );

        clearBorder();
        resetBackground();

        setFocusable( false );
        setOpaque( true );
    }

    public int getIndex()
    {
        return Square.getIndex( getFile(), getRank() );
    }

    public void clearBorder()
    {
        setBorder( BorderFactory.createEmptyBorder() );
    }

    private void resetBackground()
    {
        setBackground( defaultBg );
    }

    /**
     * Sets the selected state of this button to {@code true}. Changes the background color of this
     * button to indicate it is selected.
     * 
     * @return {@code this}
     */
    public SquareButton select()
    {
        setSelected( true );
        setBackground( ColorUtils.blend( defaultBg, HIGHLIGHT, 1, 3 ) );
        return this;
    }

    /**
     * Sets the selected state of this button to {@code false}. Changes the background color of this
     * button to its default color.
     * 
     * @return {@code null}
     */
    public SquareButton deselect()
    {
        setSelected( false );
        resetBackground();
        return null;
    }

    @Override
    public void setSize( int width, int height )
    {
        super.setSize( width, height );
        fileLbl.setSize( width, height );
        rankLbl.setSize( width, height );
        legalMoveLbl.setSize( width, height );
    }

    @Override
    public void setLocation( int x, int y )
    {
        super.setLocation( x, y );
        fileLbl.setLocation( x, y );
        rankLbl.setLocation( x, y );
        legalMoveLbl.setLocation( x, y );
    }
}