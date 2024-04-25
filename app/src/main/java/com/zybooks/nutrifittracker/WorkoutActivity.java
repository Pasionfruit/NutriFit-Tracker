package com.zybooks.nutrifittracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zybooks.nutrifittracker.model.Workout;
import com.zybooks.nutrifittracker.viewmodel.WorkoutListViewModel;

import java.util.Comparator;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity
        implements WorkoutDialogFragment.OnSubjectEnteredListener {

    public enum SubjectSortOrder {
        ALPHABETIC, NEW_FIRST, OLD_FIRST
    }

    private Boolean mLoadSubjectList = true;
    private SubjectAdapter mSubjectAdapter;
    private RecyclerView mRecyclerView;
    private int[] mSubjectColors;
    private WorkoutListViewModel mWorkoutListViewModel;
    private Workout mSelectedWorkout;
    private int mSelectedSubjectPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mWorkoutListViewModel = new ViewModelProvider(this).get(WorkoutListViewModel.class);

        mSubjectColors = getResources().getIntArray(R.array.subjectColors);

        // Create 2 grid layout columns
        mRecyclerView = findViewById(R.id.subject_recycler_view);
        RecyclerView.LayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Call updateUI() when the subject list changes
        mWorkoutListViewModel.getSubjects().observe(this, subjects -> {
            if (mLoadSubjectList) {
                updateUI(subjects);
            }
        });

        // Initialize the BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_workout);

        // Set up the navigation item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId(); // Get the ID of the selected item

                    if (itemId == R.id.navigation_macros) {
                        Intent intent = new Intent(WorkoutActivity.this, MacrosActivity.class);
                        startActivity(intent);
                    } else if (itemId == R.id.navigation_workout) {

                        return true;
                    }

                    return true;
                }
            };


    private SubjectSortOrder getSettingsSortOrder() {
        // Set sort order from settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrderPref = sharedPrefs.getString("subject_order", "alpha");
        switch (sortOrderPref) {
            case "alpha":
                return SubjectSortOrder.ALPHABETIC;
            case "new_first":
                return SubjectSortOrder.NEW_FIRST;
            default:
                return SubjectSortOrder.OLD_FIRST;
        }
    }

    private void updateUI(List<Workout> workoutList) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        boolean isWorkoutSelected = selectedItemId == R.id.navigation_workout;

        mSubjectAdapter = new SubjectAdapter(workoutList, isWorkoutSelected);
        mSubjectAdapter.setSortOrder(getSettingsSortOrder());
        mRecyclerView.setAdapter(mSubjectAdapter);
    }

    @Override
    public void onSubjectEntered(String subjectText) {
        if (subjectText.length() > 0) {
            Workout workout = new Workout(subjectText);
            mLoadSubjectList = false;
            mWorkoutListViewModel.addSubject(workout);
            mSubjectAdapter.addSubject(workout);
            Toast.makeText(this, "Added " + subjectText, Toast.LENGTH_SHORT).show();
        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private Workout mWorkout;
        private final TextView mSubjectTextView;

        public SubjectHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            mSubjectTextView = itemView.findViewById(R.id.subject_text_view);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Workout workout, int position, boolean isWorkoutSelected) {
            mWorkout = workout;
            mSubjectTextView.setText(workout.getText());

            if (isWorkoutSelected) {
                if (mSelectedSubjectPosition == position) {
                    // Make selected subject stand out
                    mSubjectTextView.setBackgroundColor(Color.RED);
                } else {
                    // Make the background color dependent on the length of the subject string
                    int colorIndex = workout.getText().length() % mSubjectColors.length;
                    mSubjectTextView.setBackgroundColor(mSubjectColors[colorIndex]);
                }
            } else {
                // If navigation_workout is not selected, make the background empty
                mSubjectTextView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public void onClick(View view) {
            // Start ExerciseActivity with the selected subject
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class);
            intent.putExtra(ExerciseActivity.EXTRA_SUBJECT_ID, mWorkout.getId());
            intent.putExtra(ExerciseActivity.EXTRA_SUBJECT_TEXT, mWorkout.getText());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            }
            mSelectedWorkout = mWorkout;
            mSubjectAdapter.notifyItemChanged(mSelectedSubjectPosition);
            mActionMode = WorkoutActivity.this.startActionMode(mActionModeCallback);
            return true;
        }
    }

    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.delete) {
                mLoadSubjectList = false;
                mWorkoutListViewModel.deleteSubject(mSelectedWorkout);
                mSubjectAdapter.removeSubject(mSelectedWorkout);
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            mSubjectAdapter.notifyItemChanged(mSelectedSubjectPosition);
            mSelectedSubjectPosition = RecyclerView.NO_POSITION;
        }
    };

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {
        private final List<Workout> mWorkoutList;
        private final boolean mIsWorkoutSelected;

        public SubjectAdapter(List<Workout> workouts, boolean isWorkoutSelected) {
            mWorkoutList = workouts;
            mIsWorkoutSelected = isWorkoutSelected;
        }

        @NonNull
        @Override
        public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SubjectHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SubjectHolder holder, int position) {
            holder.bind(mWorkoutList.get(position), position, mIsWorkoutSelected);
        }

        @Override
        public int getItemCount() {
            return mWorkoutList.size();
        }

        public void addSubject(Workout workout) {
            mWorkoutList.add(0, workout);
            notifyItemInserted(0);
            mRecyclerView.scrollToPosition(0);
        }

        public void removeSubject(Workout workout) {
            int index = mWorkoutList.indexOf(workout);
            if (index >= 0) {
                mWorkoutList.remove(index);
                notifyItemRemoved(index);
            }
        }

        public void setSortOrder(SubjectSortOrder sortOrder) {
            switch (sortOrder) {
                case ALPHABETIC:
                    mWorkoutList.sort(Comparator.comparing(Workout::getText));
                    break;
                case NEW_FIRST:
                    mWorkoutList.sort(Comparator.comparing(Workout::getUpdateTime).reversed());
                    break;
                default:
                    mWorkoutList.sort(Comparator.comparing(Workout::getUpdateTime));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.workout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.import_questions) {
            WorkoutDialogFragment dialog = new WorkoutDialogFragment();
            dialog.show(getSupportFragmentManager(), "subjectDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
