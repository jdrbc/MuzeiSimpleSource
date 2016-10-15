package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.Random;
import java.util.StringTokenizer;

public class PatternPainter {
    Canvas canvas;
    Random random = new Random();
    ColorScheme colorScheme;

    public PatternPainter(Canvas canvas) {
        this.canvas = canvas;
    }

    public void paint() {
        int rootColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        this.colorScheme = new ColorScheme(rootColor);
        paintLines();
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

        // TODO-jdr dynamically set these values based on canvas size or user params
        int num = random.nextInt(9) + 1;
        int minWidth = 25;
        int maxWidth = 150;
        String[] directions = new String[] {"vertical", "horizontal"};
        for (int i = 0; i < num; i++) {
            String direction = directions[random.nextInt(2)];

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

            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setColor(colorScheme.getRandom());
            p.setStrokeWidth(random.nextFloat() * (maxWidth - minWidth) + minWidth);
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
