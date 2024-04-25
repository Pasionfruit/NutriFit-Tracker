package com.zybooks.nutrifittracker.repo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.zybooks.nutrifittracker.model.Question;
import com.zybooks.nutrifittracker.model.Subject;

@Database(entities = {Question.class, Subject.class}, version = 2)
public abstract class RoutineDatabase extends RoomDatabase {

    public abstract ExerciseDao questionDao();
    public abstract WorkoutDao subjectDao();
}