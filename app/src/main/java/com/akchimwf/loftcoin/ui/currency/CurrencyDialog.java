package com.akchimwf.loftcoin.ui.currency;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.data.Currency;
import com.akchimwf.loftcoin.data.CurrencyRepo;
import com.akchimwf.loftcoin.data.CurrencyRepoImpl;
import com.akchimwf.loftcoin.databinding.DialogCurrencyBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/*Default way of creating dialogs*/
/*Dialog is a kind of Fragment*/
public class CurrencyDialog extends AppCompatDialogFragment {

    DialogCurrencyBinding binding;

    private CurrencyRepo currencyRepo;

    private CurrencyAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //this have to be in a first line
        currencyRepo = new CurrencyRepoImpl(requireContext());  //requireContext() check if Context != null
        adapter = new CurrencyAdapter();
    }

    /*overriding this method means onCreateView and onViewCreated of Fragment will not calling, as Dialog has other logics*/
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /*requireActivity() - Return the FragmentActivity this fragment is currently associated with.*/
        binding = DialogCurrencyBinding.inflate(requireActivity().getLayoutInflater());

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(adapter);
        currencyRepo.availableCurrencies().observe(this, new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> list) {
                adapter.submitList(list);
            }
        });
        /*currencyRepo.availableCurrencies().observe(this, adapter::submitList);*/

        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.choose_currency)
                .setView(binding.getRoot())
                .create();
    }

    @Deprecated
/*    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(adapter);
        currencyRepo.availableCurrencies().observe(this, new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> list) {
                adapter.submitList(list);
            }
        });
    }*/

    @Override
    public void onDestroy() {
        /*cleaning*/
        binding.recycler.setAdapter(null);
        super.onDestroy();
    }
}
