package ca.jdr23bc.muzeisimplesource;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    public class MyView extends View {

        GestureDetector gestureDetector;

        public MyView(Context context) {
            super(context);
            gestureDetector = new GestureDetector(context, new GestureListener(this));
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            return gestureDetector.onTouchEvent(e);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            PatternPainter pp = new PatternPainter(canvas);
            pp.style = PatternPainter.Style.Star;
            pp.paint();
        }

        private class GestureListener extends GestureDetector.SimpleOnGestureListener {

            View v;

            public GestureListener(View v) {
                this.v = v;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                v.invalidate();

                float x = e.getX();
                float y = e.getY();

                Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

                return true;
            }
        }
    }
}