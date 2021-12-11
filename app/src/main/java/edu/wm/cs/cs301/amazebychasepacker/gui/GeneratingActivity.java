package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import edu.wm.cs.cs301.amazebychasepacker.generation.Factory;
import edu.wm.cs.cs301.amazebychasepacker.generation.Floorplan;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;
import edu.wm.cs.cs301.amazebychasepacker.generation.MazeFactory;
import edu.wm.cs.cs301.amazebychasepacker.generation.Order;

import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityGeneratingBinding;

import edu.wm.cs.cs301.amazebychasepacker.R;

/**
 * @author Chase Packer
 * This Generating Activity is responsible for generating the maze.  It works
 * with builder, factory and order to build a maze.  The activity also asks to user
 * for a driver (or manual) and a configuration for the sensors on the robot if a driver
 * is selected.
 * It recieves info from AMazeActivity, and then sends the maze and driver info to
 * either PlayManually or PlayAnimation
 */
public class GeneratingActivity extends AppCompatActivity implements Order{

    private AppBarConfiguration appBarConfiguration;
    private ActivityGeneratingBinding binding;

    private SoundPool sounds;
    private int genSound;
    MediaPlayer media;

    //////Driving Information/////////////////////////
    private String[] drivers = {"Manual", "Wall Follower", "Wizard", "Jumping Wizard"};
    private int driver = -1;
    private Thread t;
    private boolean driverSelected = false;

    private String[] RobotConfigs = {"Premium", "Mediocre", "Soso", "Shaky"};
    private String config = "1111";


    private boolean mazeBuilt = false;
    ///////////////////////////////////////////////////


    ///////MazeGenerationInformation/////////////////////
    private int SkillLevel = 0;
    private boolean hasRooms = true;
    //private Order.Builder[] = {Order.Builder.DFS, Order.Builder.Prim, Order.Builder.Boruvka};
    private int builderint = 0;
    private int seed = 13;

    private String filename;
    private Order.Builder builder;
    private int mazeProgress = 0;
    protected Factory factory;
    boolean started;


    public static Maze finishedMaze;

    //////////////////////////////////////////////////////////

    //GUI elements//////////////////////////////////////////////
    TextView percent;
    Spinner driverSpinner;
    Spinner configSpinner;
    ProgressBar progress;

    //initialize variables for order.
    public GeneratingActivity() {
        filename = null;
        factory = new MazeFactory();
        SkillLevel = 0;
        builder = Order.Builder.DFS;
        hasRooms = true;
        started = false;
        seed = 13;
    }
    /////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generating);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();

        sounds = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        genSound = sounds.load(this, R.raw.loading, 1);

        media = MediaPlayer.create(this, R.raw.loading);

        //Get Info from AMazeActivity////////////////////////////////////////////////
        SkillLevel = getIntent().getIntExtra("Skill Level", 0);
        hasRooms = getIntent().getBooleanExtra("Has Rooms", true);
        builderint = getIntent().getIntExtra("Builder", 0);
        seed = getIntent().getIntExtra("Seed", 13);

        //set the builder
        switch(builderint)
        {

            case 1:
            {
                setBuilder(Builder.Prim);
                break;
            }
            case 2:
            {
                setBuilder(Builder.Boruvka);
                break;
            }
            default:
            {
                setBuilder(Builder.DFS);
                break;
            }
        }


        //////////////////////////////////////////////////////////////////////////////

        //Progress///////////////////////////////////////////////////////////////////////
        percent = (TextView) findViewById(R.id.PercentageText);

        percent.setText("0%");

        progress = (ProgressBar) findViewById(R.id.progressBar);

        progress.setProgress(0);
        ////////////////////
        String msg = "Skill Level: " + SkillLevel + " hasRooms:  " + hasRooms + " Builder:  " + " Seed:  " + seed;
        Log.v("GeneratingActivity", msg);
        ////////////////////////



        //Generate the maze.
        StartGenerating();



        //if driverSelected is true, switchToPlaying
        //else, create pop up telling user to indicate driver
        /////////////////////////////////////////////////////////////////////////////////

        //Driver Spinner/////////////////////////////////////////
        driverSpinner = (Spinner) findViewById(R.id.DriverSpinner);

        ArrayAdapter<CharSequence> driverAdapter = ArrayAdapter.createFromResource(this, R.array.drivers, R.layout.support_simple_spinner_dropdown_item);
        driverSpinner.setAdapter(driverAdapter);
        //when user selects a driver, change driver to selected driver.
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();

                String msg = "Driver selected:  " + choice;
                Log.v("GeneratingActivity", msg);
                //set driver
                switch(choice)
                {
                    case "Manual":
                    {
                        driver = 0;
                        break;
                    }
                    case "Wizard": {
                        driver = 1;
                        break;
                    }
                    case "JumpingWizard":
                    {
                        driver = 2;
                        break;
                    }
                    case "Wall Follower":
                    {
                        driver = 3;
                        break;
                    }
                    default:
                    {
                        driver = -1;
                        break;
                    }

                }

                if(driver >= 0)
                {
                    driverSelected = true;
                }

