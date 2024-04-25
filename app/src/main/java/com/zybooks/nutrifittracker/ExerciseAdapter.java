package com.zybooks.nutrifittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.nutrifittracker.model.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> mExercises;

    public ExerciseAdapter(List<Exercise> exercises) {
        mExercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = mExercises.get(position);
        holder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        mExercises = exercises;
        notifyDataSetChanged();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        private TextView mExerciseNameTextView;
        private TextView mRepetitionsTextView;
        private TextView mWeightTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExerciseNameTextView = itemView.findViewById(R.id.exercise_name_text_view);
            mRepetitionsTextView = itemView.findViewById(R.id.repetitions_text_view);
            mWeightTextView = itemView.findViewById(R.id.weight_text_view);
        }

        public void bind(Exercise exercise) {
            mExerciseNameTextView.setText(exercise.getText());
            mRepetitionsTextView.setText("Repetitions: " + exercise.getAnswer());
            mWeightTextView.setText("Weight: " + exercise.getWeight());
        }
    }
}

