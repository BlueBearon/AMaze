package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SensorStatusWidget extends View {


    private int[] robotStatus = {1, 0, 0, 1};
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private int centerX; // x coordinate of center point
    private int centerY; // y coordinate of center point
    private int size; // size of robot

    public SensorStatusWidget(Context context) {
        super(context);
    }

    public SensorStatusWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SensorStatusWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SensorStatusWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Sets the center position for the compass rose and its size
     * @param x The x coordinate of the center point
     * @param y The x coordinate of the center point
     * @param size The size of the compass rose
     */
    public void setPositionAndSize(int x, int y, int size) {
        centerX = x;
        centerY = y;
        this.size = size;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int width = getWidth();

        int rwidth = (int) (.25 * width);


        int startposx = 400;
        int startposy = 400;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        canvas.drawRect(startposx, startposy, startposx + rwidth, startposy + rwidth, paint);
        drawFrontSensor(canvas, startposx, startposy, rwidth);
        drawLeftSensor(canvas, startposx, startposy, rwidth);
        drawBackSensor(canvas, startposx, startposy, rwidth);
        drawRightSensor(canvas, startposx, startposy, rwidth);

    }

    private void drawBackSensor(Canvas canvas, int startx, int starty, int length) {
        if(robotStatus[3] == 1)
        {
            paint.setColor(Color.GREEN);
        }
        else
        {
            paint.setColor(Color.RED);
        }

        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        int half = (int) .5 * length;

        Path cone = new Path();
        cone.moveTo((startx + (int)(.5 * length)), starty + length);
        cone.lineTo(startx, (starty + length) + 100);
        cone.lineTo(startx+length, (starty + length) + 100);
        cone.lineTo((startx + (int)(.5 * length)), starty + length);
        cone.close();
        cone.setFillType(Path.FillType.EVEN_ODD);
        paint.setStrokeWidth(8);
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(cone, paint);
    }

    private void drawLeftSensor(Canvas canvas, int startx, int starty, int length) {
        if(robotStatus[1] == 1)
        {
            paint.setColor(Color.GREEN);
        }
        else
        {
            paint.setColor(Color.RED);
        }

        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        int half = (int) .5 * length;

        Path cone = new Path();
        cone.moveTo(startx, (starty + (int)(.5 * length)));
        cone.lineTo(startx-100, starty + length);
        cone.lineTo(startx-100, starty);
        cone.lineTo(startx, (starty + (int)(.5 * length)));
        cone.close();
        paint.setStrokeWidth(8);
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(cone, paint);
    }

    private void drawRightSensor(Canvas canvas, int startx, int starty, int length) {
        if(robotStatus[2] == 1)
        {
            paint.setColor(Color.GREEN);
        }
        else
        {
            paint.setColor(Color.RED);
        }

        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        int half = (int) .5 * length;

        Path cone = new Path();
        cone.moveTo(startx+ length, (starty + (int)(.5 * length)));
        cone.lineTo((startx + length) + 100, starty + length);
        cone.lineTo((startx + length) + 100, starty);
        cone.lineTo(startx + length, (starty + (int)(.5 * length)));
        cone.close();
        paint.setStrokeWidth(8);
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(cone, paint);
    }

    private void drawFrontSensor(Canvas canvas, int startx, int starty, int length) {

        if(robotStatus[0] == 1)
        {
            paint.setColor(Color.GREEN);
        }
        else
        {
            paint.setColor(Color.RED);
        }

        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        int half = (int) .5 * length;

        Path cone = new Path();
        cone.moveTo((startx + (int)(.5 * length)), starty);
        cone.lineTo(startx, starty-100);
        cone.lineTo(startx+length, starty-100);
        cone.lineTo((startx + (int)(.5 * length)), starty);
        cone.close();
        paint.setStrokeWidth(8);
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(cone, paint);



    }


}
