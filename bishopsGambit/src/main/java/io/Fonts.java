package main.java.io;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Fonts
{
    public static Font importFont( String name, Weight weight )
    {
        Font font = null;

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        List<String> fontFamilyNames = Arrays.asList( graphicsEnvironment.getAvailableFontFamilyNames() );

        try
        {
            String fontPath = String.format( "src/main/resources/font/%s/%<s-%s.ttf", name, weight );
            font = Font.createFont( Font.TRUETYPE_FONT, new File( fontPath ) );

            if ( !fontFamilyNames.contains( font.getFontName() ) )
            {
                graphicsEnvironment.registerFont( font );
            }
        }
        catch ( FontFormatException | IOException e )
        {
            e.printStackTrace();
        }

        return font;
    }

    public enum Weight
    {
        THIN( "Thin" ),
        LIGHT( "Light" ),
        REGULAR( "Regular" ),
        MEDIUM( "Medium" ),
        BOLD( "Bold" ),
        BLACK( "Black" ),
        THIN_ITALIC( "ThinItalic" ),
        LIGHT_ITALIC( "LightItalic" ),
        REGULAR_ITALIC( "RegularItalic" ),
        MEDIUM_ITALIC( "MediumItalic" ),
        BOLD_ITALIC( "BoldItalic" ),
        BLACK_ITALIC( "BlackItalic" );

        private final String str;

        Weight( String str )
        {
            this.str = str;
        }

        @Override
        public String toString()
        {
            return this.str;
        }
    }
}
