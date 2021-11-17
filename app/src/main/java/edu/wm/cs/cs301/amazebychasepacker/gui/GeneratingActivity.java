package edu.wm.cs.cs301.amazebychasepacker.gui;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wm.cs.cs301.amazebychasepacker.databinding.ActivityGeneratingBinding;

import edu.wm.cs.cs301.amazebychasepacker.R;

public class GeneratingActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityGeneratingBinding binding;

    //////Driving Information/////////////////////////
    private String[] drivers = {"Manual", "Wall Follower", "Wizard", "Jumping Wizard"};
    private int driver = 0;

    private String[] RobotConfigs = {"Premium", "Mediocre", "Soso", "Shaky"};
    private int config = 0;
    ////////////////////////////////////////////////////

    ///////MazeGenerationInformation/////////////////////
    private int SkillLevel = 0;
    private boolean hasRooms = true;
    //private Order.Builder[] = {Order.Builder.DFS, Order.Builder.Prim, Order.Builder.Boruvka};
    private int seed = 13;
    //////////////////////////////////////////////////////////




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGeneratingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_generating);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_generating);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}