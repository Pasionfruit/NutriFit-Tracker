package com.zybooks.nutrifittracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.zybooks.nutrifittracker.repo.StudyRepository;
import com.zybooks.nutrifittracker.model.Question;

public class ExerciseDetailViewModel extends AndroidViewModel {

    private StudyRepository mStudyRepo;
    private final MutableLiveData<Long> questionIdLiveData = new MutableLiveData<>();

    public LiveData<Question> questionLiveData =
            Transformations.switchMap(questionIdLiveData, questionId ->
                    mStudyRepo.getQuestion(questionId));

    public ExerciseDetailViewModel(@NonNull Application application) {
        super(application);
        mStudyRepo = StudyRepository.getInstance(application.getApplicationContext());
    }

    public void loadQuestion(long questionId) {
        questionIdLiveData.setValue(questionId);
    }

    public void addQuestion(Question question) {
        mStudyRepo.addQuestion(question);
    }

    public void updateQuestion(Question question) {
        mStudyRepo.updateQuestion(question);
    }
}