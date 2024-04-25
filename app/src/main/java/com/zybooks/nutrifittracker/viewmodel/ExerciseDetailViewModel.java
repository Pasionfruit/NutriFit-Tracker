package com.zybooks.nutrifittracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.zybooks.nutrifittracker.repo.RoutineRepository;
import com.zybooks.nutrifittracker.model.Exercise;

public class ExerciseDetailViewModel extends AndroidViewModel {

    private RoutineRepository mStudyRepo;
    private final MutableLiveData<Long> questionIdLiveData = new MutableLiveData<>();

    public LiveData<Exercise> questionLiveData =
            Transformations.switchMap(questionIdLiveData, questionId ->
                    mStudyRepo.getQuestion(questionId));

    public ExerciseDetailViewModel(@NonNull Application application) {
        super(application);
        mStudyRepo = RoutineRepository.getInstance(application.getApplicationContext());
    }

    public void loadQuestion(long questionId) {
        questionIdLiveData.setValue(questionId);
    }

    public void addQuestion(Exercise exercise) {
        mStudyRepo.addQuestion(exercise);
    }

    public void updateQuestion(Exercise exercise) {
        mStudyRepo.updateQuestion(exercise);
    }
}