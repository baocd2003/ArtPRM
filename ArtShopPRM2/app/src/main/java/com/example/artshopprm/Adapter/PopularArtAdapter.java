package com.example.artshopprm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artshopprm.DetailActivity;
import com.example.artshopprm.Entity.Art;
import com.example.artshopprm.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PopularArtAdapter extends RecyclerView.Adapter<PopularArtAdapter.ArtViewHolder> {

    private Context context;
    private List<Art> artList;
    private List<Art> filteredArtList; // List to hold filtered arts

    public PopularArtAdapter(Context context, List<Art> artList) {
        this.context = context;
        this.artList = artList;
        this.filteredArtList = new ArrayList<>(artList); // Initialize with full list
    }

    // Method to filter the list based on search query
    public void filter(String query) {
        filteredArtList.clear();
        if (query.isEmpty()) {
            filteredArtList.addAll(artList); // If query is empty, show all items
        } else {
            query = query.toLowerCase().trim();
            for (Art art : artList) {
                if (art.getArtName().toLowerCase().contains(query)) {
                    filteredArtList.add(art); // Add item if its name contains the query
                }
            }
        }
        notifyDataSetChanged(); // Notify adapter of data change
    }

    @NonNull
    @Override
    public ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_art_list, parent, false);
        return new ArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtViewHolder holder, int position) {
        Art art = filteredArtList.get(position); // Use filtered list for binding
        holder.titleTxt.setText(art.getArtName());
        holder.descriptionTxt.setText(art.getDescription());
        // Format the price
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        holder.priceTxt.setText("VND " + numberFormat.format(art.getPrice()));

        holder.rateTxt.setText(String.valueOf(art.getRate()));// Placeholder for rating

        // Load image using Picasso or any other image loading library
        Picasso.get().load(art.getImageUrl()).into(holder.imgArt);

        // Handle item click to open detail activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("art", art);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredArtList.size(); // Return size of filtered list
    }

    public static class ArtViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArt;
        TextView titleTxt, descriptionTxt, priceTxt, rateTxt;

        public ArtViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArt = itemView.findViewById(R.id.imgArt);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            rateTxt = itemView.findViewById(R.id.rateTxt);
        }
    }
}
