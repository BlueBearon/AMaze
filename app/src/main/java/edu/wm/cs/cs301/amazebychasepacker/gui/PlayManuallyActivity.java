package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityPlayManuallyBinding;

import edu.wm.cs.cs301.amazebychasepacker.R;

public class PlayManuallyActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private boolean fullMazeViewValue = false;
    private boolean showSolutionValue = false;
    private boolean showVisableWalls = false;
    private double MapScale = 1.0;
    private int PathLength = 0;
    //GUI Elements//////
    CheckBox fullMazeView;
    CheckBox showSolution;
    CheckBox showWalls;
    Button decreaseScale;
    Button increaseScale;
    Button Go2Win;
    Button forward;
    Button backward;
    Button left;
    Button right;
    Button jump;
    ////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_manually);

        fullMazeView = (CheckBox) findViewById(R.id.showfullMazeView);
        showSolution = (CheckBox) findViewById(R.id.showSolution);
        showWalls = (CheckBox) findViewById(R.id.ShowWalls);

        fullMazeView.setChecked(true);
        showSolution.setChecked(true);
        showWalls.setChecked(true);

        //if fullMazeView is clicked, change setting
        fullMazeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullMazeViewValue)
                {
                    fullMazeViewValue = false;
                }
                else
                {
                    fullMazeViewValue = true;
                }

            }
        });

        //if showSolution is clicked, change setting
        showSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showSolutionValue)
                {
                    showSolutionValue = false;
                }
                else
                {
                    showSolutionValue = true;
                }
            }
        });

        //change setting of showVisableWalls if pressed
        showWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(showVisableWalls)
                {
                    showVisableWalls = false;
                }
                else
                {
                    showVisableWalls = true;
                }

            }
        });

        decreaseScale = (Button) findViewById(R.id.Decrease_Scale1);
        increaseScale = (Button) findViewById(R.id.Increase_Scale1);

        //decrease the scale of map if pressed
        decreaseScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MapScale > .10)
                {
                    MapScale -= .10;
                }

            }
        });

        //if increase scale is pressed, increase the scale
        increaseScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MapScale < 5)
                {
                    MapScale += .10;
                }
            }
        });



        Go2Win = (Button) findViewById(R.id.Go2Win);

        //if go2Win is pressed, go to WinningActivity
        Go2Win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchToWinning();

            }
        });

        forward = (Button) findViewById(R.id.Forward);
        backward = (Button) findViewById(R.id.Around);
        left = (Button) findViewById(R.id.Left);
        right = (Button) findViewById(R.id.Right);
        jump = (Button) findViewById(R.id.Jump);

        //if forward is pushed, add to pathlength
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathLength++;
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //if jump is pushed, add to pathlength
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathLength++;
            }
        });


        //if back is pressed, go to title screen
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //interrupt generation
                switchToTitle();
            }
        };


    }

    /**
     * switches to AMazeActivity
     */
    private void switchToTitle() {
        Intent toTitle = new Intent(this, AMazeActivity.class);
        startActivity(toTitle);
    }

    /**
     * Switches to WinningActivity with path and consumption parameters
     */
    private void switchToWinning() {
        Intent toWinning = new Intent(this, WinningActivity.class);
        toWinning.putExtra("path", PathLength);
        startActivity(toWinning);

    }

}