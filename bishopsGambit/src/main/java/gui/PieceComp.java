package main.java.gui;

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
        setIcon( Images.createIcon( piece.getColour(), piece.getType(), scale ) );
    }

    @Override
    public int compareTo( Orderable o )
    {
        if ( !(o instanceof PieceComp) )
            throw new IllegalArgumentException( "The object being compared must be an instance of PieceComp." );

        return Integer.compare( o.ordinal(), ordinal() );
    }

    @Override
    public int ordinal()
    {
        return piece.getType().ordinal();
    }
}
