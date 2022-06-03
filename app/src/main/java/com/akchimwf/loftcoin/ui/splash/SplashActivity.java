package com.akchimwf.loftcoin.ui.splash;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.ui.main.MainActivity;
import com.akchimwf.loftcoin.ui.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    /*needs Handler to postdelay start of activity (Main or Welcome)*/
    private final Handler handler = new Handler(Looper.getMainLooper());

    /*use Runnable interface with lambda*/
    private Runnable goNext;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*store 'Show WelcomeActivity' flag at prefs */
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true)) {
            /*show WelcomeActivity*/
            goNext = () -> startActivity(new Intent(this, WelcomeActivity.class));
        } else {
            /*if btnStart pressed -> go directly to MainActivity, no show WelcomeActivity*/
            goNext = () -> startActivity(new Intent(this, MainActivity.class));
        }
        handler.postDelayed(goNext, 1500);
    }

    @Override
    protected void onStop() {
        /*if WelcomeActivity stopped -> remove callback from Handler*/
        handler.removeCallbacks(goNext);
        super.onStop();
    }
}
