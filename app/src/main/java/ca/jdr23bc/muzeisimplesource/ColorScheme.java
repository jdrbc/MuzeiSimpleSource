package ca.jdr23bc.muzeisimplesource;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorScheme {

    public List<Integer> colors = new ArrayList<>();
    Colour.ColorScheme csType;

    public ColorScheme(int rootColor) {
        colors.add(rootColor);
        csType = Colour.getRandomScheme();
        int[] ints = Colour.colorSchemeOfType(rootColor, csType);
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
