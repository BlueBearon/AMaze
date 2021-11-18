package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;

import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityGeneratingBinding;

import edu.wm.cs.cs301.amazebychasepacker.R;

public class GeneratingActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityGeneratingBinding binding;

    //////Driving Information/////////////////////////
    private String[] drivers = {"Manual", "Wall Follower", "Wizard", "Jumping Wizard"};
    private int driver = 0;

    private boolean driverSelected = false;

    private String[] RobotConfigs = {"Premium", "Mediocre", "Soso", "Shaky"};
    private int config = 0;
    ////////////////////////////////////////////////////

    ///////MazeGenerationInformation/////////////////////
    private int SkillLevel = 0;
    private boolean hasRooms = true;
    //private Order.Builder[] = {Order.Builder.DFS, Order.Builder.Prim, Order.Builder.Boruvka};
    private int builder = 0;
    private int seed = 13;
    //////////////////////////////////////////////////////////




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generating);

        binding = ActivityGeneratingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.GeneratingToolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_generating);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //Get Info from AMazeActivity////////////////////////////////////////////////
        SkillLevel = getIntent().getIntExtra("Skill Level", 0);
        hasRooms = getIntent().getBooleanExtra("Has Rooms", true);
        builder = getIntent().getIntExtra("Builder", 0);
        seed = getIntent().getIntExtra("Seed", 13);
        //////////////////////////////////////////////////////////////////////////////

        //Progress///////////////////////////////////////////////////////////////////////

        //if driverSelected is true, switchToPlaying
        //else, create pop up telling user to indicate driver
        /////////////////////////////////////////////////////////////////////////////////

        //Driver Spinner/////////////////////////////////////////


        //set driverSelected to true

        //if progress is less than 100%, create pop up telling user to wait for generation
        //else, switchToPlaying
        /////////////////////////////////////////////////////////

        //Config Spinner/////////////////////////////////////////

        /////////////////////////////////////////////////////////


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //interrupt generation
                switchToTitle();
            }
        };
    }

    private void switchToTitle()
    {
        Intent toTitle = new Intent (this, AMazeActivity.class);
        startActivity(toTitle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_generating);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

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

}