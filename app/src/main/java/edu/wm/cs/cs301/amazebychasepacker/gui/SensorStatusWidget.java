package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SensorStatusWidget{


    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private int x;
    private int y;
    private int length;
    private MazePanel panel;


    public SensorStatusWidget(MazePanel panel, int startposx, int startposy, int length)
    {
        this.panel = panel;
        x = startposx;
        y = startposy;
        this.length = length;
    }


    public void drawWidget(int[] status)
    {
        panel.setColor(Color.YELLOW);

        panel.addFilledRectangle(x, y, length, length);
        drawFrontSensor(x, y, length, status[3]);
        drawLeftSensor(x, y, length, status[1]);
        drawBackSensor(x, y, length, status[0]);
        drawRightSensor(x, y, length, status[2]);

    }

    private void drawBackSensor(int startx, int starty, int length, int status) {
        if(status == 1)
        {
            panel.setColor(Color.GREEN);
        }
        else
        {
            panel.setColor(Color.RED);
        }


        int[] xpoints = new int[3];
        int[] ypoints = new int [3];
        xpoints[0] =  (startx + (int)(.5 * length));
        ypoints[0] = starty + length;
        xpoints[1] = startx;
        ypoints[1] = ((starty + length) + 100);
        xpoints[2] = startx + length;
        ypoints[2] = (starty + length) + 100;

        panel.addFilledPolygon(xpoints, ypoints, 3);
    }

    private void drawLeftSensor(int startx, int starty, int length, int status) {


        if(status == 1)
        {
            panel.setColor(Color.GREEN);
        }
        else
        {
            panel.setColor(Color.RED);
        }


        int[] xpoints = new int[3];
        int[] ypoints = new int [3];
        xpoints[0] =  startx;
        ypoints[0] = (starty + (int)(.5 * length));
        xpoints[1] = startx-100;
        ypoints[1] = starty + length;
        xpoints[2] = startx-100;
        ypoints[2] = starty;

        panel.addFilledPolygon(xpoints, ypoints, 3);
    }

    private void drawRightSensor(int startx, int starty, int length, int status) {

        if(status == 1)
        {
            panel.setColor(Color.GREEN);
        }
        else
        {
            panel.setColor(Color.RED);
        }


        int[] xpoints = new int[3];
        int[] ypoints = new int [3];
        xpoints[0] =  startx+ length;
        ypoints[0] = (starty + (int)(.5 * length));
        xpoints[1] = (startx + length) + 100;
        ypoints[1] = starty + length;
        xpoints[2] = (startx + length) + 100;
        ypoints[2] = starty;

        panel.addFilledPolygon(xpoints, ypoints, 3);
    }

    private void drawFrontSensor(int startx, int starty, int length, int status) {


        if(status == 1)
        {
            panel.setColor(Color.GREEN);
        }
        else
        {
            panel.setColor(Color.RED);
        }


        int[] xpoints = new int[3];
        int[] ypoints = new int [3];
        xpoints[0] =  (startx + (int)(.5 * length));
        ypoints[0] = starty;
        xpoints[1] = startx;
        ypoints[1] = starty-100;
        xpoints[2] = startx + length;
        ypoints[2] = starty-100;

        panel.addFilledPolygon(xpoints, ypoints, 3);



    }


}
