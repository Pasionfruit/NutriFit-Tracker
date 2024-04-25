package com.zybooks.nutrifittracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zybooks.nutrifittracker.model.Workout;
import com.zybooks.nutrifittracker.repo.RoutineRepository;

import java.util.List;

public class WorkoutListViewModel extends AndroidViewModel {

    private final RoutineRepository mStudyRepo;

    public WorkoutListViewModel(Application application) {
        super(application);
        mStudyRepo = RoutineRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Workout>> getSubjects() {
        return mStudyRepo.getSubjects();
    }

    public void addSubject(Workout workout) {
        mStudyRepo.addSubject(workout);
    }

    public void deleteSubject(Workout workout) {
        mStudyRepo.deleteSubject(workout);
    }
}