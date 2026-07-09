package io.github.ollybishop.bishopsgambit.io;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.piece.Piece;

public class Images
{
    private static Image whitePawn = importImage( Player.Colour.WHITE, Piece.Type.PAWN );
    private static Image whiteKnight = importImage( Player.Colour.WHITE, Piece.Type.KNIGHT );
    private static Image whiteBishop = importImage( Player.Colour.WHITE, Piece.Type.BISHOP );
    private static Image whiteRook = importImage( Player.Colour.WHITE, Piece.Type.ROOK );
    private static Image whiteQueen = importImage( Player.Colour.WHITE, Piece.Type.QUEEN );
    private static Image whiteKing = importImage( Player.Colour.WHITE, Piece.Type.KING );

    private static Image blackPawn = importImage( Player.Colour.BLACK, Piece.Type.PAWN );
    private static Image blackKnight = importImage( Player.Colour.BLACK, Piece.Type.KNIGHT );
    private static Image blackBishop = importImage( Player.Colour.BLACK, Piece.Type.BISHOP );
    private static Image blackRook = importImage( Player.Colour.BLACK, Piece.Type.ROOK );
    private static Image blackQueen = importImage( Player.Colour.BLACK, Piece.Type.QUEEN );
    private static Image blackKing = importImage( Player.Colour.BLACK, Piece.Type.KING );

    private static Image importImage( Player.Colour colour, Piece.Type type )
    {
        Image image = null;

        try
        {
            String imagePath = String.format( "/images/%s-%s.png", colour, type );
            image = ImageIO.read( Images.class.getResource( imagePath ) );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return image;
    }

    private static Image getImage( Player.Colour colour, Piece.Type type )
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
     * Creates an {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>. The width and height of the new
     * icon are equal to that of the original image.
     * 
     * @param colour the piece colour (i.e. the colour of the player who owns the piece)
     * @param type   the piece type
     * @return an {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>
     */
    public static Icon createIcon( Player.Colour colour, Piece.Type type )
    {
        return createIcon( colour, type, -1 );
    }

    /**
     * Creates an {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>. If <b>scale</b> is positive, the
     * width and height of the new icon are equal to this value. If <b>scale</b> is negative, the original image dimensions are
     * used. If <b>scale</b> is zero, an {@code IllegalArgumentException} is thrown.
     * 
     * @param colour the piece colour (i.e. the colour of the player who owns the piece)
     * @param type   the piece type
     * @param scale  the width and height of the icon
     * @return a scaled {@code ImageIcon} of the piece with the given <b>colour</b> and <b>type</b>
     * @throws IllegalArgumentException if <b>scale</b> is zero
     */
    public static Icon createIcon( Player.Colour colour, Piece.Type type, int scale )
    {
        if ( scale == 0 )
            throw new IllegalArgumentException( "Scale must be non-zero." );

        Image image = getImage( colour, type );

        if ( scale > 0 )
            image = image.getScaledInstance( scale, scale, Image.SCALE_SMOOTH );

        return new ImageIcon( image );
    }
}
