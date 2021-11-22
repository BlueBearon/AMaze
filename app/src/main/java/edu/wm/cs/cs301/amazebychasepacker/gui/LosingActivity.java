package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import edu.wm.cs.cs301.amazebychasepacker.R;

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

        //set text to display on screen
        String msgPath = "Pathlength:  " + pathLength;
        String msgEnergy = "Energy Consumed:  " + energyConsumed;

        pathText.setText(msgPath);
        energyText.setText(msgEnergy);


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
                switchToTitle();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //interrupt generation
                switchToTitle();
            }
        };


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