package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import edu.wm.cs.cs301.amazebychasepacker.R;
import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.generation.MazeContainer;

public class PlayAnimationActivity extends PlayingActivity {

    private AppBarConfiguration appBarConfiguration;

    //Driving Information//////////////////////////////////
    private int DriverV = 0;
    private RobotDriver driver;
    private Robot robot;
    private String RobotConfig;
    private Thread t;
    private boolean done = false;
    /*
    private Driver theDriver;
    private Robot theRobot;
     */

    ///////////////////////////////////////////////////////

    private boolean showMapV = true;
    private double mapScale = 1.0;
    private boolean active = false;
    private int animationSpeed = 2;
    private int failure_cause = 0;


    //Hardcoded P6 values///
    private float consumedEnergy = 2400;
    private int pathLength = 100;
    ////////////////////////////


    Maze theMaze;

    //GUI Elements//
    ProgressBar consumption;
    CheckBox ShowMap;
    Button decreaseScale;
    Button increaseScale;
    Button winning;
    Button losing;
    Button start;
    SeekBar animation;
    ImageView sensorStatus;

    ///////////////////////////


    PlayingControl game = new PlayingControl();

    private boolean finished = false;

    Animation go;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DriverV = getIntent().getIntExtra("Driver", 0);
        RobotConfig = getIntent().getStringExtra("RobotConfig");

        switch(DriverV)
        {
            case 2:
            {
                driver = new WizardJump();
                break;
            }
            case 3:
            {
                driver = new Wallfollower();
                break;
            }
            default:
            {
                driver = new Wizard();
                break;
            }
        }

        if(this.RobotConfig.contains("0"))
        {
            robot = new UnreliableRobot(this, this.RobotConfig);
        }
        else
        {
            robot = new ReliableRobot(this);
        }

        driver.setRobot(robot);

        driver.setMaze(GeneratingActivity.finishedMaze);

        theMaze = GeneratingActivity.finishedMaze;


        setContentView(R.layout.activity_play_animation);

        consumption = (ProgressBar) findViewById(R.id.EnergyLeft);
        consumption.setProgress(3500);

        String msg = "Driver:  " + DriverV + " RobotConfig:  " + RobotConfig;
        Log.v("PlayAnimationActivity", msg);
        Snackbar.make(consumption, msg, Snackbar.LENGTH_SHORT).show();

        ShowMap = (CheckBox) findViewById(R.id.showMap);
        ShowMap.setChecked(true);

        decreaseScale = (Button) findViewById(R.id.Decrease_Scale);
        increaseScale = (Button) findViewById(R.id.Increase_Scale);


        try {
            game.setMazeConfiguration(theMaze);
            game.start(this);
            Snackbar.make(winning, "Start Succeeded", Snackbar.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            Log.v("PlayAnimationActivity", e.toString());
            Snackbar.make(winning, "Start Failed", Snackbar.LENGTH_LONG).show();
        }

        theMaze = GeneratingActivity.finishedMaze;

        start = (Button) findViewById(R.id.ActivateButton);
        animation = (SeekBar) findViewById(R.id.SpeedBar);
        animation.setProgress(2);

        //Depending on the sensor status of robot, the image will change on screen.  Will be
        //implemented further in project 7
        sensorStatus = (ImageView) findViewById(R.id.SensorStatus);
        Drawable sensorImage = getResources().getDrawable(R.drawable.s1100, getTheme());
        sensorStatus.setImageDrawable(sensorImage);

        //change showMap setting if pressed.
        ShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showMapV)
                {
                    showMapV = false;
                }
                else
                {
                    showMapV = true;
                }

                String msg = "Show Map:  " + showMapV;
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(ShowMap, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
        //decrease map scale if pressed.
        decreaseScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mapScale > .10)
                {
                    mapScale -= .10;
                }

