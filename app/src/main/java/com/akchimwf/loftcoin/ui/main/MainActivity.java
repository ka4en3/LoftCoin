package com.akchimwf.loftcoin.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*ActivityMainBinding class comes from 'viewBinding' at build.grade*/
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.toolbar);
        /*setContentView needs View -> use getRoot()*/
        setContentView(binding.getRoot());

        navController  = Navigation.findNavController(this, R.id.main_host);
        /*Sets up a NavigationBarView for use with a NavController.*/
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        /*Sets up a Toolbar for use with a NavController.*/
        /*AppBarConfiguration stands for disable navigate_back_button in a toolbar, instead set AppBarConfiguration with Menu of the bottomNav*/
        NavigationUI.setupWithNavController(binding.toolbar, navController, new AppBarConfiguration
                .Builder(binding.bottomNav.getMenu())
                .build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Navigate to current page to set correct Title at least*/
        navController.navigate(navController.getCurrentDestination().getId());
    }
}