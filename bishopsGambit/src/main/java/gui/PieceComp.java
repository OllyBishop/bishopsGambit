package main.java.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.java.io.Graphics;
import main.java.pieces.Piece;

public class PieceComp extends JLabel
{
    private final Piece piece;

    public Piece getPiece()
    {
        return this.piece;
    }

    public PieceComp( Piece piece )
    {
        this.piece = piece;
    }

    public void setScale( int scale )
    {
        setSize( scale, scale );
        paintIcon();
    }

    private void paintIcon()
    {
        Image imageFull = Graphics.getImage( getPiece() );
        Image imageScaled = imageFull.getScaledInstance( getWidth(), getHeight(), Image.SCALE_SMOOTH );
        setIcon( new ImageIcon( imageScaled ) );
    }
}
