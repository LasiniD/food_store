package com.example.foodstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodstore.R;
import com.example.foodstore.activities.NavCategoryActivity;
import com.example.foodstore.models.HomeCategory;
import com.example.foodstore.models.NavCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {

    Context context;
    List<NavCategoryModel> navCategoryModels;

    public NavCategoryAdapter(Context context, List<NavCategoryModel> navCategoryModels) {
        this.context = context;
        this.navCategoryModels = navCategoryModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cat_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(navCategoryModels.get(position).getImage_url()).into(holder.imageView);
        holder.name.setText(navCategoryModels.get(position).getName());
        holder.description.setText(navCategoryModels.get(position).getDescription());
        holder.discount.setText(navCategoryModels.get(position).getDiscount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NavCategoryActivity.class);
                intent.putExtra("type",navCategoryModels.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return navCategoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, description, discount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.cat_nav_name);
            description = itemView.findViewById(R.id.cat_nav_description);
            discount = itemView.findViewById(R.id.cat_nav_discount);
        }
    }
}
