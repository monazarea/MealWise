package com.example.mealwise.presentation.details.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Ingredient;
import com.example.mealwise.utils.ImageLoader;

import java.util.Base64;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private Context context;
    private List<Ingredient> ingredients;
    String BASE_URL = "https://www.themealdb.com/images/ingredients/";

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        holder.tvName.setText(ingredient.getName());
        holder.tvMeasure.setText(ingredient.getMeasure());

        String imageUrl = BASE_URL + ingredient.getName() + ".png";
        ImageLoader.loadImage(context, imageUrl, holder.ivIngredient);


    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIngredient;
        TextView tvName, tvMeasure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIngredient = itemView.findViewById(R.id.ivIngredient);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvMeasure = itemView.findViewById(R.id.tvIngredientMeasure);
        }
    }
}