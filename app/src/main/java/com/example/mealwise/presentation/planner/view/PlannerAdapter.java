package com.example.mealwise.presentation.planner.view;

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

import java.util.ArrayList;
import java.util.List;

public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {

    private List<Meal> meals = new ArrayList<>();
    private OnPlanClickListener listener;

    public interface OnPlanClickListener {
        void onRemoveClick(Meal meal);
        void onMealClick(Meal meal);
    }

    public PlannerAdapter(OnPlanClickListener listener) {
        this.listener = listener;
    }

    public void setList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_planner_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);

        holder.tvName.setText(meal.getName());
        holder.tvArea.setText(meal.getCategory() + " | " + meal.getArea());
        ImageLoader.loadImage(holder.itemView.getContext(), meal.getThumbUrl(), holder.imgMeal);

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
        holder.itemView.setOnClickListener(v -> listener.onMealClick(meal));
    }

    @Override
    public int getItemCount() { return meals.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMeal, btnRemove;
        TextView tvName, tvArea, tvIng1, tvIng2;
        View cardIng1, cardIng2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMeal = itemView.findViewById(R.id.ivPlanMealImg);
            tvName = itemView.findViewById(R.id.tvPlanMealName);
            tvArea = itemView.findViewById(R.id.tvPlanMealArea);
            btnRemove = itemView.findViewById(R.id.btnRemovePlan);
            cardIng1 = itemView.findViewById(R.id.cardPlanIng1);
            tvIng1 = itemView.findViewById(R.id.tvPlanIng1);
            cardIng2 = itemView.findViewById(R.id.cardPlanIng2);
            tvIng2 = itemView.findViewById(R.id.tvPlanIng2);
        }
    }
}