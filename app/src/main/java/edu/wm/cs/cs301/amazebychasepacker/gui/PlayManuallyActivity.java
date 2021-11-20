package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

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
    private ActivityPlayManuallyBinding binding;

    private boolean fullMazeViewValue = false;
    private boolean showSolutionValue = false;
    private boolean showVisableWalls = false;
    private double MapScale = 1.0;
    private int PathLength = 0;

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

        decreaseScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MapScale > .10)
                {
                    MapScale -= .10;
                }

            }
        });

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

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathLength++;
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_play_manually);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    private void switchToWinning() {
        Intent toWinning = new Intent(this, WinningActivity.class);
        toWinning.putExtra("PathLength", PathLength);
        toWinning.putExtra("EnergyConsumed", -42);
        startActivity(toWinning);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_play_manually);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}