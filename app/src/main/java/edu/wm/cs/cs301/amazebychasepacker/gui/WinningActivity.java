package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

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

import org.w3c.dom.Text;

import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityWinningBinding;

import edu.wm.cs.cs301.amazebychasepacker.R;

/**
 * @author Chase Packer
 * This class is responsible for recieving and displaying relevant information
 * after the user or RobotDriver has navigated the maze successfully.  Then sends
 * the user back to AMazeActivity.
 */
public class WinningActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private int pathLength = 0;
    private float energyConsumed = 3400;

    //GUI////
    TextView pathText;
    TextView energyText;
    Button titleButton;
    ///////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_winning);

        //get info from previous activity
        pathLength = getIntent().getIntExtra("path", 0);
        energyConsumed = getIntent().getFloatExtra("Consumption", 9000);

        //get gui elements
        pathText = (TextView) findViewById(R.id.PathText);
        energyText = (TextView) findViewById(R.id.EnergyText);
        titleButton = (Button) findViewById(R.id.WinToTitleButton);

        String msg = "pathLength:  " + pathLength + " EnergyConsumed:  " + energyConsumed;
        Log.v("WinningActivity", msg);

        //set text contents
        String msgPath = "Pathlength:  " + pathLength;
        String msgEnergy = "Energy Consumed:  " + energyConsumed;

        //energyconsumed will be negative if coming from Manual
        //If we come from manual, energyConsumed is irrelevant, so don't show it.
        if(energyConsumed >= 9000)
        {
            energyText.setVisibility(View.INVISIBLE);
        }

        pathText.setText(msgPath);
        energyText.setText(msgEnergy);

        MediaPlayer media = MediaPlayer.create(this, R.raw.victory);
        media.start();

        //if title button is selected, change to Title
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "Going to title screen";
                Log.v("WinningActivity", msg);
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
     * Switches Activity to AMazeActivity
     */
    private void switchToTitle()
    {
        Intent toTitle = new Intent (this, AMazeActivity.class);
        startActivity(toTitle);
    }
}