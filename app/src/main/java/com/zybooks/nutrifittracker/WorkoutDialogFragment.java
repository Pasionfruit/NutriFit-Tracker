package com.zybooks.nutrifittracker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class WorkoutDialogFragment extends DialogFragment {

    public interface OnWorkoutEnteredListener {
        void onWorkoutEntered(String workoutText);
    }

    private OnWorkoutEnteredListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final EditText workoutEditText = new EditText(requireActivity());
        workoutEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        workoutEditText.setMaxLines(1);

        return new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.workout)
                .setView(workoutEditText)
                .setPositiveButton(R.string.create, (dialog, whichButton) -> {
                    // Notify listener
                    String workout = workoutEditText.getText().toString();
                    mListener.onWorkoutEntered(workout.trim());
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnWorkoutEnteredListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnWorkoutEnteredListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
