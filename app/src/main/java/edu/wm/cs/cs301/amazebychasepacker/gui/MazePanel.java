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

    private CompassRose cr;

    private Bitmap map;

    private boolean showWalls;

    private boolean mapMode;

    private boolean showMaze;

    private boolean showSolution;

    private FirstPersonView firstPersonView;
    private Map mapView;

    private Maze mazeconfig;

    private int angle;

    Floorplan seenCells;

    private Picture pict;




    /*
    Tutorials used

    https://www.youtube.com/watch?v=sb9OEl4k9Dk&list=PLnZX5HteTIv_8QCo1e-qgQGHptWALWSIR&index=1&t=4s


     */

    public void toggleSolution()
    {
        showSolution = !showSolution;
    }

    public void toggleFullMap()
    {
        showMaze = showMaze;
    }

    public void toggleLocalMap()
    {
        mapMode = !mapMode;
    }





    @Override
    protected void onDraw(Canvas canvas)
    {
        //canvas.drawRect(10, 10, 20, 20, new Paint());

        art = canvas;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);






        //art.drawRect(0, 0, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, paint);

        addBackground(100, 1400, 1400);


    }


    public MazePanel(Context context) {
        super(context);
    }

    public MazePanel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MazePanel(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public MazePanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }




    public void setUpPanel(Maze mazeConfig)
    {
       cr = new CompassRose();
       cr.setPositionAndSize(Constants.VIEW_WIDTH/2, (int)(0.1*Constants.VIEW_HEIGHT), 35);

       firstPersonView = new FirstPersonView(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Constants.MAP_UNIT, Constants.STEP_SIZE, seenCells, mazeConfig.getRootnode());

    }

    public void updateInfo()
    {

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

        Bitmap backgroundMap = BitmapFactory.decodeResource(getResources(), R.drawable.starsactual);

        paint.setColor(Color.BLACK);

       // addFilledRectangle(0, 0, viewWidth, viewHeight/2);
        addFilledRectangle(0, 0, viewWidth, viewHeight);

       // addFilledRectangle(0, viewHeight/2, viewWidth, viewHeight/2);


    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        //tell paint to fill

        paint.setStyle(Paint.Style.FILL);
        art.drawRect(x, y, width, height, paint);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        //https://stackoverflow.com/questions/5783624/porting-awt-graphics-code-to-android
     //use path object

        paint.setStyle(Paint.Style.FILL);
        //tell paint to fill
        Path polygon = new Path();
        polygon.moveTo(xPoints[0], yPoints[0]);

        for(int i = 1; i < nPoints; i++)
        {
            polygon.lineTo(xPoints[i], yPoints[i]);
            polygon.moveTo(xPoints[i], yPoints[i]);
        }

        polygon.lineTo(xPoints[0], yPoints[0]);

        paint.setStyle(Paint.Style.STROKE);


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
