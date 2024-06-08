package main.java.util;

import java.awt.Color;

public class ColorUtils
{
    public static Color blend( Color color1, Color color2 )
    {
        return blend( color1, color2, 1, 1 );
    }

    public static Color blend( Color color1, Color color2, int ratio1, int ratio2 )
    {
        int red1 = color1.getRed();
        int green1 = color1.getGreen();
        int blue1 = color1.getBlue();

        int red2 = color2.getRed();
        int green2 = color2.getGreen();
        int blue2 = color2.getBlue();

        int total = ratio1 + ratio2;
        int r = (ratio1 * red1 + ratio2 * red2) / total;
        int g = (ratio1 * green1 + ratio2 * green2) / total;
        int b = (ratio1 * blue1 + ratio2 * blue2) / total;

        return new Color( r, g, b );
    }

    public static Color changeAlpha( Color color, int a )
    {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return new Color( r, g, b, a );
    }
}
