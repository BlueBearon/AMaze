package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wm.cs.cs301.amazebychasepacker.R;
import edu.wm.cs.cs301.amazebychasepacker.generation.Maze;

/**
 * @author Chase Packer
 *
 * This class is in charge of handling manual navigation in the maze.  It works with PlayingControl
 * to handle actually playing through the maze.
 * This class is responsible for taking user input and giving it to PlayingControl.
 */
public class PlayManuallyActivity extends PlayingActivity {


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
    Button forward;
    Button backward;
    Button left;
    Button right;
    Button jump;
    MazePanel panel;
    ////////////////////////////////////////////
    //set PlayingControl
    PlayingControl game = new PlayingControl(false);
    Maze theMaze;

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

        theMaze = GeneratingActivity.finishedMaze;


        //set up PlayingControl for playing
        try {
            panel = (MazePanel) findViewById(R.id.ManualPanel);
            game.setMazeConfiguration(theMaze);
            game.start(this, panel);
        }
        catch(Exception e)
        {
            Log.v("PlayAnimationActivity", e.toString());
        }

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

                game.keyDown(Constants.UserInput.TOGGLEFULLMAP, 2);

                String msg = "fullMazeView:  " + fullMazeViewValue;
                Log.v("PlayAnimationActivity", msg);

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

                game.keyDown(Constants.UserInput.TOGGLESOLUTION, 2);

                String msg = "showSolution:  " + showSolutionValue;
                Log.v("PlayAnimationActivity", msg);
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

                game.keyDown(Constants.UserInput.TOGGLELOCALMAP, 2);

                String msg = "showWalls:  " + showVisableWalls;
                Log.v("PlayAnimationActivity", msg);

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

                game.keyDown(Constants.UserInput.ZOOMOUT,2);
                String msg = "New Scale:  " + MapScale;
                Log.v("PlayManuallyActivity", msg);
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

                game.keyDown(Constants.UserInput.ZOOMIN,2);

                String msg = "New Scale:  " + MapScale;
                Log.v("PlayManuallyActivity", msg);
            }
        });

        //set up inputs for moving throughout maze
        ConstraintLayout layout = findViewById(R.id.ManualLayout);

        SwipeListener swipeListener = new SwipeListener(layout);

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
                String msg = "forward pressed";
                Log.v("PlayManuallyActivity", msg);
                game.keyDown(Constants.UserInput.UP, 2);
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "back pressed";
                Log.v("PlayManuallyActivity", msg);
                game.keyDown(Constants.UserInput.DOWN, 2);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "left pressed";
                Log.v("PlayManuallyActivity", msg);
                game.keyDown(Constants.UserInput.LEFT, 2);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "right pressed";
                Log.v("PlayManuallyActivity", msg);
                game.keyDown(Constants.UserInput.RIGHT, 2);
            }
        });

        //if jump is pushed, add to pathlength
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathLength++;

                String msg = "jump pressed";
                Log.v("PlayManuallyActivity", msg);
                game.keyDown(Constants.UserInput.JUMP, 2);
            }
        });


    }

    /**
     *This class is responsible for handling Gesture controls
     */
    private class SwipeListener implements View.OnTouchListener{

        //Tutorial:  https://www.youtube.com/watch?v=vNJyU-XW8_Y

        GestureDetector gestureDetector;

        SwipeListener(View view){
            //thresholds for recongizing movement
            int threshold = 100;
            int velocity_threshold = 100;

            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onDown(MotionEvent e)
                {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    //differences between position of beginning and end of swipe
                    float xDiff = e2.getX() - e1.getX();
                    float yDiff = e2.getY() - e1.getY();

                    try
                    {
                        //if swipe is horizontal, i.e., swipe is mostly going horizontal
                        if(Math.abs(xDiff) > Math.abs(yDiff))
                        {
                            if(Math.abs(xDiff) > threshold  && Math.abs(velocityX) > velocity_threshold)//only if it is actually a swipe
                            {
                                if(xDiff > 0)
                                {
                                    //Swipe Right
                                    game.keyDown(Constants.UserInput.RIGHT, 2);
                                }
                                else
                                {
                                    //Swipe Left
                                    game.keyDown(Constants.UserInput.LEFT, 2);
                                }

                                return true;
                            }
                        }
                        else //swipe is vertical
                        {
                            if(Math.abs(yDiff) > threshold  && Math.abs(velocityY) > velocity_threshold)//only if it is actually a swipe
                            {
                                if(yDiff > 0)
                                {
                                    //Swipe Down
                                    game.keyDown(Constants.UserInput.DOWN, 2);
                                }
                                else
                                {
                                    //Swipe UP
                                    game.keyDown(Constants.UserInput.UP, 2);
                                    PathLength++;
                                }

                                return true;
                            }
                        }
                    }
                    catch(Exception e)
                    {

                    }

                    return false;



                }
            };

            gestureDetector = new GestureDetector(getApplicationContext(), listener);

            view.setOnTouchListener(this);

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
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

    /**
     * Switches to WinningActivity with path and consumption parameters
     */
    public void switchToWinning() {
        Intent toWinning = new Intent(this, WinningActivity.class);
        toWinning.putExtra("path", PathLength);
        startActivity(toWinning);

    }

}