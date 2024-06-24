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

import java.util.List;
public class PopularArtAdapter extends RecyclerView.Adapter<PopularArtAdapter.ArtViewHolder> {

    private Context context;
    private List<Art> artList;

    public PopularArtAdapter(Context context, List<Art> artList) {
        this.context = context;
        this.artList = artList;
    }

    @NonNull
    @Override
    public ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_art_list, parent, false);
        return new ArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtViewHolder holder, int position) {
        Art art = artList.get(position);
        holder.titleTxt.setText(art.getArtName());
        holder.descriptionTxt.setText(art.getDescription());
        holder.priceTxt.setText("$" + art.getPrice());

        // Assuming you have a rating field in your Art class
        // holder.rateTxt.setText(String.valueOf(art.getRating()));
        holder.rateTxt.setText("5"); // Placeholder for rating

        // Loading image using Picasso or any other image loading library
        Picasso.get().load(art.getImageUrl()).into(holder.imgArt); // Assuming you have an image URL in your Art class
        holder.imgArt.setOnClickListener(v -> {
            Intent intent=new Intent(context, DetailActivity.class);
            intent.putExtra("object",artList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    public static class ArtViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArt, imageView10;
        TextView titleTxt, descriptionTxt, priceTxt, rateTxt;

        public ArtViewHolder(@NonNull View itemView) {
            super(itemView);
            imgArt = itemView.findViewById(R.id.imgArt);
            imageView10 = itemView.findViewById(R.id.imageView10);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            rateTxt = itemView.findViewById(R.id.rateTxt);
        }
    }
}
