package com.zybooks.nutrifittracker.repo;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.zybooks.nutrifittracker.model.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM Exercise WHERE id = :id")
    LiveData<Exercise> getQuestion(long id);

    @Query("SELECT * FROM Exercise WHERE subject_id = :subjectId ORDER BY id")
    LiveData<List<Exercise>> getQuestions(long subjectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addQuestion(Exercise exercise);

    @Update
    void updateQuestion(Exercise exercise);

    @Delete
    void deleteQuestion(Exercise exercise);
}