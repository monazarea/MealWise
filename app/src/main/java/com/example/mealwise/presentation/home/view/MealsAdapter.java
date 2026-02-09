package com.example.mealwise.presentation.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Meal;
import com.example.mealwise.utils.ImageLoader;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {

    private Context context;
    private List<Meal> meals;
    private OnMealClickListener listener;

    public interface OnMealClickListener {
        void onMealClick(Meal meal);
    }

    public MealsAdapter(Context context, List<Meal> meals, OnMealClickListener listener) {
        this.context = context;
        this.meals = meals;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.tvMealName.setText(meal.getName());
        ImageLoader.loadImage(context, meal.getThumbUrl(), holder.ivMealThumb);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener. onMealClick(meal);
        });
    }

    @Override
    public int getItemCount() { return meals != null ? meals.size() : 0; }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMealThumb;
        TextView tvMealName;
        ImageView ivFavorite;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMealThumb = itemView.findViewById(R.id.ivMealThumb);
            tvMealName = itemView.findViewById(R.id.tvMealName);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
        }
    }
}