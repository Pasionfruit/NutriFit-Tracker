package com.zybooks.nutrifittracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.zybooks.nutrifittracker.model.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Query("SELECT * FROM Workout WHERE id = :id")
    LiveData<Workout> getSubject(long id);

    @Query("SELECT * FROM Workout ORDER BY text COLLATE NOCASE")
    LiveData<List<Workout>> getSubjects();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addSubject(Workout workout);

    @Update
    void updateSubject(Workout workout);

    @Delete
    void deleteSubject(Workout workout);
}