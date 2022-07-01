package com.akchimwf.loftcoin1.ui.splash;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.akchimwf.loftcoin1.R;
import com.akchimwf.loftcoin1.ui.main.MainActivity;
import com.akchimwf.loftcoin1.ui.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    /*needs Handler to postdelay start of activity (Main or Welcome)*/
    private final Handler handler = new Handler(Looper.getMainLooper());

    /*use Runnable interface with lambda*/
    private Runnable goNext;

    private SharedPreferences prefs;

    /*only for testing -> must be visible in androidTest -> package private*/
    @VisibleForTesting
    SplashIdling idling = new NoopIdling();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*store 'Show WelcomeActivity' flag at prefs */
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true)) {
            /*show WelcomeActivity*/
            goNext = () -> {
                startActivity(new Intent(this, WelcomeActivity.class));
                idling.idle();   //flag SplashActivity free
            };
        } else {
            /*if btnStart pressed -> go directly to MainActivity, no show WelcomeActivity*/
            goNext = () -> {
                startActivity(new Intent(this, MainActivity.class));
                idling.idle();   //flag SplashActivity free
            };
        }
        handler.postDelayed(goNext, 500);
        idling.busy();           //flag SplashActivity busy = wait 0.5 seconds
    }

    @Override
    protected void onStop() {
        /*if WelcomeActivity stopped -> remove callback from Handler*/
        handler.removeCallbacks(goNext);
        super.onStop();
    }

    /*Only for purposes of testing*/
    /*Noop = no operations*/
    private static class NoopIdling implements SplashIdling {

        @Override
        public void busy() {

        }

        @Override
        public void idle() {

        }
    }
}
