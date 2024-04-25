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

    public MutableLiveData<String> importedSubject = new MutableLiveData<>();
    public MutableLiveData<List<Workout>> fetchedSubjectList = new MutableLiveData<>();

    private final RoutineFetcher mRoutineFetcher;
    private static RoutineRepository mStudyRepo;
    private static final String DATABASE_NAME = "study2.db";
    private final WorkoutDao mWorkoutDao;
    private final ExerciseDao mExerciseDao;

    public static RoutineRepository getInstance(Context context) {
        if (mStudyRepo == null) {
            mStudyRepo = new RoutineRepository(context);
        }
        return mStudyRepo;
    }

    private RoutineRepository(Context context) {
        RoutineDatabase database = Room.databaseBuilder(context, RoutineDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        mWorkoutDao = database.subjectDao();
        mExerciseDao = database.questionDao();
        mRoutineFetcher = new RoutineFetcher(context);
    }

    public void addSubject(Workout workout) {
        long subjectId = mWorkoutDao.addSubject(workout);
        workout.setId(subjectId);
    }

    public LiveData<Workout> getSubject(long subjectId) {
        return mWorkoutDao.getSubject(subjectId);
    }

    public LiveData<List<Workout>> getSubjects() {
        return mWorkoutDao.getSubjects();
    }

    public void deleteSubject(Workout workout) {
        mWorkoutDao.deleteSubject(workout);
    }

    public LiveData<Exercise> getQuestion(long questionId) {
        return mExerciseDao.getQuestion(questionId);
    }

    public LiveData<List<Exercise>> getQuestions(long subjectId) {
        return mExerciseDao.getQuestions(subjectId);
    }

    public void addQuestion(Exercise exercise) {
        long questionId = mExerciseDao.addQuestion(exercise);
        exercise.setId(questionId);
    }

    public void updateQuestion(Exercise exercise) {
        mExerciseDao.updateQuestion(exercise);
    }

    public void deleteQuestion(Exercise exercise) {
        mExerciseDao.deleteQuestion(exercise);
    }

    public void fetchSubjects() {
        mRoutineFetcher.fetchSubjects(mFetchListener);
    }

    public void fetchQuestions(Workout workout) {
        mRoutineFetcher.fetchQuestions(workout, mFetchListener);
    }

    private final RoutineFetcher.OnStudyDataReceivedListener mFetchListener =
            new RoutineFetcher.OnStudyDataReceivedListener() {

                @Override
                public void onSubjectsReceived(List<Workout> workoutList) {
                    fetchedSubjectList.setValue(workoutList);
                }

                @Override
                public void onQuestionsReceived(Workout workout, List<Exercise> exerciseList) {
                    for (Exercise exercise : exerciseList) {
                        exercise.setSubjectId(workout.getId());
                        addQuestion(exercise);
                    }

                    importedSubject.setValue(workout.getText());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            };
}