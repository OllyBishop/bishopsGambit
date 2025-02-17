package main.java.io;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import main.java.pieces.Piece.Typ;
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

    /**
     * Creates an {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>. The
     * width and height of the instance returned are equal to that of the original image.
     * 
     * @param colour the piece colour (i.e. the colour of the player the piece belongs to)
     * @param type   the piece type
     * @return an {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>
     */
    public static Icon createIcon( Colour colour, Typ type )
    {
        return createIcon( colour, type, -1 );
    }

    /**
     * Creates an {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>. If
     * positive, the width and height of the instance returned are equal to the given <b>scale</b>.
     * If negative, the original image dimensions are used. If zero, an
     * {@code IllegalArgumentException} is thrown.
     * 
     * @param colour the piece colour (i.e. the colour of the player the piece belongs to)
     * @param type   the piece type
     * @param scale  the width and height of the icon
     * @return a scaled {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>
     * @throws IllegalArgumentException if <b>scale</b> is zero
     */
    public static Icon createIcon( Colour colour, Typ type, int scale )
    {
        if ( scale == 0 )
            throw new IllegalArgumentException( "Scale must be non-zero." );

        Image image = getImage( colour, type );

        if ( scale > 0 )
            image = image.getScaledInstance( scale, scale, Image.SCALE_SMOOTH );

        return new ImageIcon( image );
    }
}
