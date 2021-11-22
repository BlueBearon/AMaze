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

import org.w3c.dom.Text;

import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityWinningBinding;

import edu.wm.cs.cs301.amazebychasepacker.R;

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
        energyConsumed = getIntent().getFloatExtra("Consumption", 3400);

        //get gui elements
        pathText = (TextView) findViewById(R.id.PathText);
        energyText = (TextView) findViewById(R.id.EnergyText);
        titleButton = (Button) findViewById(R.id.WinToTitleButton);

        //set text contents
        String msgPath = "Pathlength:  " + pathLength;
        String msgEnergy = "Energy Consumed:  " + energyConsumed;

        //energyconsumed will be negative if coming from Manual
        //If we come from manual, energyConsumed is irrelevant, so don't show it.
        if(energyConsumed < 0)
        {
            energyText.setVisibility(View.INVISIBLE);
        }

        pathText.setText(msgPath);
        energyText.setText(msgEnergy);

        //if title button is selected, change to Title
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
     * Switches Activity to AMazeActivity
     */
    private void switchToTitle()
    {
        Intent toTitle = new Intent (this, AMazeActivity.class);
        startActivity(toTitle);
    }
}