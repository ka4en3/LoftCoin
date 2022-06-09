package com.akchimwf.loftcoin.ui.rates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.databinding.FragmentRatesBinding;

import java.util.List;

public class RatesFragment extends Fragment {
    /*FragmentRatesBinding class comes from 'viewBinding' at build.grade*/
    private FragmentRatesBinding binding;

    private RatesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*when closing Fragment, it stays alive, only associated View is destroyed. So we keep adapter alive on the Fragment lifecycle.*/
        adapter = new RatesAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*create View from current fragment layout*/
        return inflater.inflate(R.layout.fragment_rates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*FragmentRatesBinding class comes from 'viewBinding' at build.grade*/
        /*don't inflate here, as we already inflated View in onCreateView. just bind View to FragmentRatesBinding*/
        binding = FragmentRatesBinding.bind(view);
        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        /*Swaps the current adapter with the provided one.
        It is similar to setAdapter(RecyclerView.Adapter) but assumes existing adapter and the new adapter uses
        the same RecyclerView.ViewHolder and does not clear the RecycledViewPool.*/
        /*Params:
        adapter – The new adapter to set, or null to set no adapter.
        removeAndRecycleExistingViews – If set to true, RecyclerView will recycle all existing Views.
        If adapters have stable ids and/or you want to animate the disappearing views, you may prefer to set this to false.*/
        binding.recycler.swapAdapter(adapter, false);
    }

    @Override
    public void onDestroyView() {
        /*when closing Fragment, it stays alive, only associated View is destroyed. So we keep adapter alive on the Fragment lifecycle.*/
        binding.recycler.swapAdapter(null, false);
        super.onDestroyView();
    }
}