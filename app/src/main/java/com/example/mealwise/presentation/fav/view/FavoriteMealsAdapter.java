package com.example.mealwise.presentation.fav.view;

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
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMealsAdapter extends RecyclerView.Adapter<FavoriteMealsAdapter.ViewHolder> {

    private List<Meal> meals = new ArrayList<>();
    private Context context;
    private OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onRemoveClick(Meal meal);
        void onItemClick(Meal meal);
    }

    public FavoriteMealsAdapter(Context context, OnFavoriteClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);

        holder.tvName.setText(meal.getName());
        holder.tvArea.setText(meal.getArea() + " | " + meal.getCategory());
        ImageLoader.loadImage(context, meal.getThumbUrl(), holder.imgMeal);


        if (meal.getIngredient1() != null && !meal.getIngredient1().isEmpty()) {
            holder.tvIng1.setText(meal.getIngredient1());
            holder.cardIng1.setVisibility(View.VISIBLE);
        } else {
            holder.cardIng1.setVisibility(View.GONE);
        }

        if (meal.getIngredient2() != null && !meal.getIngredient2().isEmpty()) {
            holder.tvIng2.setText(meal.getIngredient2());
            holder.cardIng2.setVisibility(View.VISIBLE);
        } else {
            holder.cardIng2.setVisibility(View.GONE);
        }

        holder.btnRemove.setOnClickListener(v -> listener.onRemoveClick(meal));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(meal));
    }

    @Override
    public int getItemCount() { return meals.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMeal, btnRemove;
        TextView tvName, tvArea, tvIng1, tvIng2;
        View cardIng1, cardIng2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMeal = itemView.findViewById(R.id.ivFavMealImg);
            tvName = itemView.findViewById(R.id.tvFavMealName);
            tvArea = itemView.findViewById(R.id.tvFavMealArea);
            btnRemove = itemView.findViewById(R.id.btnRemoveFav);
            cardIng1 = itemView.findViewById(R.id.cardIng1);
            tvIng1 = itemView.findViewById(R.id.tvIng1);
            cardIng2 = itemView.findViewById(R.id.cardIng2);
            tvIng2 = itemView.findViewById(R.id.tvIng2);
        }
    }
}
