package br.ifce.mastermind.util;

import java.awt.*;

/**
 * Created by jrocha on 25/07/14.
 */
public class ColorUtil {

    public static final String GREEN = "Green";
    public static final String YELLOW = "Yellow";
    public static final String BLUE = "Blue";
    public static final String CYAN = "Cyan";
    public static final String ORANGE = "Orange";
    public static final String RED = "Red";
    public static final String BLACK = "Black";
    public static final String WHITE = "White";


    public static Color[] toColorArray(String[] colors) {

        Color[] result = new Color[colors.length];

        for (int i = 0; i < colors.length; i++) {
            result[i] = string2Color(colors[i]);
        }

        return result;
    }

    public static Color[] getValidColors() {
        return new Color[]{Color.GREEN, Color.YELLOW, Color.BLUE, Color.CYAN, Color.ORANGE, Color.RED};
    }

    public static String[] getValidColorNames() {
        return new String[]{GREEN, YELLOW, BLUE, CYAN, ORANGE, RED};
    }

    public static String color2String(Color color) {

        String result = "";

        if (color.equals(Color.GREEN)) {
            result = GREEN;
        } else if (color.equals(Color.YELLOW)) {
            result = YELLOW;
        } else if (color.equals(Color.BLUE)) {
            result = BLUE;
        } else if (color.equals(Color.CYAN)) {
            result = CYAN;
        } else if (color.equals(Color.ORANGE)) {
            result = ORANGE;
        } else if (color.equals(Color.RED)) {
            result = RED;
        } else if (color.equals(Color.BLACK)) {
            result = BLACK;
        } else if (color.equals(Color.WHITE)) {
            result = WHITE;
        } else {
            return "";
        }

        return result;
    }

    public static Color string2Color(String color) {
        Color result = Color.DARK_GRAY;

        if (color.equalsIgnoreCase(GREEN)) {
            result = Color.GREEN;
        } else if (color.equalsIgnoreCase(YELLOW)) {
            result = Color.YELLOW;
        } else if (color.equalsIgnoreCase(BLUE)) {
            result = Color.BLUE;
        } else if (color.equalsIgnoreCase(CYAN)) {
            result = Color.CYAN;
        } else if (color.equalsIgnoreCase(ORANGE)) {
            result = Color.ORANGE;
        } else if (color.equalsIgnoreCase(RED)) {
            result = Color.RED;
        } else if (color.equalsIgnoreCase(BLACK)) {
            result = Color.BLACK;
        } else if (color.equalsIgnoreCase(WHITE)) {
            result = Color.WHITE;
        }

        return result;
    }
}