                String msg = "New Scale:  " + mapScale;
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(decreaseScale, msg, Snackbar.LENGTH_SHORT).show();

            }
        });
        //increase map scale if pressed
        increaseScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapScale < 5)
                {
                    mapScale += .10;
                }

                String msg = "New Scale:  " + mapScale;
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(increaseScale, msg, Snackbar.LENGTH_SHORT).show();
            }
        });

        //if start/stop button is pressed, either activate or deactivate driver depending on state
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(active)
                {
                    stopDriver();
                }
                else
                {
                    startDriver();
                }

                String msg = "Start/Stop button Selected";
                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(increaseScale, msg, Snackbar.LENGTH_SHORT).show();
            }
        });
        //if animation speed bar is changed, change the animation speed.
        animation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                animationSpeed = progress;

                String msg = "Animation speed set to " + progress + " .";

                Log.v("PlayAnimationActivity", msg);
                Snackbar.make(animation, msg, Snackbar.LENGTH_SHORT).show();

                if(active)
                {
                    go.speedToDelay();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public int[] getCurrentPosition()
    {
        return game.getCurrentPosition();
    }

    public CardinalDirection getCurrentDirection() {
        return game.getCurrentDirection();
    }

    public boolean keyDown(Constants.UserInput key, int value) {
        // delegated to state object
        return game.keyDown(key, value);
    }

    public Maze getMazeConfiguration() {
        return GeneratingActivity.finishedMaze;
    }


    /**
     * This method facilitates the robot jumping through the maze by
     * directly changing it through the current state.
     *
     * @param x position
     * @param y position
     */
    public void setNewPosition(int x, int y)
    {

        game.setCurrentPosition(x, y);

    }

    @Override
    public void onBackPressed()
    {
        switchToTitle();
    }

    /**
     * switches to AMazeActivity
     */
    public void switchToTitle() {
        Intent toTitle = new Intent(this, AMazeActivity.class);
        startActivity(toTitle);
    }

    private void stopDriver() {
        //project 7
        active = false;

        t.interrupt();
    }

    private void startDriver() {
        //project 7
        active = true;

        go = new Animation();

        t = new Thread(go);

        t.start();


    }

    /**
     * Switches to WinningActivity with path and consumption
     */
    public void switchToWinning() {
        Intent toWinning = new Intent(this, WinningActivity.class);
        consumedEnergy = driver.getEnergyConsumption();
        pathLength = driver.getPathLength();
        toWinning.putExtra("Consumption", consumedEnergy);
        toWinning.putExtra("path", pathLength);
        startActivity(toWinning);
    }

    /**
     * Switches ot LosingActivity and sends path and consumption
     */
    private void switchToLosing() {
        Intent toLosing = new Intent(this, LosingActivity.class);
        toLosing.putExtra("Consumption", consumedEnergy);
        toLosing.putExtra("path", pathLength);
        toLosing.putExtra("Failure", failure_cause);
        startActivity(toLosing);
    }

    public void setSensors(String SensorString)
    {
        RobotConfig = SensorString;
    }

    public boolean hasWon()
    {
        return finished;
    }

    public void notifyWin() {
        done = true;

        stopDriver();

        pathLength = driver.getPathLength();

        consumedEnergy = driver.getEnergyConsumption();

        switchToWinning();


    }

    private class Animation implements Runnable
    {
        private int delay;

        //speed -> delay
        //0 - 1000 ms
        //1 - 750 ms
        //2 - 500 ms
        //3 - 250 ms
        //4 - 50 ms

        public void speedToDelay() {
            switch (animationSpeed) {
                case 1: {
                    delay = 750;
                    break;
                }
                case 2: {
                    delay = 500;
                    break;
                }
                case 3: {
                    delay = 250;
                    break;
                }
                case 4: {
                    delay = 25;
                    break;
                }
                default: {
                    delay = 1000;
                    break;
                }
            }
        }

        public Animation()
        {
             speedToDelay();
        }


        @Override
        public void run()
        {

            try
            {
                while (!done)
                {
                    if(theMaze.getDistanceToExit(getCurrentPosition()[0],getCurrentPosition()[1]) == 1)
                    {
                        done = true;
                    }
                    //1 step to exit
                    driver.drive1Step2Exit();

                    int showConsumption = (int) (3500 - driver.getEnergyConsumption());

                    consumption.setProgress(showConsumption);
                    Thread.sleep(delay);

                }


            }
            catch (InterruptedException e)
            {
                active = false;
            }
            catch(Exception e)
            {
                active = false;
                //add code to get failure condition

                pathLength = driver.getPathLength();

                consumedEnergy = driver.getEnergyConsumption();

                String msg = e.toString();

                Log.v("PlayAnimation", msg);


                switchToLosing();
            }
            catch(AssertionError e)
            {
                active = false;
                //add code to get failure condition

                pathLength = driver.getPathLength();

                consumedEnergy = driver.getEnergyConsumption();

                String msg = e.toString();

                Log.v("PlayAnimation", msg);

                switchToLosing();
            }


        }

        public void setSpeed(int speed)
        {
            //Filler code for now, this is not how delay is calculated
            speedToDelay();

        }


    }

}