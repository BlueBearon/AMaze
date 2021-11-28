package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.PaintDrawable;
import android.view.View;

public class MazePanel extends View implements P5Panel21 {

    private Canvas art = new Canvas();

    private Paint paint = new Paint();

    private Bitmap map = null;//Bitmap.createBitMap(....)


    public MazePanel(Context context) {
        super(context);
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

    }

    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        art.drawRect(x, y, width, height, paint);

    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        float[] points = new float[2 * nPoints];

        points = fillPoints(xPoints, yPoints, nPoints, points);

       art.drawPoints(points, paint);
    }

    private float[] fillPoints(int[] xPoints, int[] yPoints, int nPoints, float[] points) {

        int count = 0;
        for(int i = 0; i < nPoints; i++)
        {
            points[count] = (float) xPoints[i];
            count++;
            points[count] = (float) yPoints[i];
            count++;
        }

        return points;
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {

        art.drawPoints(null, 0, 0, paint);

    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        art.drawLine(startX, startY, endX, endY, paint);

    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {

    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {


    }

    @Override
    public void addMarker(float x, float y, String str) {

    }

    @Override
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue) {

    }
}
