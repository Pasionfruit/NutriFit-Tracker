package com.zybooks.nutrifittracker.repo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zybooks.nutrifittracker.model.Exercise;
import com.zybooks.nutrifittracker.model.Workout;

@Database(entities = {Exercise.class, Workout.class}, version = 4)
public abstract class RoutineDatabase extends RoomDatabase {

    public abstract ExerciseDao questionDao();
    public abstract WorkoutDao subjectDao();
}