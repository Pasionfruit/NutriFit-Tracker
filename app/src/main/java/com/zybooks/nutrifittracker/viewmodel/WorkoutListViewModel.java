package com.zybooks.nutrifittracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zybooks.nutrifittracker.repo.RoutineRepository;
import com.zybooks.nutrifittracker.model.Subject;

import java.util.List;

public class WorkoutListViewModel extends AndroidViewModel {

    private final RoutineRepository mStudyRepo;

    public WorkoutListViewModel(Application application) {
        super(application);
        mStudyRepo = RoutineRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Subject>> getSubjects() {
        return mStudyRepo.getSubjects();
    }

    public void addSubject(Subject subject) {
        mStudyRepo.addSubject(subject);
    }

    public void deleteSubject(Subject subject) {
        mStudyRepo.deleteSubject(subject);
    }
}