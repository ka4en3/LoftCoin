package com.akchimwf.loftcoin.ui.rates;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.databinding.LiRatesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/*RecyclerView.Adapter base class for presenting List data in a RecyclerView,
including computing diffs between Lists on a background thread*/
/*This class is a convenience wrapper around AsyncListDiffer that implements Adapter common default behavior for item access and counting*/
/*Type parameters:
<T> – Type of the Lists this Adapter will receive.
<VH> – A class that extends ViewHolder that will be used by the adapter.*/
class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {

    private LayoutInflater inflater;

    /*DiffUtil is a utility class that calculates the difference between two lists and outputs
    a list of update operations that converts the first list into the second one.*/
    /*DiffUtil.Callback serves two roles - list indexing, and item diffing. ItemCallback handles just the second of these,
    which allows separation of code that indexes into an array or List from the presentation-layer and content specific diffing code.*/
    /*Type parameters:
    <T> – Type of items to compare.*/
    RatesAdapter() {
        super(new DiffUtil.ItemCallback<Coin>() {
            /*here is the functionality for RV to calculate the difference between two datasets*/
            @Override
            public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return oldItem.id() == newItem.id();   //this method compare ids
            }

            @Override
            public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return Objects.equals(oldItem, newItem);  //compare the content of objects(coins), like price and change24h
                //can use oldItem.equals(newItem) too
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItem(position).id();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LiRatesBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RatesAdapter.ViewHolder holder, int position) {
        /*getItem -> method from ListAdapter*/
        holder.binding.symbol.setText(getItem(position).symbol());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /*store LiRatesBinding instance in ViewHolder*/
        private final LiRatesBinding binding;

        public ViewHolder(LiRatesBinding binding) {
            /*WalletsViewHolder needs View in constructor -> use getRoot(), not WelcomePageBinding itself*/
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
