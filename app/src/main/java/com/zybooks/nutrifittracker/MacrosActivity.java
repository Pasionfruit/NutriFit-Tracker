package com.zybooks.nutrifittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zybooks.nutrifittracker.model.Meal;
import com.zybooks.nutrifittracker.viewmodel.MealViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MacrosActivity extends AppCompatActivity {
    private static final String[] MEAL_TYPES = {"Breakfast", "Lunch", "Snack", "Dinner"};

    private RecyclerView recyclerView;
    private MealAdapter mealAdapter;
    private MealViewModel mealViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Macros");
        }

        Spinner mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MEAL_TYPES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_macros);

        Button addButton = findViewById(R.id.add_button);

        recyclerView = findViewById(R.id.recycler_view_meals);
        mealAdapter = new MealAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mealAdapter);
        registerForContextMenu(recyclerView);

        // Initialize ViewModel
        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);

        // Observe changes in the list of meals
        mealViewModel.getAllMeals().observe(this, meals -> mealAdapter.setMeals(meals));

        addButton.setOnClickListener(v -> addMealToList()); // Lambda expression for click listener
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.macros_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            // Get the position of the selected item
            RecyclerView.ViewHolder viewHolder = recyclerView.findContainingViewHolder(((View) Objects.requireNonNull(item.getActionView())));
            assert viewHolder != null;
            int position = viewHolder.getAdapterPosition();

            // Get the meal at the selected position
            Meal mealToDelete = mealAdapter.meals.get(position);

            // Delete the meal from the database via the ViewModel
            mealViewModel.delete(mealToDelete);

            Toast.makeText(this, "Meal deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void addMealToList() {
        EditText mealNameEditText = findViewById(R.id.meal_name);
        Spinner mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        EditText caloriesEditText = findViewById(R.id.calories);
        EditText carbsEditText = findViewById(R.id.carbs);
        EditText fatsEditText = findViewById(R.id.fats);
        EditText proteinEditText = findViewById(R.id.protein);

        String mealName = mealNameEditText.getText().toString();
        String mealType = mealTypeSpinner.getSelectedItem().toString();
        String caloriesStr = caloriesEditText.getText().toString();
        String carbsStr = carbsEditText.getText().toString();
        String fatsStr = fatsEditText.getText().toString();
        String proteinStr = proteinEditText.getText().toString();

        // Check if any of the input fields are empty
        if (mealName.isEmpty() || caloriesStr.isEmpty() || carbsStr.isEmpty() || fatsStr.isEmpty() || proteinStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse numerical inputs
        int calories = Integer.parseInt(caloriesStr);
        int carbs = Integer.parseInt(carbsStr);
        int fats = Integer.parseInt(fatsStr);
        int protein = Integer.parseInt(proteinStr);

        // Create a new instance of the Meal class with the retrieved values
        Meal meal = new Meal(mealName, mealType, calories, carbs, fats, protein);

        // Insert the meal into the database via the ViewModel
        mealViewModel.insert(meal);

        // Clear the input fields after adding the meal
        mealNameEditText.getText().clear();
        caloriesEditText.getText().clear();
        carbsEditText.getText().clear();
        fatsEditText.getText().clear();
        proteinEditText.getText().clear();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId(); // Get the ID of the selected item

                    if (itemId == R.id.navigation_macros) {
                        // Already in MacrosActivity, no action needed
                        return true;
                    } else if (itemId == R.id.navigation_workout) {
                        // Intent to switch to WorkoutActivity
                        Intent intent = new Intent(MacrosActivity.this, WorkoutActivity.class);
                        startActivity(intent);
                        finish();  // Close the current activity
                        return true;
                    }
                    return false;
                }
            };
}
