package com.example.mealwise.presentation.search.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealwise.R;
import com.example.mealwise.data.meals.models.Area;
import com.example.mealwise.data.meals.models.Category;
import com.example.mealwise.data.meals.models.Ingredient;
import com.example.mealwise.utils.FlagUtils;
import com.example.mealwise.utils.ImageLoader;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterAdapter extends RecyclerView.Adapter<SearchFilterAdapter.ViewHolder> {

    private List<Object> list = new ArrayList<>();
    private String currentType = "";
    private OnFilterClickListener listener;

    public interface OnFilterClickListener {
        void onFilterClick(String type, String value);
    }

    public SearchFilterAdapter(OnFilterClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<?> newList, String type) {
        this.list.clear();
        this.list.addAll((List<Object>) newList);
        this.currentType = type;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = list.get(position);
        String name = "";
        String imageUrl = "";

        if (item instanceof Category) {
            Category cat = (Category) item;
            name = cat.getName();
            imageUrl = cat.getThumbUrl();
        } else if (item instanceof Area) {
            Area area = (Area) item;
            name = area.getName();
            imageUrl = FlagUtils.getFlagUrl(name);
        } else if (item instanceof Ingredient) {
            Ingredient ing = (Ingredient) item;
            name = ing.getName();
            imageUrl = "https://www.themealdb.com/images/ingredients/" + name + ".png";
        }

        holder.tvName.setText(name);
        ImageLoader.loadImage(holder.itemView.getContext(), imageUrl, holder.ivImage);

        String finalName = name;
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilterClick(currentType, finalName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivFilterImage);
            tvName = itemView.findViewById(R.id.tvFilterName);
        }
    }
}