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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zybooks.nutrifittracker.model.Subject;
import com.zybooks.nutrifittracker.ui.diary.DiaryFragment;
import com.zybooks.nutrifittracker.ui.macros.MacrosFragment;
import com.zybooks.nutrifittracker.viewmodel.SubjectListViewModel;

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
    private SubjectListViewModel mSubjectListViewModel;
    private Subject mSelectedSubject;
    private int mSelectedSubjectPosition = RecyclerView.NO_POSITION;
    private ActionMode mActionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mSubjectListViewModel = new ViewModelProvider(this).get(SubjectListViewModel.class);

        mSubjectColors = getResources().getIntArray(R.array.subjectColors);

        // Create 2 grid layout columns
        mRecyclerView = findViewById(R.id.subject_recycler_view);
        RecyclerView.LayoutManager gridLayoutManager =
                new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Call updateUI() when the subject list changes
        mSubjectListViewModel.getSubjects().observe(this, subjects -> {
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
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId(); // Get the ID of the selected item

                    // Use if-else statements instead of switch
                    if (itemId == R.id.navigation_macros) {
                        selectedFragment = new MacrosFragment();
                    } else if (itemId == R.id.navigation_workout) {
                        // Already in the WorkoutActivity, no need to navigate
                        return true;
                    } else if (itemId == R.id.navigation_diary) {
                        selectedFragment = new DiaryFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, selectedFragment)
                                .commit();
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

    private void updateUI(List<Subject> subjectList) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        boolean isWorkoutSelected = selectedItemId == R.id.navigation_workout;

        mSubjectAdapter = new SubjectAdapter(subjectList, isWorkoutSelected);
        mSubjectAdapter.setSortOrder(getSettingsSortOrder());
        mRecyclerView.setAdapter(mSubjectAdapter);
    }

    @Override
    public void onSubjectEntered(String subjectText) {
        if (subjectText.length() > 0) {
            Subject subject = new Subject(subjectText);
            mLoadSubjectList = false;
            mSubjectListViewModel.addSubject(subject);
            mSubjectAdapter.addSubject(subject);
            Toast.makeText(this, "Added " + subjectText, Toast.LENGTH_SHORT).show();
        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private Subject mSubject;
        private final TextView mSubjectTextView;

        public SubjectHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));
            itemView.setOnClickListener(this);
            mSubjectTextView = itemView.findViewById(R.id.subject_text_view);
            itemView.setOnLongClickListener(this);
        }

        public void bind(Subject subject, int position, boolean isWorkoutSelected) {
            mSubject = subject;
            mSubjectTextView.setText(subject.getText());

            if (isWorkoutSelected) {
                if (mSelectedSubjectPosition == position) {
                    // Make selected subject stand out
                    mSubjectTextView.setBackgroundColor(Color.RED);
                } else {
                    // Make the background color dependent on the length of the subject string
                    int colorIndex = subject.getText().length() % mSubjectColors.length;
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
            intent.putExtra(ExerciseActivity.EXTRA_SUBJECT_ID, mSubject.getId());
            intent.putExtra(ExerciseActivity.EXTRA_SUBJECT_TEXT, mSubject.getText());
            startActivity(intent);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            }
            mSelectedSubject = mSubject;
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
                mSubjectListViewModel.deleteSubject(mSelectedSubject);
                mSubjectAdapter.removeSubject(mSelectedSubject);
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
        private final List<Subject> mSubjectList;
        private final boolean mIsWorkoutSelected;

        public SubjectAdapter(List<Subject> subjects, boolean isWorkoutSelected) {
            mSubjectList = subjects;
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
            holder.bind(mSubjectList.get(position), position, mIsWorkoutSelected);
        }

        @Override
        public int getItemCount() {
            return mSubjectList.size();
        }

        public void addSubject(Subject subject) {
            mSubjectList.add(0, subject);
            notifyItemInserted(0);
            mRecyclerView.scrollToPosition(0);
        }

        public void removeSubject(Subject subject) {
            int index = mSubjectList.indexOf(subject);
            if (index >= 0) {
                mSubjectList.remove(index);
                notifyItemRemoved(index);
            }
        }

        public void setSortOrder(SubjectSortOrder sortOrder) {
            switch (sortOrder) {
                case ALPHABETIC:
                    mSubjectList.sort(Comparator.comparing(Subject::getText));
                    break;
                case NEW_FIRST:
                    mSubjectList.sort(Comparator.comparing(Subject::getUpdateTime).reversed());
                    break;
                default:
                    mSubjectList.sort(Comparator.comparing(Subject::getUpdateTime));
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
