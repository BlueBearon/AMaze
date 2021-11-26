package edu.wm.cs.cs301.amazebychasepacker.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import edu.wm.cs.cs301.amazebychasepacker.R;
import edu.wm.cs.cs301.amazebychasepacker.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;

public class PlayingActivity extends AppCompatActivity {

    public PlayingControl game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playing);
    }

    public void switchToWinning() {
        Intent toWinning = new Intent(this, WinningActivity.class);
        startActivity(toWinning);
    }

    public int[] getCurrwentPosition()
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


}