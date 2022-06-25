package com.akchimwf.loftcoin1.ui.wallets;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akchimwf.loftcoin1.R;
import com.akchimwf.loftcoin1.databinding.LiWalletBinding;

public class WalletsAdapter extends RecyclerView.Adapter<WalletsAdapter.WalletsViewHolder> {

    private LayoutInflater inflater;

    @NonNull
    @Override
    public WalletsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletsViewHolder(LiWalletBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletsViewHolder holder, int position) {
        holder.binding.logoCardText.setText("BTC");
        holder.binding.balance1Text.setText("2.77744" + " BTC");
        holder.binding.balance2Text.setText(String.valueOf(2.77744 * 31515) + " $");
        holder.binding.logoCard.setImageResource(R.drawable.ic_logo_card);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        /*create inflater once adapter attached to RecycleView, not every time onCreateViewHolder*/
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class WalletsViewHolder extends RecyclerView.ViewHolder {
        /*store LiWalletBinding instance in WalletsViewHolder*/
        private final LiWalletBinding binding;

        public WalletsViewHolder(@NonNull LiWalletBinding binding) {
            /*WalletsViewHolder needs View in constructor -> use getRoot(), not WelcomePageBinding itself*/
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
