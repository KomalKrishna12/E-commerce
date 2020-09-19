package com.example.e_commerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce.Interface.ItemClickListener;
import com.example.e_commerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProuctname, txtProductdescription, txtProductprice;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.products_image);
        txtProuctname = itemView.findViewById(R.id.products_name);
        txtProductdescription = itemView.findViewById(R.id.products_description);
        txtProductprice = itemView.findViewById(R.id.products_price);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
