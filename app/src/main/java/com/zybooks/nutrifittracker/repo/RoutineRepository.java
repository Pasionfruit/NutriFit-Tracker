package com.zybooks.nutrifittracker.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.android.volley.VolleyError;
import com.zybooks.nutrifittracker.model.Question;
import com.zybooks.nutrifittracker.model.Subject;

import java.util.List;

public class RoutineRepository {

    public MutableLiveData<String> importedSubject = new MutableLiveData<>();
    public MutableLiveData<List<Subject>> fetchedSubjectList = new MutableLiveData<>();

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

    private void addStarterData() {
        Subject subject = new Subject("Math");
        long subjectId = mWorkoutDao.addSubject(subject);

        Question question = new Question();
        question.setText("What is 2 + 3?");
        question.setAnswer("2 + 3 = 5");
        question.setSubjectId(subjectId);
        mExerciseDao.addQuestion(question);

        question = new Question();
        question.setText("What is pi?");
        question.setAnswer("The ratio of a circle's circumference to its diameter.");
        question.setSubjectId(subjectId);
        mExerciseDao.addQuestion(question);

        subject = new Subject("History");
        subjectId = mWorkoutDao.addSubject(subject);

        question = new Question();
        question.setText("On what date was the U.S. Declaration of Independence adopted?");
        question.setAnswer("July 4, 1776");
        question.setSubjectId(subjectId);
        mExerciseDao.addQuestion(question);

        subject = new Subject("Computing");
        mWorkoutDao.addSubject(subject);
    }

    public void addSubject(Subject subject) {
        long subjectId = mWorkoutDao.addSubject(subject);
        subject.setId(subjectId);
    }

    public LiveData<Subject> getSubject(long subjectId) {
        return mWorkoutDao.getSubject(subjectId);
    }

    public LiveData<List<Subject>> getSubjects() {
        return mWorkoutDao.getSubjects();
    }

    public void deleteSubject(Subject subject) {
        mWorkoutDao.deleteSubject(subject);
    }

    public LiveData<Question> getQuestion(long questionId) {
        return mExerciseDao.getQuestion(questionId);
    }

    public LiveData<List<Question>> getQuestions(long subjectId) {
        return mExerciseDao.getQuestions(subjectId);
    }

    public void addQuestion(Question question) {
        long questionId = mExerciseDao.addQuestion(question);
        question.setId(questionId);
    }

    public void updateQuestion(Question question) {
        mExerciseDao.updateQuestion(question);
    }

    public void deleteQuestion(Question question) {
        mExerciseDao.deleteQuestion(question);
    }

    public void fetchSubjects() {
        mRoutineFetcher.fetchSubjects(mFetchListener);
    }

    public void fetchQuestions(Subject subject) {
        mRoutineFetcher.fetchQuestions(subject, mFetchListener);
    }

    private final RoutineFetcher.OnStudyDataReceivedListener mFetchListener =
            new RoutineFetcher.OnStudyDataReceivedListener() {

                @Override
                public void onSubjectsReceived(List<Subject> subjectList) {
                    fetchedSubjectList.setValue(subjectList);
                }

                @Override
                public void onQuestionsReceived(Subject subject, List<Question> questionList) {
                    for (Question question : questionList) {
                        question.setSubjectId(subject.getId());
                        addQuestion(question);
                    }

                    importedSubject.setValue(subject.getText());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            };
}