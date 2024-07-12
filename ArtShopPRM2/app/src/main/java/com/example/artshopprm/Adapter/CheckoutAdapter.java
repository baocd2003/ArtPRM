package com.example.artshopprm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.R;
import com.example.artshopprm.Service.ChangeNumberItemsListener;
import com.example.artshopprm.Service.ManagementCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.viewholder> {
    ArrayList<Art> list;
    ChangeNumberItemsListener changeNumberItemsListener;
    public CheckoutAdapter(ArrayList<Art> list, Context context) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item_checkout, parent, false);
        return new CheckoutAdapter.viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.title.setText(list.get(position).getArtName());
        holder.feeEachItem.setText("VND " + list.get(position).getPrice());
        holder.totalEachItem.setText("VND " + list.get(position).getNumberInCart() * list.get(position).getPrice());
        holder.quantityTxt.setText("Quantity: " + list.get(position).getNumberInCart());
        Picasso.get().load(list.get(position).getImageUrl()).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, quantityTxt;
        ImageView pic;
        TextView totalEachItem;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
        }
    }
}
