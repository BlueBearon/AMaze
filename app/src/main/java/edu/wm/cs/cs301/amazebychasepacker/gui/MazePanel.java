package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.media.Image;
import android.util.Log;
import android.view.View;

import edu.wm.cs.cs301.amazebychasepacker.R;

public class MazePanel extends View implements P5Panel21 {

    private Canvas art;

    private Paint paint;

    private Bitmap map;

    private int panelId;

    /*
    Tutorials used

    https://www.youtube.com/watch?v=sb9OEl4k9Dk&list=PLnZX5HteTIv_8QCo1e-qgQGHptWALWSIR&index=1&t=4s


     */


    @Override
    protected void onDraw(Canvas canvas)
    {

    }


    public MazePanel(Context context) {
        super(context);
    }

    public void setUpPanel(int id)
    {
        map = BitmapFactory.decodeResource(getResources(), id);

        art = new Canvas(map);

        paint = new Paint();
    }

    public static int getColorfromRGB(int rgbValue, int rgb_def, int rgb_def1) {
        return 2;
    }

    @Override
    public void commit() {
        draw(art);
    }

    @Override
    public boolean isOperational() {

        return(art != null && paint != null && map != null);
    }

    @Override
    public void setColor(int rgb) {

        paint.setColor(rgb);
    }

    @Override
    public int getColor()
    {
        return paint.getColor();
    }

    @Override
    public void addBackground(float percentToExit, int viewWidth, int viewHeight) {

        Bitmap backgroundMap = BitmapFactory.decodeResource(getResources(), R.drawable.starsactual);




    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        //tell paint to fill

        paint.setStyle(Paint.Style.FILL);
        art.drawRect(x, y, width, height, paint);

    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        //https://stackoverflow.com/questions/5783624/porting-awt-graphics-code-to-android
     //use path object

        paint.setStyle(Paint.Style.FILL);
        //tell paint to fill
        Path path = new Path();

        for(int i = 0; i < nPoints; i++)
        {

        }


    }


    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        //use path object
        //tell paint to fill
        Path path = new Path();

        for(int i = 0; i < nPoints; i++)
        {

        }

    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        art.drawLine(startX, startY, endX, endY, paint);

    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {


        paint.setStyle(Paint.Style.FILL);
        art.drawOval(x, y, width, height, paint);


    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

        art.drawArc(x, y, width, height, startAngle, arcAngle, true, paint);

    }

    @Override
    public void addMarker(float x, float y, String str) {

        art.drawText(str, x, y, paint);

    }

    @Override
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue) {

        Log.v("MazePanel" , "setRenderingHint not implemented");
    }



}
