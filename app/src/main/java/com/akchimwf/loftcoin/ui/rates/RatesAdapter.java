package com.akchimwf.loftcoin.ui.rates;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.akchimwf.loftcoin.BuildConfig;
import com.akchimwf.loftcoin.R;
import com.akchimwf.loftcoin.data.CmcCoin;
import com.akchimwf.loftcoin.data.Coin;
import com.akchimwf.loftcoin.databinding.LiRatesBinding;
import com.akchimwf.loftcoin.util.ImageLoader;
import com.akchimwf.loftcoin.util.formatter.Formatter;
import com.akchimwf.loftcoin.util.OutlineCircle;
import com.akchimwf.loftcoin.util.formatter.PercentFormatter;
import com.akchimwf.loftcoin.util.formatter.PriceFormatter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

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
    private final PriceFormatter priceFormatter;
    private final PercentFormatter percentFormatter;
    private final ImageLoader imageLoader;

    /*no @Binds, no @Provides in any @Module.
    Place RatesAdapter class in Component as is (because not binding interface or abstract to some custom realization)*/
    /*@Inject is enough*/
    @Inject
    /*put priceFormatter and percentFormatter to input, can use any type of formatter this way*/
    /*priceFormatter and percentFormatter provided by Dagger*/
    /*D in SOLID was broken here, as RatesAdapter depends of concrete class PriceFormatter (not from interface), but it will be excess*/
    RatesAdapter(PriceFormatter priceFormatter, PercentFormatter percentFormatter, ImageLoader imageLoader) {
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

            /*When areItemsTheSame(T, T) returns true for two items and areContentsTheSame(T, T) returns false for them,
            this method is called to get a payload about the change*/
            @Override
            public Object getChangePayload(@NonNull Coin oldItem, @NonNull Coin newItem) {
                /*will not calculate difference here, just return a newItem*/
                return newItem;
            }
        });
        this.priceFormatter = priceFormatter;
        this.percentFormatter = percentFormatter;
        this.imageLoader = imageLoader;
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
        ViewHolder viewHolder = new ViewHolder(LiRatesBinding.inflate(inflater, parent, false));
        return viewHolder;
    }

    /*default onBindViewHolder*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RatesAdapter.ViewHolder holder, int position) {
        /*getItem -> method from ListAdapter*/
        final Coin coin = getItem(position);

        holder.binding.symbol.setText(coin.symbol());

        holder.binding.price.setText(priceFormatter.format(coin.currencyCode(), coin.price()));

        /*%.2f%%: .2 - 2 number after dot, %% - show percent symbol*/
        holder.binding.change.setText(percentFormatter.format(coin.change24h()));
        if (coin.change24h() > 0) {
            holder.binding.change.setTextColor(colorPositive);
        } else {
            holder.binding.change.setTextColor(colorNegative);
        }

        /*dependency only of abstraction, not the Picasso realization*/
        imageLoader
                .load(BuildConfig.IMG_ENDPOINT + coin.id() + ".png")
                .into(holder.binding.logo);
    }

    /*called when payloads present(means data(price, change24) from server changed),
    so number of times as how many coins were changed*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            /*sure that inside Payload is Coin -> (Coin)*/
            /*get(0) as payloads size always==1*/
            final Coin coin = (Coin) payloads.get(0);

            /*binding only changed fields: price and change24*/
            holder.binding.price.setText(priceFormatter.format(coin.currencyCode(), coin.price()));
            holder.binding.change.setText(percentFormatter.format(coin.change24h()));
            if (coin.change24h() > 0) {
                holder.binding.change.setTextColor(colorPositive);
            } else {
                holder.binding.change.setTextColor(colorNegative);
            }
        }
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
