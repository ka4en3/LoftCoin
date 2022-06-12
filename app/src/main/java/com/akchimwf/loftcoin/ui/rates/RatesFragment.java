package com.akchimwf.loftcoin.ui.rates;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.databinding.FragmentRatesBinding;
import com.akchimwf.loftcoin.util.formatter.PercentFormatter;
import com.akchimwf.loftcoin.util.formatter.PriceFormatter;

import java.util.List;

public class RatesFragment extends Fragment {
    /*FragmentRatesBinding class comes from 'viewBinding' at build.grade*/
    private FragmentRatesBinding binding;

    private RatesAdapter adapter;

    private RatesViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*when closing Fragment, it stays alive, only associated View is destroyed. So we keep adapter alive on the Fragment lifecycle.*/
        adapter = new RatesAdapter(new PriceFormatter(), new PercentFormatter());
        /*Creates ViewModelProvider. This will create ViewModels and retain them in a store of the given ViewModelStoreOwner.*/
        /*get -> Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or an activity), associated with this ViewModelProvider.*/
        viewModel = new ViewModelProvider(this).get(RatesViewModel.class);
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

        /*set options menu*/
        setHasOptionsMenu(true);

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

        binding.recycler.setHasFixedSize(true);

        /*Adds the given observer to the observers list within the lifespan of the given owner. */
        /*This is observer for LiveData. Every time LiveData.setValue or LiveData.postValue calling, onChanged will be called*/
        /*getViewLifecycleOwner() (NOT observeForever, or NOT "this"(wrong ViewLifecycleOwner here)) - because ViewModel can live longer than Fragment, so we have to monitor lifecycle of Fragment*/
        /*if not -> leak of binding=View could happen*/
        /*could be replaced with lambda*/
        viewModel.coins().observe(getViewLifecycleOwner(), new Observer<List<Coin>>() {
            @Override
            public void onChanged(List<Coin> coins) {
                /*ListAdapter method -> Submits a new list to be diffed, and displayed.*/
                adapter.submitList(coins);
            }
        });

        viewModel.isRefreshing().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean refreshing) {
                binding.refresher.setRefreshing(refreshing);
            }
        });
        /*could use also: */
        /*viewModel.isRefreshing().observe(getViewLifecycleOwner(), binding.refresher::setRefreshing);*/

        binding.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.rates, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Timber.d("%s", item);
        /*if clicked on currency in menu*/
        if (item.getItemId() == R.id.currency_dialog) {
            /*all navigation provided by Navigation controller -> we have to find it in current Fragment*/
            /*RatesFragment exists inside id/main_host fragment*/
            /*NavHostFragment provides an area within your layout for self-contained navigation to occur*/
            /*we sure that we can get Navigation controller from current Fragment*/
            NavHostFragment.findNavController(this)
                    /*RatesFragment don't know which Fragment will be shown on click -> no direct connections between fragments*/
                    /*can replace currency_dialog in Navigation graph without any problems with dependencies*/
                    .navigate(R.id.currency_dialog);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        /*when closing Fragment, it stays alive, only associated View is destroyed. So we keep adapter alive on the Fragment lifecycle.*/
        binding.recycler.swapAdapter(null, false); //else GC will not collect RV
        super.onDestroyView();
    }
}