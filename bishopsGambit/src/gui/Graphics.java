package gui;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import pieces.Piece;
import pieces.Piece.Typ;
import players.Player.Colour;

public class Graphics
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
            String imagePath = String.format( "/img/%s_%s.png", colour, type );
            image = ImageIO.read( Graphics.class.getResource( imagePath ) );
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
}
