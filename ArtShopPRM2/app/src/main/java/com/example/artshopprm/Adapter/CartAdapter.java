package com.example.artshopprm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.CartActivity;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.MainActivity;
import com.example.artshopprm.R;
import com.example.artshopprm.Service.ChangeNumberItemsListener;
import com.example.artshopprm.Service.ManagementCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    ArrayList<Art> list;
    private ManagementCart managementCart;
    ChangeNumberItemsListener changeNumberItemsListener;
    Context context;

    public CartAdapter(ArrayList<Art> list, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.list = list;
        managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
        context = context;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.title.setText(list.get(position).getArtName());
        holder.feeEachItem.setText("$"+(list.get(position).getNumberInCart()*list.get(position).getPrice()));
        holder.totalEachItem.setText(list.get(position).getNumberInCart()+" * $"+(
                list.get(position).getPrice()));
        holder.num.setText(list.get(position).getNumberInCart()+"");
        Picasso.get().load(list.get(position).getImageUrl()).into(holder.pic);

        holder.plusItem.setOnClickListener(v -> {
            int num = list.get(position).getNumberInCart();
            if (num < list.get(position).getStockQuantity()) {
                num++;
                list.get(position).setNumberInCart(num);
                holder.num.setText(String.valueOf(num));
                holder.feeEachItem.setText("$" + (num * list.get(position).getPrice()));
                holder.totalEachItem.setText(num + " * $" + list.get(position).getPrice());
                managementCart.plusNumberItem(list, position, () -> {
                    notifyDataSetChanged();
                    changeNumberItemsListener.change();
                });
            } else {
                Toast.makeText(holder.itemView.getContext(), "Out of stock", Toast.LENGTH_SHORT).show();
            }
        });

        holder.minusItem.setOnClickListener(v -> {
            int num = list.get(position).getNumberInCart();
            if (num >= 1) {
                num--;
                list.get(position).setNumberInCart(num);
                holder.num.setText(String.valueOf(num));
                holder.feeEachItem.setText("$" + (num * list.get(position).getPrice()));
                holder.totalEachItem.setText(num + " * $" + list.get(position).getPrice());
                managementCart.minusNumberItem(list, position, () -> {
                    notifyDataSetChanged();
                    changeNumberItemsListener.change();
                });

            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView totalEachItem, num;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }
}
