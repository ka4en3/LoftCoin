package com.akchimwf.loftcoin1.ui.welcome;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;

import com.akchimwf.loftcoin1.R;
import com.akchimwf.loftcoin1.ui.main.MainActivity;

@RunWith(AndroidJUnit4.class)
public class WelcomeActivityTest {
    @Test
    public void open_main_if_button_begin_pressed() {

        /*ACTUAL WAY TO LAUNCH ACTIVITIES IN TESTS*/
        ActivityScenario.launch(WelcomeActivity.class);

        /*listen to intents*/
        Intents.init();

        /*Performs the given action(s) on the view selected by the current view matcher.*/
        Espresso.onView(ViewMatchers.withId(R.id.btnStart)).perform(click());
        //        onView(withId(R.id.btnStart)).perform(click());   if replace with static import -> better to read

        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}