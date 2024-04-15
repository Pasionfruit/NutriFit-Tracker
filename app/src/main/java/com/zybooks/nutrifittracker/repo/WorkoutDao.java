package com.zybooks.nutrifittracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.zybooks.nutrifittracker.model.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM Workout WHERE id = :id")
    LiveData<Workout> getWorkout(long id);

    @Query("SELECT * FROM Workout ORDER BY id")
    LiveData<List<Workout>> getAllWorkouts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addWorkout(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);
}
