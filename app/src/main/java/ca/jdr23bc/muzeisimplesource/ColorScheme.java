package ca.jdr23bc.muzeisimplesource;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ca.jdr23bc.muzeisimplesource.Colour.ColorScheme.ColorSchemeMonochromatic;

public class ColorScheme {

    public List<Integer> colors = new ArrayList<Integer>();

    public ColorScheme(int rootColor) {
        colors.add(rootColor);
        int[] ints = Colour.colorSchemeOfType(rootColor, ColorSchemeMonochromatic);
        for (int index = 0; index < ints.length; index++) {
            colors.add(ints[index]);
        }
    }

    public int popRandom() {
        return colors.remove(randIndex());
    }

    public int getRandom() {
        return colors.get(randIndex());
    }

    private int randIndex() {
        Random rand = new Random();
        return rand.nextInt(colors.size());
    }
}
