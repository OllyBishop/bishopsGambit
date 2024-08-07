package main.java.io;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import main.java.pieces.Piece;
import main.java.pieces.Piece.Typ;
import main.java.player.Player;
import main.java.player.Player.Colour;

public class Images
{
    private static Image whitePawn = importImage( Colour.WHITE, Typ.PAWN );
    private static Image whiteKnight = importImage( Colour.WHITE, Typ.KNIGHT );
    private static Image whiteBishop = importImage( Colour.WHITE, Typ.BISHOP );
    private static Image whiteRook = importImage( Colour.WHITE, Typ.ROOK );
    private static Image whiteQueen = importImage( Colour.WHITE, Typ.QUEEN );
    private static Image whiteKing = importImage( Colour.WHITE, Typ.KING );

    private static Image blackPawn = importImage( Colour.BLACK, Typ.PAWN );
    private static Image blackKnight = importImage( Colour.BLACK, Typ.KNIGHT );
    private static Image blackBishop = importImage( Colour.BLACK, Typ.BISHOP );
    private static Image blackRook = importImage( Colour.BLACK, Typ.ROOK );
    private static Image blackQueen = importImage( Colour.BLACK, Typ.QUEEN );
    private static Image blackKing = importImage( Colour.BLACK, Typ.KING );

    private static Image importImage( Colour colour, Typ type )
    {
        Image image = null;

        try
        {
            String imagePath = String.format( "/main/resources/img/%s_%s.png", colour, type );
            image = ImageIO.read( Images.class.getResource( imagePath ) );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return image;
    }

    public static Image getImage( Piece piece )
    {
        return getImage( piece.getColour(), piece.getType() );
    }

    public static Image getImage( Colour colour, Typ type )
    {
        return switch ( colour )
        {
            case WHITE -> switch ( type )
            {
                case PAWN -> whitePawn;
                case KNIGHT -> whiteKnight;
                case BISHOP -> whiteBishop;
                case ROOK -> whiteRook;
                case QUEEN -> whiteQueen;
                case KING -> whiteKing;
            };

            case BLACK -> switch ( type )
            {
                case PAWN -> blackPawn;
                case KNIGHT -> blackKnight;
                case BISHOP -> blackBishop;
                case ROOK -> blackRook;
                case QUEEN -> blackQueen;
                case KING -> blackKing;
            };
        };
    }

    public static Icon getIcon( Player player, Typ type )
    {
        return new ImageIcon( getImage( player.getColour(), type ) );
    }
}
