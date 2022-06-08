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

public class RatesFragment extends Fragment implements RatesView {
    /*FragmentRatesBinding class comes from 'viewBinding' at build.grade*/
    private FragmentRatesBinding binding;

    /*when Fragment switches to another, associated View is destroyed. But not Fragment -> all fields(and presenter) stays alive */
    /*Presenter form MVP pattern*/
    private RatesPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RatesPresenter();
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
        /*hasFixedSize – true if adapter changes cannot affect the size of the RecyclerView.*/
        binding.recycler.setHasFixedSize(true); //TODO why?
        /*attach (RatesFragment)View to Presenter*/
        presenter.attach(this);  //TODO presenter.attach((RatesView) view); ?
    }

    @Override
    public void onDestroyView() {
        /*when Fragment switches to another, associated View is destroyed. But not Fragment -> all fields(and presenter) stays alive */
        /*detach (RatesFragment)View from Presenter*/
        presenter.detach(this);
        super.onDestroyView();
    }

    @Override
    public void showCoins(@NonNull List<? extends Coin> coins) {
        binding.recycler.setAdapter(new RatesAdapter(coins));
    }

    @Override
    public void showError(@NonNull String error) {

    }
}