package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PatternPainter {
    private static final List<Style> STYLES =
            Collections.unmodifiableList(Arrays.asList(Style.values()));

    public enum Style {
        Lines, Dots
    }
    Canvas canvas;
    Style style;
    Random random = new Random();
    ColorScheme colorScheme;

    public PatternPainter(Canvas canvas) {
        this.canvas = canvas;
        this.style = STYLES.get(random.nextInt(STYLES.size()));
    }

    public void paint() {
        int rootColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        this.colorScheme = new ColorScheme(rootColor);

        if (style == Style.Lines) {
            paintLines();
        } else if (style == Style.Dots) {
            paintDots();
        }
    }

    public void paintDots() {
        fillBackground();

        // TODO-jdr dynamically set these values based on canvas size or user params
        int num = random.nextInt(9) + 1;
        int minDotWidth = 25;
        int maxDotWidth = 150;
        int circleColor = colorScheme.popRandom();
        for (int i = 0; i < num; i++) {
            Point p = new Point();
            p.x = random.nextInt(canvas.getWidth());
            p.y = random.nextInt(canvas.getHeight());
            Float radius = random.nextFloat() * (maxDotWidth - minDotWidth) + minDotWidth;
            Circle c = new Circle(radius, p, circleColor);
            c.draw(canvas);
            Log.d(SimpleArtSource.TAG, "painted circle " + c.toString());
        }
    }

    public void paintLines() {
        fillBackground();

        Paint p = new Paint();
        p.setAntiAlias(true);
        int minWidth = 25;
        int maxWidth = 150;
        String direction = "vertical";
        String[] directions = new String[] {"vertical", "horizontal"};
        boolean sameColor = random.nextBoolean();
        if (sameColor) {
            p.setColor(colorScheme.getRandom());
            p.setAlpha(random.nextInt(255));
        }
        boolean sameDir = random.nextBoolean();
        if (sameDir) {
            direction = directions[random.nextInt(2)];
        }
        boolean sameWeight = random.nextBoolean();
        if (sameWeight) {
            p.setStrokeWidth(random.nextFloat() * (maxWidth - minWidth) + minWidth);
        }

        int num = random.nextInt(9) + 1;
        for (int i = 0; i < num; i++) {
            if (!sameColor) {
                p.setColor(colorScheme.getRandom());
                p.setAlpha(random.nextInt(255));
            }
            if (!sameDir) {
                direction = directions[random.nextInt(2)];
            }
            if (!sameWeight) {
                p.setStrokeWidth(random.nextFloat() * (maxWidth - minWidth) + minWidth);
            }
            Point start = new Point();
            Point end = new Point();
            if (direction == "vertical") {
                start.y = -500;
                start.x = random.nextInt(canvas.getWidth());
                end.y = canvas.getHeight() + 500;
                end.x = random.nextInt(canvas.getWidth());
            } else {
                start.y = random.nextInt(canvas.getHeight());
                start.x = -500;
                end.y = random.nextInt(canvas.getHeight());
                end.x = canvas.getWidth() + 500;
            }
            canvas.drawLine(start.x, start.y, end.x, end.y, p);
        }
    }

    public void fillBackground() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorScheme.popRandom());
        canvas.drawPaint(paint);
    }
}
