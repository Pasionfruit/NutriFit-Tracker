package com.zybooks.nutrifittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.zybooks.nutrifittracker.model.Meal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {
    private List<Meal> meals;

    public MealAdapter(List<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        Meal currentMeal = meals.get(position);
        holder.bind(currentMeal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    public Meal getMeal(int position) {
        return meals.get(position);
    }

    public void deleteItem(int position) {
        meals.remove(position);
        notifyItemRemoved(position);
    }

    static class MealViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView typeTextView;
        private TextView caloriesTextView;
        private TextView carbsTextView;
        private TextView fatsTextView;
        private TextView proteinTextView;

        public MealViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_meal_name);
            typeTextView = itemView.findViewById(R.id.text_meal_type);
            caloriesTextView = itemView.findViewById(R.id.text_meal_calories);
            carbsTextView = itemView.findViewById(R.id.text_meal_carbs);
            fatsTextView = itemView.findViewById(R.id.text_meal_fats);
            proteinTextView = itemView.findViewById(R.id.text_meal_protein);
        }

        public void bind(Meal meal) {
            nameTextView.setText(meal.getName());
            typeTextView.setText(meal.getType());
            caloriesTextView.setText("Calories: " + meal.getCalories());
            carbsTextView.setText("Carbs: " + meal.getCarbs() + "g");
            fatsTextView.setText("Fats: " + meal.getFats() + "g");
            proteinTextView.setText("Protein: " + meal.getProtein() + "g");
        }
    }
}
