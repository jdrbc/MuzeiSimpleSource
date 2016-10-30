package ca.jdr23bc.muzeisimplesource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorScheme {

    public List<Integer> colors = new ArrayList<>();
    public Integer rootColor;
    Colour.ColorScheme csType;

    public ColorScheme(int rootColor) {
        this.rootColor = rootColor;
        colors.add(rootColor);
        csType = Colour.getRandomScheme();
        int[] ints = Colour.colorSchemeOfType(rootColor, csType);
        for (int index = 0; index < ints.length; index++) {
            colors.add(ints[index]);
        }
    }

    public int popRandom() {
        if (colors.size() > 0) {
            return colors.remove(randIndex());
        } else {
            return rootColor;
        }
    }

    public int getRandom() {
        return colors.get(randIndex());
    }

    private int randIndex() {
        Random rand = new Random();
        return rand.nextInt(colors.size());
    }
}
