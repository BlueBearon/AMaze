package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wm.cs.cs301.amazebychasepacker.R;
import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Button;
import android.content.Intent;

import java.util.Random;
import android.widget.Toast;

/**
 * @author Chase Packer
 *
 * This class is starting activity.  This class is incharge of taking user input for the maze that
 * will be generated and gives that information to GeneratingActivity.  It has many view objects
 * to take and record said input.
 */
public class AMazeActivity extends AppCompatActivity{

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    //maze variables
    private int SkillLevel = 0;
    private boolean hasRooms = true;
    private int builder = 0;
    private int seed = 13;

    //Preferences variables
    private static final String MYPREFS = "MyPreferences_001";
    public static final String Seed = "Seed";
    public static final String Skill = "SkillLevel";
    public static final String Rooms = "HasRooms";
    public static final String Gen = "Builder";


    /*
    UI components:

    Widgets:
    Seekbar for Skill Level

    Spinner for Builder


     */

    private SeekBar skilllevelBar;
    private CheckBox roomBox;
    private Spinner builderSpinner;
    private Button revisitButton;
    private Button newButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);


        /////SEEKBAR//////////////////////////////////////////////////////////
        skilllevelBar = (SeekBar) findViewById(R.id.seekBar);

        skilllevelBar.setProgress(0);
        //When user sets skill level, change skill level to selected skill level
        skilllevelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SkillLevel = progress;

                String msg = "Skill Level set to " + progress + ".";
                Log.v("AMazeActivity", msg);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////

        //Maze Generation////////////////////////////////////////////////////////////////////
        //spinner tutorial:  https://www.youtube.com/watch?v=PjW-XiQ6usI
        builderSpinner = (Spinner) findViewById(R.id.DriverSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.builders, R.layout.support_simple_spinner_dropdown_item);
        builderSpinner.setAdapter(adapter);
        //if user selects a builder, change builder to selected builder
        builderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String choice = parent.getItemAtPosition(position).toString();

                String msg = "Builder selected is " + choice + ".";
                Log.v("AMazeActivity", msg);
                //set builder
              switch(choice)
              {
                  case "Prim":
                  {
                      builder = 1;
                      break;
                  }
                  case "Boruvka":
                  {
                      builder = 2;
                      break;
                  }
                  default:
                  {
                      builder = 0;
                      break;
                  }
              }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /////////////////////////////////////////////////////////////////////////////////////


        //Has Rooms CheckBox/////////////////////////////////////////////////////////////////
        roomBox = (CheckBox) findViewById(R.id.checkBox);

        roomBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
               if(hasRooms)
               {
                   hasRooms = false;
               }
               else
               {
                   hasRooms = true;
               }

                String msg = "Has rooms is set to " + hasRooms + ".";
                Log.v("AMazeActivity", msg);
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////

        //Explore Button///////////////////////////////////////////////////////////////////////
        newButton = (Button) findViewById(R.id.Explore);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String msg = "Explore Selected";
                Log.v("AMazeActivity", msg);

              SwitchtoGenerating(true);
            }
        });
        ///////////////////////////////////////////////////////////////////////////////////////

        //Revisit Button//////////////////////////////////////////////////////////////////////
        revisitButton = (Button) findViewById(R.id.Revisit);

        revisitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = "Revist Selected";
                Log.v("AMazeActivity", msg);
                SwitchtoGenerating(false);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sends the speicfied parameters to Generating Activity and switches to Activity.
     *If explore maze is selected, it generates a new seed.
     * @param newMaze: whether or not new seed needs to be created.
     */
    private void SwitchtoGenerating(boolean newMaze)
    {
        Intent toGenerating = new Intent(this, GeneratingActivity.class);

        SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();


        Random gen = new Random();

        if(newMaze == true)
        {
           seed = gen.nextInt();
           storeMazeInfo(seed, SkillLevel, hasRooms, builder);
        }
        else
        {


            if(mySharedPreferences != null && mySharedPreferences.contains("Seed"))
            {
                Log.v("AMazeActivity" , "Getting Maze Info");
                getMazeInfo();
            }
            else
            {
                seed = gen.nextInt();
                storeMazeInfo(seed, SkillLevel, hasRooms, builder);
                Log.v("AMazeActivity" , "No maze is stored, generating new Maze");
            }
        }


        //set Extras and go to Generating
        toGenerating.putExtra("Seed", seed);

        toGenerating.putExtra("Skill Level", SkillLevel);
        toGenerating.putExtra("Has Rooms", hasRooms);
        toGenerating.putExtra("Builder", builder);

        startActivity(toGenerating);

    }

    /*
    Tutorial for storing data:
    https://www.youtube.com/watch?v=fJEFZ6EOM9o
     */

    /**
     * Takes seed, skilllevel, hasRooms, and builder of generated maze and stores them
     * in preferences for later use.
     * @param seed
     * @param skillLevel
     * @param hasRooms
     * @param builder
     */
    private void storeMazeInfo(int seed, int skillLevel, boolean hasRooms, int builder) {
        SharedPreferences mySharedPreferences = getSharedPreferences(MYPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();

        editor.putInt(Seed, seed);
        editor.putInt(Skill, skillLevel);
        editor.putBoolean(Rooms, hasRooms);
        editor.putInt(Gen, builder);

        editor.apply();

        Log.v("AMazeActivity", "Data Saved");
    }

    /**
     * If revisit is selected, this method accesses the variables of the previous maze
     * within preferences, and then sets those variables.
     */
    private void getMazeInfo()
    {
        Random gen = new Random();
        SharedPreferences pref = getSharedPreferences(MYPREFS, MODE_PRIVATE);

        seed = pref.getInt(Seed, gen.nextInt());
        SkillLevel = pref.getInt(Skill, SkillLevel);
        hasRooms = pref.getBoolean(Rooms, hasRooms);
        builder = pref.getInt(Gen, builder);
    }

}