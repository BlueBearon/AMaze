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


    private int[] robotStatus = {1, 1, 0, 0};
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

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

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int rwidth = (int) (.25 * width);
        int rheight = rwidth;

        int half = (int) (.5) * rwidth;

        int startposx = 400;
        int startposy = 400;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);

        canvas.drawRect(startposx, startposy, startposx + rwidth, startposy + rheight, paint);
        drawFrontSensor(canvas, startposx, startposy, rwidth);
        drawLeftSensor(canvas, startposx, startposy, rwidth);
        drawBackSensor(canvas, startposx, startposy, rwidth);
        drawRightSensor(canvas, startposx, startposy, rwidth);
        /*
        Path cone = new Path();
        cone.moveTo(20, 20);
        cone.lineTo(10, 10);
        cone.moveTo(10, 10);
        cone.lineTo(30, 10);
        //cone.addArc(10, 10, 30, 30, 15, 50);
        cone.moveTo(30, 10);
        cone.lineTo(20, 20);
        cone.close();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(cone, paint);
        */
        Path path = new Path();
        path.moveTo(400, 400);
        path.addArc(400, 400, 600, 400, 25, 135);
        path.moveTo(600, 400);
        path.close();
        paint.setStrokeWidth(3);
        paint.setPathEffect(null);
        paint.setColor(Color.GREEN);
        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        /*
        for (int i = 300; i < 400; i++) {
            path.moveTo(i, i-1);
            path.lineTo(i, i);
        }
         */
        /*
        path.moveTo((400 + (int)(.5 * rwidth)), 400);
        path.lineTo(400, 350);
        path.moveTo(400, 350);
        path.lineTo(400+rwidth, 350);
        path.moveTo(400+rwidth, 350);
        path.lineTo((400 + (int)(.5 * rwidth)), 400);
        path.moveTo((400 + (int)(.5 * rwidth)), 400);
        path.close();
        paint.setStrokeWidth(3);
        paint.setPathEffect(null);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, paint);
        */

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
        cone.moveTo((400 + (int)(.5 * length)), 400 + length);
        cone.lineTo(400, (400 + length) + 100);
        cone.moveTo(400, (400 + length) + 100);
        cone.lineTo(400+length, (400 + length) + 100);
        cone.moveTo(400+length, (400 + length) + 100);
        cone.lineTo((400 + (int)(.5 * length)), 400 + length);
        cone.moveTo((400 + (int)(.5 * length)), 400 + length);
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
        cone.moveTo(400, (400 + (int)(.5 * length)));
        cone.lineTo(300, 400 + length);
        cone.moveTo(300, 400 + length);
        cone.lineTo(300, 400);
        cone.moveTo(300, 400);
        cone.lineTo(400, (400 + (int)(.5 * length)));
        cone.moveTo(400, (400 + (int)(.5 * length)));
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
        cone.moveTo(400+ length, (400 + (int)(.5 * length)));
        cone.lineTo((400 + length) + 100, 400 + length);
        cone.moveTo((400 + length) + 100, 400 + length);
        cone.lineTo((400 + length) + 100, 400);
        cone.moveTo((400 + length) + 100, 400);
        cone.lineTo(400 + length, (400 + (int)(.5 * length)));
        cone.moveTo(400+ length, (400 + (int)(.5 * length)));
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
        cone.moveTo((400 + (int)(.5 * length)), 400);
        cone.lineTo(400, 300);
        cone.moveTo(400, 300);
        cone.lineTo(400+length, 300);
        cone.moveTo(400+length, 300);
        cone.lineTo((400 + (int)(.5 * length)), 400);
        cone.moveTo((400 + (int)(.5 * length)), 400);
        cone.close();
        paint.setStrokeWidth(8);
        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(cone, paint);



    }


}
