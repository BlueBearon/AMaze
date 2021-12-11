package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import edu.wm.cs.cs301.amazebychasepacker.R;

/**
 * @author Chase Packer
 * This class is responsible for recieving and displaying relevant information
 * after the RobotDriver has failed to naviagate the maze.  Then sends
 * the user back to AMazeActivity.
 */
public class LosingActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private int pathLength = 0;
    private float energyConsumed = 3500;

    private String LosingMessage1 = "Emergency!";
    private String FailureMessage = "Vehicle has run out of Energy";
    private String LosingMessage2 = "Attempting Recovery...";



    //Gui////
    TextView pathText;
    TextView energyText;
    Button titleButton;

    TextView losingCause;
    //////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_losing);

        //get info from previous activity
        pathLength = getIntent().getIntExtra("path", 0);
        energyConsumed = getIntent().getFloatExtra("Consumption", 3500);
        int cause = getIntent().getIntExtra("Failure", 0);

        //get gui elements
        pathText = (TextView) findViewById(R.id.PathTextLosing);
        energyText = (TextView) findViewById(R.id.EnergyTextLosing);
        titleButton = (Button) findViewById(R.id.LoseToTitleButton);
        losingCause = (TextView) findViewById(R.id.FailureCauseText);

        String msg = "pathLength:  " + pathLength + " EnergyConsumed:  " + energyConsumed;
        Log.v("LosingActivity", msg);
        Snackbar.make(pathText, msg, Snackbar.LENGTH_SHORT).show();

        //set text to display on screen
        String msgPath = "Pathlength:  " + pathLength;
        String msgEnergy = "Energy Consumed:  " + energyConsumed;

        pathText.setText(msgPath);
        energyText.setText(msgEnergy);


        MediaPlayer media = MediaPlayer.create(this, R.raw.wilhelmscream);

        media.start();

        while(media.isPlaying())
        {

        }

        MediaPlayer media2 = MediaPlayer.create(this, R.raw.failure);

        media2.start();



        if(cause == 1)
        {
            losingCause.setText("Collision with Asteroid");
        }
        else
        {
            losingCause.setText("Ran out of Energy");
        }

        //if title button is clicked, go to title screen.
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Going to title screen";
                Log.v("LosingActivity", msg);
                Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
                switchToTitle();
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        switchToTitle();
    }

    /**
     * This method, when called, creates the intent and changes activity to AMazeActivity
     */
    private void switchToTitle()
    {
        Intent toTitle = new Intent (this, AMazeActivity.class);
        startActivity(toTitle);
    }
}