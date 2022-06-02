package com.akchimwf.loftcoin.ui.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import android.content.Intent;
import android.os.Bundle;

import com.akchimwf.loftcoin.databinding.ActivityWelcomeBinding;
import com.akchimwf.loftcoin.ui.main.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    public static final String KEY_SHOW_WELCOME = "show_welcome";

    private ActivityWelcomeBinding binding;
    private PagerSnapHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recycler.setAdapter(new WelcomeAdapter());
        helper = new PagerSnapHelper();
        helper.attachToRecyclerView(binding.recycler);
        binding.btnStart.setOnClickListener(v -> {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putBoolean(KEY_SHOW_WELCOME, false)
                    .apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        helper.attachToRecyclerView(null);
        binding.recycler.setAdapter(null);
        super.onDestroy();
    }
}