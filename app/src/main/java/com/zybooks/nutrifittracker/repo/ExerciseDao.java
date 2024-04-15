package com.zybooks.nutrifittracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.zybooks.nutrifittracker.model.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM Exercise WHERE id = :id")
    LiveData<Exercise> getExercise(long id);

    @Query("SELECT * FROM Exercise WHERE workout_id = :workoutId ORDER BY id")
    LiveData<List<Exercise>> getExercisesForWorkout(long workoutId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Delete
    void deleteExercise(Exercise exercise);
}
