package com.zybooks.nutrifittracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MacrosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macros);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Macros");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_macros);  // Mark the Macros item as selected
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
