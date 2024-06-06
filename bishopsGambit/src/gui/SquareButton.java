package gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

import board.Square;
import utils.ColorUtils;

public class SquareButton extends JButton
{
    private static final Color DARK = new Color( 209, 139, 71 );
    private static final Color LIGHT = new Color( 254, 206, 157 );
    private static final Color HIGHLIGHT = ColorUtils.blend( Color.yellow, Color.white );

    private static final Character BLACK_DOT_CHAR = '\u25cf';
    private static final Color BLACK_SEMI_TRANSPARENT = ColorUtils.changeAlpha( Color.black, 85 );

    /*
     * New squares are created throughout the game (two with each move), but the file and rank of a
     * given square never change. For this reason, we use the file and rank as identifiers for
     * (rather than directly associate a square with) each button.
     */
    private final char file;
    private final int rank;

    private final JLabel placeholder;

    public char getFile()
    {
        return this.file;
    }

    public int getRank()
    {
        return this.rank;
    }

    public JLabel getPlaceholder()
    {
        return this.placeholder;
    }

    public SquareButton( char file, int rank )
    {
        this.file = file;
        this.rank = rank;

        clearBorder();
        resetBackground();

        setFocusable( false );
        setOpaque( true );

        JLabel placeholder = new JLabel( BLACK_DOT_CHAR.toString(), CENTER );
        placeholder.setForeground( BLACK_SEMI_TRANSPARENT );
        placeholder.setVisible( false );

        this.placeholder = placeholder;
    }

    public int getIndex()
    {
        return Square.getIndex( getFile(), getRank() );
    }

    private Color getDefaultBg()
    {
        return (getFile() + getRank()) % 2 == 0 ? DARK : LIGHT;
    }

    public void clearBorder()
    {
        setBorder( BorderFactory.createEmptyBorder() );
    }

    private void resetBackground()
    {
        setBackground( getDefaultBg() );
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
        setBackground( ColorUtils.blend( getDefaultBg(), HIGHLIGHT, 1, 3 ) );
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
        getPlaceholder().setSize( width, height );
    }

    @Override
    public void setLocation( int x, int y )
    {
        super.setLocation( x, y );
        getPlaceholder().setLocation( x, y );
    }
}
