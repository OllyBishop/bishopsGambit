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
            String imagePath = "/images/%s-%s.png".formatted( colour, type );
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
     * Creates an {@link ImageIcon} for a piece of the given colour and type, using the original image dimensions.
     * 
     * @param colour the colour of the player who owns the piece
     * @param type   the piece type
     * @return an icon for the piece with the given colour and type
     */
    public static Icon createIcon( Player.Colour colour, Piece.Type type )
    {
        return createIcon( colour, type, -1 );
    }

    /**
     * Creates an {@link ImageIcon} for a piece of the given colour and type.
     * <ul>
     * <li>If {@code scale} is positive, the icon's width and height are set to {@code scale}.</li>
     * <li>If {@code scale} is negative, the original image dimensions are used.</li>
     * <li>If {@code scale} is zero, an {@link IllegalArgumentException} is thrown.</li>
     * </ul>
     * 
     * @param colour the colour of the player who owns the piece
     * @param type   the piece type
     * @param scale  the width and height of the icon, or a negative value to use the original image dimensions
     * @return an icon for the piece with the given colour, type and dimensions
     * @throws IllegalArgumentException if {@code scale} is zero
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
