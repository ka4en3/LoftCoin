package com.akchimwf.loftcoin1.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.intercepting.SingleActivityFactory;

import com.akchimwf.loftcoin1.ui.main.MainActivity;
import com.akchimwf.loftcoin1.ui.welcome.WelcomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

/*Only work with SplashActivity here*/
@RunWith(AndroidJUnit4.class)
public class SplashActivityTest {

    /*pre-configure SplashActivity, override NoopIdling with TestIdling */
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

    /*Method name can be voluntary*/
    @Before     //call this method before every test
    public void setUp() throws Exception {
        final Context context = ApplicationProvider.getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        idling = new TestIdling();
        /*register Espresso idling resource*/
        IdlingRegistry.getInstance().register(idling.resource);
    }

    @Test
    public void open_welcome_activity_if_first_run() throws InterruptedException {
        prefs.edit().putBoolean(WelcomeActivity.KEY_SHOW_WELCOME, true).apply();

        /*THIS IS ACTUAL WAY TO LAUNCH ACTIVITIES IN TESTS*/
        /*ActivityScenario provides APIs to start and drive an Activity's lifecycle state for testing. */
//        final ActivityScenario<SplashActivity> scenario = ActivityScenario.launch(SplashActivity.class);

        /*DEPRECATED WAY*/
        rule.launchActivity(new Intent());   //Intent = SingleActivityFactory.create(Intent intent)

        /*listen to intents*/
        /*check(catch) the intent to start WelcomeActivity*/
        Intents.init();       //Initializes Intents and begins recording intents

        /*NOT A GOOD WAY as it takes 1 sec for every UI test probably*/
//        TimeUnit.SECONDS.sleep(1);   //because SplashActivity waits 0.5 sec before start WelcomeActivity

        /*GOOD WAY - to use IdlingResource, which triggers test execution according to Activity states */

        /*Asserts that the given matcher matches one and only one intent sent by the application under test.*/
        Intents.intended(IntentMatchers.hasComponent(WelcomeActivity.class.getName()));
    }

    @Test
    public void open_main_if_start_working_being_clicked() throws InterruptedException {
        prefs.edit().putBoolean(WelcomeActivity.KEY_SHOW_WELCOME, false).apply();

        /*DEPRECATED WAY*/
        rule.launchActivity(new Intent());   //Intent = SingleActivityFactory.create(Intent intent)

        /*start of a testing*/
        /*check(catch) the intent to start MainActivity*/
        Intents.init();       //Initializes Intents and begins recording intents

        /*Asserts that the given matcher matches one and only one intent sent by the application under test.*/
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    /*Method name can be voluntary*/
    @After  //call this method after every test
    public void tearDown() throws Exception {
        /*register Espresso idling resource*/
        IdlingRegistry.getInstance().unregister(idling.resource);
        /*Clears Intents state. Must be called after each test case.*/
        Intents.release();
    }

    private static class TestIdling implements SplashIdling {

        /*Espresso provides*/
        /*IdlingResource: These resources are required by Espresso to provide synchronisation against your application code.*/
        final CountingIdlingResource resource = new CountingIdlingResource("splash");

        @Override
        public void busy() {
            resource.increment();
        }

        @Override
        public void idle() {
            resource.decrement();
        }
    }
}

