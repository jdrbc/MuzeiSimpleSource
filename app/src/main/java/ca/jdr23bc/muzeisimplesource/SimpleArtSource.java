package ca.jdr23bc.muzeisimplesource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.MuzeiArtSource;


public class SimpleArtSource extends MuzeiArtSource {
    private static final String TAG = "MuzeiSimpleSource";
    private static final String SOURCE_NAME = "MuzeiSimpleSource";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours
    private static final String IMAGE_NAME ="background.png";

    public SimpleArtSource() {
        super(SOURCE_NAME);
    }

//    public Color generateRandomColor(Color mix) {
//        Random random = new Random();
//        int red = random.nextInt(256);
//        int green = random.nextInt(256);
//        int blue = random.nextInt(256);
//
//        // mix the color
//        if (mix != null) {
//            red = (red + mix.getRed()) / 2;
//            green = (green + mix.getGreen()) / 2;
//            blue = (blue + mix.getBlue()) / 2;
//        }
//
//        Color color = new Color(red, green, blue);
//        return color;
//    }

    public static void drawCircle(Canvas canvas) {
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        int radius;
        radius = 100;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        // Use Color.parseColor to define HTML colors
        Random random = new Random();
        paint.setColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        canvas.drawCircle(x / 2, y / 2, radius, paint);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onUpdate(int reason) {
        Log.e("MuzeiSimpleSource", "test");
        File imagePath = new File(getFilesDir(), "images");
        imagePath.mkdirs();
        File newFile = new File(imagePath, IMAGE_NAME);
        try {
            OutputStream out = new FileOutputStream(newFile);
            WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Bitmap bm = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            drawCircle(c);
            Log.e("MuzeiSimpleSource", size.toString());
            bm.compress(Bitmap.CompressFormat.PNG, 0, out);
        } catch(IOException e) {
            Log.e(TAG, "Exception ", e);
        }

        Uri imgUri = FileProvider.getUriForFile(getApplicationContext(), "ca.jdr23bc.muzeisimplesource.fileprovider", newFile);
        getApplicationContext().grantUriPermission("net.nurik.roman.muzei", imgUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        publishArtwork(new Artwork.Builder()
                .title("circle")
                .byline("random background")
                .imageUri(imgUri)
                .build());
    }
}

