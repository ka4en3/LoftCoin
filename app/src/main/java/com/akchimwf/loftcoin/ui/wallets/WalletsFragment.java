package com.akchimwf.loftcoin.ui.wallets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.databinding.FragmentWalletsBinding;

public class WalletsFragment extends Fragment {
    /*PagerSnapHelper helps to show RecycleView as ViewPager, we don't use ViewPager here*/
    private PagerSnapHelper walletsSnapHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*create View from current fragment layout*/
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*FragmentWalletsBinding class comes from 'viewBinding' at build.grade*/
        /*don't inflate here, as we already inflated View in onCreateView. just bind View to FragmentWalletsBinding*/
        final FragmentWalletsBinding binding = FragmentWalletsBinding.bind(view);

        walletsSnapHelper = new PagerSnapHelper();
        walletsSnapHelper.attachToRecyclerView(binding.recycler);

        /*Container for a dynamically typed data value. Primarily used with android.content.res.Resources for holding resource values.*/
        final TypedValue outValue = new TypedValue();
        /*Retrieve the value of an attribute in the Theme. walletCardWidth -> outValue. resolveRefs means Style can be referenced to another Style*/
        view.getContext().getTheme().resolveAttribute(R.attr.walletCardWidth, outValue, true);
        /*A structure describing general information about a display, such as its size, density, and font scaling.*/
        final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        /*calculating padding. (Screen width - walletCard width)/2*/
        final int padding = (int) (displayMetrics.widthPixels - outValue.getDimension(displayMetrics)) / 2;
        /*set paddings to RecycleView*/
        binding.recycler.setPadding(padding, 0, padding, 0);
        /*By default, children are clipped to the padding of their parent RecyclerView. This clipping behavior is only enabled if padding is non-zero.*/
        binding.recycler.setClipToPadding(false);

        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.recycler.setAdapter(new WalletsAdapter());
        binding.recycler.addOnScrollListener(new CarouselScroller());

        binding.recycler.setVisibility(View.VISIBLE);
        binding.walletCard.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        walletsSnapHelper.attachToRecyclerView(null);
        super.onDestroyView();
    }

    private static class CarouselScroller extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            //           super.onScrolled(recyclerView, dx, dy);
            /*find the center of the screen, instead of using DisplayMetrics*/
            final int centerX = (recyclerView.getLeft() + recyclerView.getRight()) / 2;
            /*iteration on Childs of current recycleView. Childs - only visible views (not all in the adapter)*/
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                final View child = recyclerView.getChildAt(i);

                /*find the center of the Child*/
                final int childCenterX = (child.getLeft() + child.getRight()) / 2;

                /*normalized by centerX absolute offset of the Child X position*/
                //final int childOffset = Math.abs(centerX - childCenterX) / centerX;   //doesn't work properly
                /*normalized by walletCard width absolute offset of the Child X position*/
                final float childOffset = (float) Math.abs(centerX - childCenterX) / (child.getWidth());

                /*Returns the value of the first argument raised to the power of the second argument...*/
                /*read more in function description*/
                //final float factor = (float) Math.pow(2, -1f / childOffset);   //doesn't work properly
                //final float factor = (float) Math.pow(0.85, childOffset);      //doesn't work properly too

                /*if childOffset <= 50% of the walletCard width -> set scale=1, else set scale = 0.9*/
                final float factor = ((childOffset >= 0) && (childOffset <= 0.5f)) ? 1 : 0.9f;

                /*scaling with the factor*/
                child.setScaleX(factor);
                child.setScaleY(factor);
            }
        }

    }
}