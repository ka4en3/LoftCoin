package com.akchimwf.loftcoin1.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akchimwf.loftcoin1.BaseComponent;
import com.akchimwf.loftcoin1.R;
import com.akchimwf.loftcoin1.data.Coin;
import com.akchimwf.loftcoin1.databinding.DialogCurrencyBinding;
import com.akchimwf.loftcoin1.widget.RxRecycleView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/*Fragment for choosing currency TO/FROM convert. Share the same ViewModel with ConverterFragment.*/
/*In this case choosing currency in one fragment replicates in another*/
/*Fragment behaviour, not Dialog behaviour.*/
public class CoinsSheet extends BottomSheetDialogFragment {

    static final int MODE_FROM = 1;
    static final int MODE_TO = 2;
    static final String KEY_MODE = "MODE";
    private int mode;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final ConverterComponent component;

    private DialogCurrencyBinding binding;

    private ConverterViewModel viewModel;

    private CoinsSheetAdapter adapter;

    @Inject
    public CoinsSheet(BaseComponent baseComponent) {
        component = DaggerConverterComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireParentFragment(), component.viewModelFactory())
                .get(ConverterViewModel.class);

        adapter = component.coinsSheetAdapter();

        /*get mode from ConverterFragment necessary*/
        mode = requireArguments().getInt(KEY_MODE, MODE_FROM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_currency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DialogCurrencyBinding.bind(view);

        binding.recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.recycler.setAdapter(adapter);
        //binding.recycler.setHasFixedSize(true);  //NO NEED as sizes should be calculated due to content

        /*subscribe on top 3 coins and push to RV*/
        disposable.add(viewModel.topCoins().subscribe(adapter::submitList));

        /*subscribe onClick*/
        disposable.add(RxRecycleView.onClick(binding.recycler)
                .map((position) -> adapter.getItem(position))
                .subscribe(coin -> {
                    if (mode == MODE_FROM) {
                        viewModel.fromCoin(coin);
                    } else {
                        viewModel.toCoin(coin);
                    }
                    dismissAllowingStateLoss();   //close dialog
                }));
    }

    @Override
    public void onDestroy() {
        binding.recycler.setAdapter(null);
        /*This is DialogFragment, means when destroyed View also destroyed the whole Fragment -> can call dispose(), not clear()*/
        disposable.dispose();
        super.onDestroy();
    }
}
