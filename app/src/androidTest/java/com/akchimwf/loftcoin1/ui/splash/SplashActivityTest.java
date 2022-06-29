package com.akchimwf.loftcoin1.ui.splash;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.intercepting.SingleActivityFactory;

import com.akchimwf.loftcoin1.splash.SplashActivity;
import com.akchimwf.loftcoin1.splash.SplashIdling;
import com.akchimwf.loftcoin1.ui.welcome.WelcomeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

/*Only work with SplashActivity here*/
@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    /*pre-configure SplashActivity, override SplashIdling*/
    @Rule  //this field is using to configure test environment, have to be public
    public final ActivityTestRule<SplashActivity> rule = new ActivityTestRule<>(
            new SingleActivityFactory<SplashActivity>(SplashActivity.class) {
                @Override
                protected SplashActivity create(Intent intent) {
                    final SplashActivity splashActivity = new SplashActivity();
                    splashActivity.idling = idling;
                    return splashActivity;
                }
            }, false, false);

    private SharedPreferences prefs;

    private TestIdling idling;

    @Before     //call this method before every test
    public void setUp() throws Exception {
        final Context context = ApplicationProvider.getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        idling = new TestIdling();
    }

    @Test
    public void open_welcome_activity_if_first_run() throws InterruptedException {
        prefs.edit().putBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true).apply();
        /*ActivityScenario provides APIs to start and drive an Activity's lifecycle state for testing. */
        final ActivityScenario<SplashActivity> scenario = ActivityScenario.launch(SplashActivity.class);
        /*start of a test*/
        /*check(catch) the intent to start WelcomeActivity*/
        Intents.init();       //Initializes Intents and begins recording intents

        /*not a good way as it takes 1 sec for every UI test probably*/
        TimeUnit.SECONDS.sleep(1);   //because SplashActivity waits 0.5 sec before start WelcomeActivity

        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity.class.getName()));  //Asserts that the given matcher matches one and only one intent sent by the application under test.
        Intents.release();    //Clears Intents state. Must be called after each test case.
    }

    private static class TestIdling implements SplashIdling {

        @Override
        public void busy() {

        }

        @Override
        public void idle() {

        }
    }
}

