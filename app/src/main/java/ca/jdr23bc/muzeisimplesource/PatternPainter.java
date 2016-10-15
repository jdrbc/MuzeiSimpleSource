package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.Random;

public class PatternPainter {
    Canvas canvas;

    public PatternPainter(Canvas canvas) {
        this.canvas = canvas;
    }

    public void paintDots() {
        // Draw background
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        Random random = new Random();
        paint.setColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        canvas.drawPaint(paint);

        int num = 10;
        int minDotWidth = 50;
        int maxDotWidth = 500;
        for (int i = 0; i < num; i++) {
            Point p = new Point();
            int circleColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            p.x = random.nextInt(canvas.getWidth());
            p.y = random.nextInt(canvas.getHeight());
            Float radius = random.nextFloat() * (maxDotWidth - minDotWidth) + minDotWidth;
            Circle c = new Circle(radius, p, circleColor);
            c.draw(canvas);
            Log.d(SimpleArtSource.TAG, "painted circle " + c.toString());
        }
    }
}
