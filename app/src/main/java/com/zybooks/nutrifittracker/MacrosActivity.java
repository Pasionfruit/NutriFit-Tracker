package com.zybooks.nutrifittracker;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zybooks.nutrifittracker.R;
import com.zybooks.nutrifittracker.WorkoutActivity;
import com.zybooks.nutrifittracker.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class MacrosActivity extends AppCompatActivity {
    private static final String[] MEAL_TYPES = {"Breakfast", "Lunch", "Snack", "Dinner"};

    private List<Meal> mealList = new ArrayList<>(); // List to store meals

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
        bottomNavigationView.setSelectedItemId(R.id.navigation_macros);  // Mark the Macros item as selected

        // Find the add button in your layout
        Button addButton = findViewById(R.id.add_button);

        // Set a click listener for the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMealToList(); // Call method to add meal to list
            }
        });
    }

    // Method to add a meal to the list
    // Method to add a meal to the list
    // Method to add a meal to the list
    private void addMealToList() {
        EditText mealNameEditText = findViewById(R.id.meal_name);
        Spinner mealTypeSpinner = findViewById(R.id.meal_type_spinner);
        EditText caloriesEditText = findViewById(R.id.calories);
        EditText carbsEditText = findViewById(R.id.carbs);
        EditText fatsEditText = findViewById(R.id.fats);
        EditText proteinEditText = findViewById(R.id.protein);

        // Retrieve values entered by the user
        String mealName = mealNameEditText.getText().toString();
        String mealType = mealTypeSpinner.getSelectedItem().toString();
        String caloriesStr = caloriesEditText.getText().toString();
        String carbsStr = carbsEditText.getText().toString();
        String fatsStr = fatsEditText.getText().toString();
        String proteinStr = proteinEditText.getText().toString();

        // Flag to indicate if there are any invalid inputs
        boolean hasInvalidInput = false;

        // Check if any of the input fields are empty
        if (mealName.isEmpty()) {
            mealNameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            hasInvalidInput = true;
        } else {
            mealNameEditText.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE)); // Revert to default color
        }

        if (caloriesStr.isEmpty()) {
            caloriesEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            hasInvalidInput = true;
        } else {
            caloriesEditText.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE)); // Revert to default color
        }

        if (carbsStr.isEmpty()) {
            carbsEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            hasInvalidInput = true;
        } else {
            carbsEditText.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE)); // Revert to default color
        }

        if (fatsStr.isEmpty()) {
            fatsEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            hasInvalidInput = true;
        } else {
            fatsEditText.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE)); // Revert to default color
        }

        if (proteinStr.isEmpty()) {
            proteinEditText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            hasInvalidInput = true;
        } else {
            proteinEditText.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE)); // Revert to default color
        }

        // If any input is invalid, display a toast message and return
        if (hasInvalidInput) {
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

        // Add the meal to the list
        mealList.add(meal);

        // Optionally, you can clear the input fields after adding the meal
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
