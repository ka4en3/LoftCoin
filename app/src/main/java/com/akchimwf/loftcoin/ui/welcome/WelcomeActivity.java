package com.akchimwf.loftcoin.ui.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import android.content.Intent;
import android.os.Bundle;

import com.akchimwf.loftcoin.databinding.ActivityWelcomeBinding;
import com.akchimwf.loftcoin.ui.widget.CircleIndicator;
import com.akchimwf.loftcoin.ui.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    /*flag to show or not WelcomeActivity */
    public static final String KEY_SHOW_WELCOME = "show_welcome";

    private ActivityWelcomeBinding binding;
    /*PagerSnapHelper helps to show RecycleView as ViewPager, we don't use ViewPager here*/
    private PagerSnapHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*ActivityWelcomeBinding class comes from 'viewBinding' at build.grade*/
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());

        /*setContentView needs View -> use getRoot()*/
        setContentView(binding.getRoot());

        /*set LayoutManager and adapter to RecycleView*/
        binding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recycler.setAdapter(new WelcomeAdapter());

        /*set itemDecorator*/
        binding.recycler.addItemDecoration(new CircleIndicator(this));

        /*define Helper*/
        helper = new PagerSnapHelper();
        helper.attachToRecyclerView(binding.recycler);

        /*set OnClickListener to btnStart, set KEY_SHOW_WELCOME flag to False */
        binding.btnStart.setOnClickListener(v -> {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putBoolean(KEY_SHOW_WELCOME, false)
                    .apply();
            /*start MainActivity*/
            startActivity(new Intent(this, MainActivity.class));
            /*needs to close WelcomeActivity here as we proceed with MainActivity further*/
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        /*onDestroy de-attach adapter and helper */
        helper.attachToRecyclerView(null);
        binding.recycler.setAdapter(null);

        super.onDestroy();
    }
}