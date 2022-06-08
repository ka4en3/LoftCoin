package com.akchimwf.loftcoin.ui.rates;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.databinding.LiRatesBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private final List<? extends Coin> coins;

    protected RatesAdapter(List<? extends Coin> coins) {
        /*Indicates whether each item in the data set can be represented with a unique identifier of type Long.*/
        setHasStableIds(true);  //this is optimization of RV behavior
        this.coins = coins;
    }

    /*as every coin has id in RV*/
    @Override
    public long getItemId(int position) {
        return coins.get(position).id();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LiRatesBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RatesAdapter.ViewHolder holder, int position) {
        holder.bind(coins.get(position));
    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final Context context = recyclerView.getContext();
        /*create inflater once adapter attached to RecycleView, not every time onCreateViewHolder*/
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /*store LiRatesBinding instance in ViewHolder*/
        private final LiRatesBinding binding;

        public ViewHolder(LiRatesBinding binding) {
            /*WalletsViewHolder needs View in constructor -> use getRoot(), not WelcomePageBinding itself*/
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Coin coin) {
            binding.symbol.setText(coin.symbol());
        }
    }
}