                if(mazeBuilt)
                {
                    switchToPlaying();
                }
                else
                {
                    Snackbar.make(view, "Please Wait for Maze to be Generated", Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////////////////////////////////

        //Config Spinner/////////////////////////////////////////
        configSpinner = (Spinner) findViewById(R.id.ConfigSpinner);

        ArrayAdapter<CharSequence> configAdapter = ArrayAdapter.createFromResource(this, R.array.robotConfigs, R.layout.support_simple_spinner_dropdown_item);
        configSpinner.setAdapter(configAdapter);

        //when user makes a choice, change the robot config
        configSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = parent.getItemAtPosition(position).toString();

                String msg = "Robot Config selected:  " + choice;
                Log.v("GeneratingActivity", msg);
                //set robot config
                switch(choice)
                {
                    case "Mediocre":
                    {
                        config = "1100";
                        break;
                    }
                    case "So so":
                    {
                        config = "0011";
                        break;
                    }
                    case "Shaky":
                    {
                        config = "0000";
                        break;
                    }
                    default:
                    {
                        config = "1111";
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /////////////////////////////////////////////////////////
    }

    @Override
    public void onBackPressed()
    {
        //Credit to Tutorial:  https://www.youtube.com/watch?v=ODg638Qzp8g
        switchToTitle();
    }

    /**
     * Stops the progress bar thread and switches to title
     */
    private void switchToTitle()
    {
        try {
            t.interrupt();
        }
        catch(Exception e)
        {

        }
        Intent toTitle = new Intent (this, AMazeActivity.class);
        startActivity(toTitle);
    }

    /**
     * Depending on the value of driver, will activate either PlayAnimation or PlayManually.
     * Sends driver and robot config info if necessary.
     */
    private void switchToPlaying()
    {


        if(driver > 0)
        {
            //create intent with driver and robot config info
            Intent toAnimation = new Intent(this, PlayAnimationActivity.class);
            toAnimation.putExtra("Driver", driver);
            toAnimation.putExtra("RobotConfig", config);
            //send to PlayAnimationActivity
            startActivity(toAnimation);
        }
        else
        {
            //create intent
            Intent toManual = new Intent(this, PlayManuallyActivity.class);
            //send to PlayManuallyActivity
            startActivity(toManual);
        }
    }

    @Override
    public int getSkillLevel() {
        return this.SkillLevel;
    }

    public void setSkillLevel(int skilllevel)
    {
        this.SkillLevel = skilllevel;
    }

    @Override
    public Builder getBuilder() {
        return this.builder;
    }

    /**
     * Sets the builder for Maze Generation
     * @param builder
     */
    public void setBuilder(Builder builder)
    {
        this.builder = builder;
    }

    @Override
    public boolean isPerfect() {
        return !hasRooms;
    }

    @Override
    public int getSeed() {
        return this.seed;
    }


    @Override
    public void updateProgress(int percentage)
    {
        //if progress is different and less than 100%
        if(this.mazeProgress < percentage && percentage <= 100)
        {
          this.mazeProgress = percentage;
          progress.setProgress(percentage);

          String msg = mazeProgress + "%";
          percent.setText(msg);
          progress.setProgress(mazeProgress, true);
        }
    }

    /**
     * This method is in charge of starting the maze generation, calling factory.order(this)
     */
    public void StartGenerating()
    {
        started = true;

        mazeProgress = 0;

        media.start();

        if(filename == null)
        {
            assert null != factory:  "GeneratingActivity:  factory must be present";

            factory.order(this);
        }
    }

    @Override
    public void deliver(Maze mazeconfig)
    {
        media.stop();
        //sounds.pause(genSound);
       if(Floorplan.deepdebugWall)
       {
           mazeconfig.getFloorplan().saveLogFile(Floorplan.deepedebugWallFileName);
       }

       finishedMaze = mazeconfig;

        mazeBuilt = true;

        if(driverSelected)
        {
            Log.v("PlayingActivity", "Switching to Playing");
            switchToPlaying();
        }
        else
        {
            Snackbar.make(driverSpinner, "Please pick a driver", Snackbar.LENGTH_SHORT).show();
        }

    }


    /**
     * This class is the runnable object that simulates maze generation.
     * Every second, progress bar increases by 10.  If mazeDriver is selected
     * it will call SwitchtoPlaying().
     */
    private class progressSimulation implements Runnable
    {
        public progressSimulation()
        {}

        @Override
        public void run()
        {
            try
            {
                while (mazeProgress < 100)
                {
                    mazeProgress += 10;
                    String msg = mazeProgress + "%";
                    percent.setText(msg);
                    progress.setProgress(mazeProgress, true);
                    Thread.sleep(1000);
                }

                mazeBuilt = true;
                if(driverSelected)
                {
                    Log.v("PlayingActivity", "Switching to Playing");
                    switchToPlaying();
                }
                else
                {
                    Snackbar.make(driverSpinner, "Please pick a driver", Snackbar.LENGTH_SHORT).show();
                }
            }
            catch (InterruptedException e)
            {
                mazeProgress = 0;
            }


        }
    }

}