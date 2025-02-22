package com.zybooks.nutrifittracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.zybooks.nutrifittracker.model.Exercise;
import com.zybooks.nutrifittracker.model.Workout;
import com.zybooks.nutrifittracker.viewmodel.ExerciseListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    public static final String EXTRA_SUBJECT_ID = "com.zybooks.studyhelper.subject_id";
    public static final String EXTRA_SUBJECT_TEXT  = "com.zybooks.studyhelper.subject_text";

    private ExerciseListViewModel mQuestionListViewModel;
    private Workout mWorkout;
    private List<Exercise> mExerciseList;
    private TextView mAnswerTextView;
    private Button mAnswerButton;

    private TextView mNameTextView;
    private TextView mQuestionTextView;
    private ViewGroup mShowQuestionLayout;
    private ViewGroup mNoQuestionLayout;
    private int mCurrentQuestionIndex = 0;
    private RecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        mNameTextView = findViewById(R.id.exercise_name_text_view);
        mQuestionTextView = findViewById(R.id.repetition_text_view);
        mAnswerTextView = findViewById(R.id.weight_text_view);
        mAnswerButton = findViewById(R.id.answer_button);
        mShowQuestionLayout = findViewById(R.id.show_question_layout);
        mNoQuestionLayout = findViewById(R.id.no_question_layout);

        // Add click callbacks
        mAnswerButton.setOnClickListener(view -> logWorkout());
        findViewById(R.id.add_question_button).setOnClickListener(view -> addQuestion());

        // SubjectActivity should provide the subject ID and text
        Intent intent = getIntent();
        long subjectId = intent.getLongExtra(EXTRA_SUBJECT_ID, 0);
        String subjectText = intent.getStringExtra(EXTRA_SUBJECT_TEXT);
        assert subjectText != null;
        mWorkout = new Workout(subjectText);
        mWorkout.setId(subjectId);

        mQuestionListViewModel = new ViewModelProvider(this).get(ExerciseListViewModel.class);

        mQuestionListViewModel.loadQuestions(subjectId);
        mQuestionListViewModel.questionListLiveData.observe(this, questions -> {
            mExerciseList = questions;
            updateUI();
        });
        mRecyclerView = findViewById(R.id.recycler_view_exercises);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExerciseAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        // Initialize ViewModel and observe changes
        mQuestionListViewModel = new ViewModelProvider(this).get(ExerciseListViewModel.class);
        mQuestionListViewModel.loadQuestions(subjectId);
        mQuestionListViewModel.questionListLiveData.observe(this, exercises -> {
            mExerciseList = exercises;
            mAdapter.setExercises(exercises);
        });
    }

    private void updateUI() {
        showQuestion(mCurrentQuestionIndex);

        if (mExerciseList.isEmpty()) {
            updateAppBarTitle();
            displayQuestion(false);
        } else {
            displayQuestion(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercise_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //  Determine which app bar item was chosen
        if (item.getItemId() == R.id.previous) {
            showQuestion(mCurrentQuestionIndex - 1);
            return true;
        }
        else if (item.getItemId() == R.id.next) {
            showQuestion(mCurrentQuestionIndex + 1);
            return true;
        }
        else if (item.getItemId() == R.id.add) {
            addQuestion();
            return true;
        }
        else if (item.getItemId() == R.id.edit) {
            editQuestion();
            return true;
        }
        else if (item.getItemId() == R.id.delete) {
            deleteQuestion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayQuestion(boolean display) {
        if (display) {
            mShowQuestionLayout.setVisibility(View.VISIBLE);
            mNoQuestionLayout.setVisibility(View.GONE);
        }
        else {
            mShowQuestionLayout.setVisibility(View.GONE);
            mNoQuestionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateAppBarTitle() {

        // Display subject and number of questions in app bar
        String title = getResources().getString(R.string.question_number,
                mWorkout.getText(), mCurrentQuestionIndex + 1, mExerciseList.size());
        setTitle(title);
    }

    private final ActivityResultLauncher<Intent> mAddQuestionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    // Display the added question, which will appear at end of list
                    mCurrentQuestionIndex = mExerciseList.size();
                    Toast.makeText(ExerciseActivity.this, R.string.question_added, Toast.LENGTH_SHORT).show();
                }
            });

    private void addQuestion() {
        Intent intent = new Intent(this, ExerciseEditActivity.class);
        intent.putExtra(ExerciseEditActivity.EXTRA_SUBJECT_ID, mWorkout.getId());
        mAddQuestionResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> mEditQuestionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Toast.makeText(ExerciseActivity.this, R.string.question_updated, Toast.LENGTH_SHORT).show();
                }
            });

    private void editQuestion() {
        if (mCurrentQuestionIndex >= 0) {
            Intent intent = new Intent(this, ExerciseEditActivity.class);
            long questionId = mExerciseList.get(mCurrentQuestionIndex).getId();
            intent.putExtra(ExerciseEditActivity.EXTRA_QUESTION_ID, questionId);
            mEditQuestionResultLauncher.launch(intent);
        }
    }

    private void deleteQuestion() {
        if (mCurrentQuestionIndex >= 0) {
            Exercise exercise = mExerciseList.get(mCurrentQuestionIndex);
            mQuestionListViewModel.deleteQuestion(exercise);
            Toast.makeText(this, R.string.question_deleted, Toast.LENGTH_SHORT).show();

            // Show delete message with Undo button
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),
                    R.string.question_deleted, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, view -> {
                // Add question back
                mQuestionListViewModel.addQuestion(exercise);
            });
            snackbar.show();
        }
    }

    private void showQuestion(int questionIndex) {

        // Show question at the given index
        if (mExerciseList.size() > 0) {
            if (questionIndex < 0) {
                questionIndex = mExerciseList.size() - 1;
            }
            else if (questionIndex >= mExerciseList.size()) {
                questionIndex = 0;
            }

            mCurrentQuestionIndex = questionIndex;
            updateAppBarTitle();

            Exercise exercise = mExerciseList.get(mCurrentQuestionIndex);
            mQuestionTextView.setText(exercise.getWeight());
            mAnswerTextView.setText(exercise.getAnswer());
            mNameTextView.setText(exercise.getText());
        }
        else {
            // No questions yet
            mCurrentQuestionIndex = -1;
        }
    }

    private void logWorkout() {
        // Create a new Exercise object with the current exercise details
        Exercise loggedExercise = new Exercise(
                mNameTextView.getText().toString(), // Exercise name
                mQuestionTextView.getText().toString(), // Repetitions
                mAnswerTextView.getText().toString() // Weight
        );

        // Add the logged exercise to the list of exercises
        if (mExerciseList != null) {
            mExerciseList.add(loggedExercise);
            // Notify the adapter that the dataset has changed
            mAdapter.setExercises(mExerciseList);
            mAdapter.notifyDataSetChanged();
        }

        // Show a toast message to indicate that the exercise has been logged
        Toast.makeText(this, "Exercise logged", Toast.LENGTH_SHORT).show();
    }
}