package main.java.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.java.Orderable;
import main.java.io.Images;
import main.java.pieces.Piece;

public class PieceComp extends JLabel implements Orderable
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

        Image imageFull = Images.getImage( getPiece() );
        Image imageScaled = imageFull.getScaledInstance( scale, scale, Image.SCALE_SMOOTH );
        setIcon( new ImageIcon( imageScaled ) );
    }

    @Override
    public int compareTo( Object o )
    {
        if ( !(o instanceof PieceComp) )
            throw new RuntimeException( "The object being compared must be an instance of PieceComp." );

        return Integer.compare( getOrder(), ((PieceComp) o).getOrder() );
    }

    @Override
    public int getOrder()
    {
        return getPiece().getType().ordinal();
    }
}
