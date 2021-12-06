package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.fonts.Font;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import edu.wm.cs.cs301.amazebychasepacker.R;
import edu.wm.cs.cs301.amazebychasepacker.generation.Floorplan;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;

public class MazePanel extends View implements P5Panel21 {

    private Canvas art;

    private Paint paint;

    private Bitmap map = null;

    private PlayingControl game = null;


    Floorplan seenCells;

    private Boolean firstDraw = true;




    /*
    Tutorials used

    https://www.youtube.com/watch?v=sb9OEl4k9Dk&list=PLnZX5HteTIv_8QCo1e-qgQGHptWALWSIR&index=1&t=4s

    https://stackoverflow.com/questions/10918773/how-to-draw-canvas-on-canvas

    https://stackoverflow.com/questions/5663671/creating-an-empty-bitmap-and-drawing-though-canvas-in-android
     */


    @Override
    protected void onDraw(Canvas canvas)
    {
        //draw(art);

        super.onDraw(canvas);

        if(map == null)
        {
            art = canvas;

            int[] xpoints = {400, 600, 200};
            int[] ypoints = {400, 600, 600};
            int num = 3;
            paint.setColor(Color.RED);
            addFilledPolygon(xpoints, ypoints, num);
            paint.setColor(Color.BLUE);
            addFilledRectangle(200, 200, 200, 200);

            art.drawRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, paint);

            addMarker(700, 700, "Hello");
        }
        else
        {
          //canvas.drawBitmap(map, 0, 0, paint);
        }


    }


    public MazePanel(Context context) {
        super(context);
        init();
    }

    public MazePanel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MazePanel(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MazePanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init()
    {
        firstDraw = true;
        art = new Canvas();


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);

       setBackgroundResource(R.drawable.starsactual);



        //addBackground(100, Constants.VIEW_HEIGHT, Constants.VIEW_HEIGHT);
    }


    public static int getColorfromRGB(int rgbValue, int rgb_def, int rgb_def1) {
        return 2;
    }

    @Override
    public void commit() {
        postInvalidate();
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

        /*
        Bitmap backgroundMap = BitmapFactory.decodeResource(getResources(), R.drawable.starsactual);

        paint.setColor(Color.BLACK);

       // addFilledRectangle(0, 0, viewWidth, viewHeight/2);
        addFilledRectangle(0, 0, viewWidth, viewHeight);

       // addFilledRectangle(0, viewHeight/2, viewWidth, viewHeight/2);

         */


    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        //tell paint to fill

        paint.setStyle(Paint.Style.FILL);
        art.drawRect(x, y, x + width, y + height, paint);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        //https://stackoverflow.com/questions/5783624/porting-awt-graphics-code-to-android
     //use path object

        paint.setStyle(Paint.Style.FILL);

        addPolygon(xPoints, yPoints, nPoints);

    }


    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        //use path object
        //tell paint to fill
        Path polygon = new Path();
        polygon.moveTo(xPoints[0], yPoints[0]);

        for(int i = 1; i < nPoints; i++)
        {
            polygon.lineTo(xPoints[i], yPoints[i]);
        }

        polygon.lineTo(xPoints[0], yPoints[0]);
        polygon.close();

        art.drawPath(polygon, paint);

        paint.setStyle(Paint.Style.STROKE);

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

        art.drawArc(x, y, x+ width, y + height, startAngle, arcAngle, true, paint);

    }

    @Override
    public void addMarker(float x, float y, String str) {

        paint.setTextSize(100);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);
        art.drawText(str, x, y, paint);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue) {

        Log.v("MazePanel" , "setRenderingHint not implemented");
    }


    public void StartDrawing()
    {
        map = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        art = new Canvas(map);
    }
}
