package com.zybooks.nutrifittracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.zybooks.nutrifittracker.model.Question;
import com.zybooks.nutrifittracker.repo.RoutineRepository;

import java.util.List;

public class ExerciseListViewModel extends AndroidViewModel {

    private RoutineRepository mStudyRepo;
    private final MutableLiveData<Long> mSubjectIdLiveData = new MutableLiveData<>();

    public LiveData<List<Question>> questionListLiveData =
            Transformations.switchMap(mSubjectIdLiveData, subjectId ->
                    mStudyRepo.getQuestions(subjectId));

    public ExerciseListViewModel(@NonNull Application application) {
        super(application);
        mStudyRepo = RoutineRepository.getInstance(application.getApplicationContext());
    }

    public void loadQuestions(long subjectId) {
        mSubjectIdLiveData.setValue(subjectId);
    }

    public void addQuestion(Question question) {
        mStudyRepo.addQuestion(question);
    }

    public void deleteQuestion(Question question) {
        mStudyRepo.deleteQuestion(question);
    }
}