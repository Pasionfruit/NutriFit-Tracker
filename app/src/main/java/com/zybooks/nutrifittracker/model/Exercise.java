package com.zybooks.nutrifittracker.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Workout.class, parentColumns = "id",
        childColumns = "workout_id", onDelete = CASCADE))
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "exercise_name")
    private String mExerciseName;

    @ColumnInfo(name = "reps")
    private int mReps;

    @ColumnInfo(name = "weight")
    private double mWeight;

    @ColumnInfo(name = "workout_id")
    private long mWorkoutId;


    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        mExerciseName = exerciseName;
    }

    public int getReps() {
        return mReps;
    }

    public void setReps(int reps) {
        mReps = reps;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public long getWorkoutId() {
        return mWorkoutId;
    }

    public void setWorkoutId(long workoutId) {
        mWorkoutId = workoutId;
    }
}
