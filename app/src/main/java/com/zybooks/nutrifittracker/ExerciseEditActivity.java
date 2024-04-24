package com.zybooks.nutrifittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.zybooks.nutrifittracker.model.Question;
import com.zybooks.nutrifittracker.viewmodel.QuestionDetailViewModel;

public class ExerciseEditActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_ID = "com.zybooks.nutrifittracker.question_id";
    public static final String EXTRA_SUBJECT_ID = "com.zybooks.nutrifittracker.subject_id";

    private EditText exerciseNameEditText;
    private EditText repetitionsEditText;
    private EditText weightEditText;
    private Question mQuestion;
    private QuestionDetailViewModel mQuestionDetailViewModel;
    private long mQuestionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_edit);

        exerciseNameEditText = findViewById(R.id.exercise_name_edit_text);
        repetitionsEditText = findViewById(R.id.repetition_name_edit_text);
        weightEditText = findViewById(R.id.weight_name_edit_text);

        findViewById(R.id.save_button).setOnClickListener(view -> {
            if (validateInputs()) {
                saveButtonClick();
            }
        });

        Intent intent = getIntent();
        mQuestionId = intent.getLongExtra(EXTRA_QUESTION_ID, -1);
        mQuestionDetailViewModel = new ViewModelProvider(this).get(QuestionDetailViewModel.class);

        if (mQuestionId == -1) {
            mQuestion = new Question();
            mQuestion.setSubjectId(intent.getLongExtra(EXTRA_SUBJECT_ID, 0));
            setTitle(R.string.add_exercise);
        } else {
            mQuestionDetailViewModel.loadQuestion(mQuestionId);
            mQuestionDetailViewModel.questionLiveData.observe(this, question -> {
                mQuestion = question;
                updateUI();
            });
            setTitle(R.string.edit_question);
        }
    }

    private boolean validateInputs() {
        String exerciseName = exerciseNameEditText.getText().toString().trim();
        String repetitions = repetitionsEditText.getText().toString().trim();
        String weight = weightEditText.getText().toString().trim();

        if (exerciseName.isEmpty() || repetitions.isEmpty() || weight.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            int repNum = Integer.parseInt(repetitions);
            double weightNum = Double.parseDouble(weight);
            if (repNum <= 0 || weightNum <= 0) {
                Toast.makeText(this, "Repetitions and weight must be greater than zero", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Repetitions and weight must be valid numbers", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void updateUI() {
        exerciseNameEditText.setText(mQuestion.getText());
        repetitionsEditText.setText(mQuestion.getAnswer()); // Assume repetitions and weight in one field for simplicity, split if necessary
        weightEditText.setText(""); // Assuming another field or parsing logic needed
    }

    private void saveButtonClick() {
        if (mQuestionId == -1) {
            mQuestionDetailViewModel.addQuestion(mQuestion);
        } else {
            mQuestionDetailViewModel.updateQuestion(mQuestion);
        }
        setResult(RESULT_OK);
        finish();
    }
}
