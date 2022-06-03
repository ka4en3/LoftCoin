package com.akchimwf.loftcoin.ui.welcome;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.databinding.WelcomePageBinding;

public class WelcomeAdapter extends RecyclerView.Adapter<WelcomeAdapter.ViewHolder> {

    /*store resources for WelcomeActivities screens in int[]*/
    private static final int[] IMAGES = {
            R.drawable.welcome_page_1,
            R.drawable.welcome_page_2,
            R.drawable.welcome_page_3
    };

    private static final int[] TITLES = {
            R.string.welcome_page_1_title,
            R.string.welcome_page_2_title,
            R.string.welcome_page_3_title
    };

    private static final int[] SUBTITLES = {
            R.string.welcome_page_1_subtitle,
            R.string.welcome_page_2_subtitle,
            R.string.welcome_page_3_subtitle
    };

    private LayoutInflater inflater;

    @NonNull
    @Override
    public WelcomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*WelcomePageBinding class comes from 'viewBinding' at build.grade*/
        return new ViewHolder(WelcomePageBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WelcomeAdapter.ViewHolder holder, int position) {
        /*binding resources to current screen of WelcomeAdapter*/
        holder.binding.image.setImageResource(IMAGES[position]);
        holder.binding.subtitle.setText(TITLES[position]);
        holder.binding.subtitle.setText(SUBTITLES[position]);
    }

    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        /*create inflater once adapter attached to RecycleView, not every time onCreateViewHolder*/
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /*store WelcomePageBinding instance in ViewHolder*/
        final WelcomePageBinding binding;

        public ViewHolder(@NonNull WelcomePageBinding binding) {
            /*ViewHolder needs View in constructor -> use getRoot(), not WelcomePageBinding itself*/
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
