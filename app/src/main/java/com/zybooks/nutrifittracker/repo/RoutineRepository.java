package com.zybooks.nutrifittracker.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.android.volley.VolleyError;
import com.zybooks.nutrifittracker.model.Exercise;
import com.zybooks.nutrifittracker.model.Workout;

import java.util.List;

public class RoutineRepository {

    public MutableLiveData<List<Workout>> fetchedWorkoutList = new MutableLiveData<>();
    public MutableLiveData<List<Exercise>> fetchedExerciseList = new MutableLiveData<>();

    private final RoutineFetcher mRoutineFetcher;
    private static RoutineRepository mRoutineRepo;
    private static final String DATABASE_NAME = "routine.db";
    private final ExerciseDao mExerciseDao;
    private final WorkoutDao mWorkoutDao;

    public static RoutineRepository getInstance(Context context) {
        if (mRoutineRepo == null) {
            mRoutineRepo = new RoutineRepository(context);
        }
        return mRoutineRepo;
    }

    private RoutineRepository(Context context) {
        RoutineDatabase database = Room.databaseBuilder(context, RoutineDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        mExerciseDao = database.exerciseDao();
        mWorkoutDao = database.workoutDao();
        mRoutineFetcher = new RoutineFetcher(context);
    }

    public void fetchWorkouts() {
        mRoutineFetcher.fetchWorkouts(mFetchListener);
    }

    public void fetchExercises(Workout workout) {
        mRoutineFetcher.fetchExercises(workout, mFetchListener);
    }

    private final RoutineFetcher.OnRoutineDataReceivedListener mFetchListener =
            new RoutineFetcher.OnRoutineDataReceivedListener() {

                @Override
                public void onWorkoutsReceived(List<Workout> workoutList) {
                    fetchedWorkoutList.setValue(workoutList);
                }

                @Override
                public void onExercisesReceived(Workout workout, List<Exercise> exerciseList) {
                    fetchedExerciseList.setValue(exerciseList);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            };
}
