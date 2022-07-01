package com.akchimwf.loftcoin1.ui.currency;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akchimwf.loftcoin1.BaseComponent;
import com.akchimwf.loftcoin1.R;
import com.akchimwf.loftcoin1.data.Currency;
import com.akchimwf.loftcoin1.databinding.DialogCurrencyBinding;
import com.akchimwf.loftcoin1.widget.OnItemClick;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import javax.inject.Inject;

/*Default way of creating dialogs*/
/*Dialog is a kind of Fragment*/
public class CurrencyDialog extends AppCompatDialogFragment {

    DialogCurrencyBinding binding;

    private CurrencyAdapter adapter;

    private final CurrencyComponent component;

    private CurrencyViewModel viewModel;

    private OnItemClick onItemClick;

    @Inject
    public CurrencyDialog(BaseComponent baseComponent) {
        /*get the CurrencyComponent in CurrencyDialog, setting BaseComponent as dependency*/
        /*DaggerCurrencyComponent.Builder autogenerated*/
        component = DaggerCurrencyComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //this have to be in a first line
        adapter = new CurrencyAdapter();

        /*Creates ViewModelProvider. This will create ViewModels and retain them in a store of the given ViewModelStoreOwner.*/
        /*get -> Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or an activity), associated with this ViewModelProvider.*/
        viewModel = new ViewModelProvider(this, component.viewModelFactory()).get(CurrencyViewModel.class);
    }

    /*overriding this method means onCreateView and onViewCreated of Fragment will not calling, as Dialog has other logics*/
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        /*requireActivity() - Return the FragmentActivity this fragment is currently associated with.*/
        binding = DialogCurrencyBinding.inflate(requireActivity().getLayoutInflater());

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycler.setAdapter(adapter);

        /*Adds the given observer to the observers list within the lifespan of the given owner.
        The events are dispatched on the main thread. If LiveData already has com.akchimwf.loftcoin1.data set, it will be delivered to the observer.*/
        viewModel.availableCurrencies().observe(this, new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> list) {
                adapter.submitList(list);
            }
        });
        /*currencyRepo.availableCurrencies().observe(this, adapter::submitList);*/

        /*set onClickListener*/
        onItemClick = new OnItemClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecyclerView.ViewHolder containingViewHolder = binding.recycler.findContainingViewHolder(v);
                if (containingViewHolder != null) {
                    /*update chosen in dialog currency*/
                    viewModel.updateCurrency(adapter.getItem(containingViewHolder.getAdapterPosition())); //Returns the Adapter position of the item represented by this ViewHolder.
                }
                /*close the dialog - standard way*/
                dismissAllowingStateLoss();
            }
        });
        binding.recycler.addOnItemTouchListener(onItemClick);

        return new MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.choose_currency)
                .setView(binding.getRoot())
                .create();
    }

/*    @Deprecated
    @Override
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
        binding.recycler.removeOnItemTouchListener(onItemClick);
        super.onDestroy();
    }
}
