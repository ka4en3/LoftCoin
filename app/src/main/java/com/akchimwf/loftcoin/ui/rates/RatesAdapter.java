package com.akchimwf.loftcoin.ui.rates;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.akchimwf.loftcoin.BuildConfig;
import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.databinding.LiRatesBinding;
import com.akchimwf.loftcoin.util.formatter.Formatter;
import com.akchimwf.loftcoin.util.OutlineCircle;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/*NO ANY LOGICS(f.e. formatting values) IN ADAPTER! ONLY BINDING VIEWS*/

/*RecyclerView.Adapter base class for presenting List data in a RecyclerView,
including computing diffs between Lists on a background thread*/
/*This class is a convenience wrapper around AsyncListDiffer that implements Adapter common default behavior for item access and counting*/
/*Type parameters:
<T> – Type of the Lists this Adapter will receive.
<VH> – A class that extends ViewHolder that will be used by the adapter.*/
class RatesAdapter extends ListAdapter<Coin, RatesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private int colorNegative = Color.RED;
    private int colorPositive = Color.GREEN;
    private final Formatter<Double> priceFormatter;
    private final Formatter<Double> percentFormatter;

    /*put priceFormatter to input, can use any type of formatter this way*/
    RatesAdapter(Formatter<Double> priceFormatter, Formatter<Double> percentFormatter) {
        /*DiffUtil is a utility class that calculates the difference between two lists and outputs
        a list of update operations that converts the first list into the second one.*/
        /*DiffUtil.Callback serves two roles - list indexing, and item diffing. ItemCallback handles just the second of these,
        which allows separation of code that indexes into an array or List from the presentation-layer and content specific diffing code.*/
        /*Type parameters:
        <T> – Type of items to compare.*/
        super(new DiffUtil.ItemCallback<Coin>() {
            /*here is the functionality for RV to calculate the difference between two datasets*/
            @Override
            public boolean areItemsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return oldItem.id() == newItem.id();   //this method compare ids
            }

            @Override
            public boolean areContentsTheSame(@NonNull Coin oldItem, @NonNull Coin newItem) {
                return Objects.equals(oldItem, newItem);  //compare the content of objects(coins), like price and change24h
                /*can use oldItem.equals(newItem) too*/
            }
        });
        this.priceFormatter = priceFormatter;
        this.percentFormatter = percentFormatter;
        /*as all Coins has ids*/
        setHasStableIds(true);
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
    public void onBindViewHolder(@NonNull RatesAdapter.ViewHolder holder, int position) {
        /*getItem -> method from ListAdapter*/
        final Coin coin = getItem(position);

        holder.binding.symbol.setText(coin.symbol());

        holder.binding.price.setText(priceFormatter.format(coin.price()));

        /*%.2f%%: .2 - 2 number after dot, %% - show percent symbol*/
        holder.binding.change.setText(percentFormatter.format(coin.change24h()));
        if (coin.change24h() > 0) {
            holder.binding.change.setTextColor(colorPositive);
        } else {
            holder.binding.change.setTextColor(colorNegative);
        }

        /*get the global Picasso instance.
        This instance is automatically initialized with defaults that are suitable to most implementations.*/
        Picasso.get()
                .load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                .into(holder.binding.logo);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final Context context = recyclerView.getContext();
        inflater = LayoutInflater.from(context);

        /*Container for a dynamically typed data value. Primarily used with android.content.res.Resources for holding resource values.*/
        final TypedValue typedValue = new TypedValue();
        /*Retrieve the value of an attribute in the Theme. resolveRefs means Style can be referenced to another Style*/
        context.getTheme().resolveAttribute(R.attr.percentNegative, typedValue, true);
        colorNegative = typedValue.data;
        context.getTheme().resolveAttribute(R.attr.percentPositive, typedValue, true);
        colorPositive = typedValue.data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /*store LiRatesBinding instance in ViewHolder*/
        private final LiRatesBinding binding;

        public ViewHolder(LiRatesBinding binding) {
            /*WalletsViewHolder needs View in constructor -> use getRoot(), not WelcomePageBinding itself*/
            super(binding.getRoot());
            this.binding = binding;
            OutlineCircle.apply(binding.logo);
        }
    }
}
