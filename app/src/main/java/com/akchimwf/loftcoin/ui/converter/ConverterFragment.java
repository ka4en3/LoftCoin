package com.akchimwf.loftcoin.ui.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.akchimwf.loftcoin.BaseComponent;
import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.databinding.FragmentConverterBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ConverterFragment extends Fragment {
    /*FragmentConverterBinding class comes from 'viewBinding' at build.grade*/
    private FragmentConverterBinding binding;

    private final ConverterComponent component;

    private ConverterViewModel viewModel;

    @Inject
    public ConverterFragment(BaseComponent baseComponent) {
        /*get the ConverterComponent in ConverterFragment, setting BaseComponent as dependency*/
        /*DaggerRatesComponent.Builder autogenerated*/
        component = DaggerConverterComponent.builder()
                .baseComponent(baseComponent)
                .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Creates ViewModelProvider. This will create ViewModels and retain them in a store of the given ViewModelStoreOwner.*/
        /*get -> Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or an activity), associated with this ViewModelProvider.*/
        viewModel = new ViewModelProvider(this, component.viewModelFactory()).get(ConverterViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*create View from current fragment layout*/
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}