package com.zybooks.nutrifittracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zybooks.nutrifittracker.model.Meal;
import com.zybooks.nutrifittracker.repo.MealRepository;

import java.util.List;

public class MealViewModel extends AndroidViewModel {
    private MealRepository mealRepository;
    private LiveData<List<Meal>> allMeals;

    public MealViewModel(@NonNull Application application) {
        super(application);
        mealRepository = new MealRepository(application);
        allMeals = mealRepository.getAllMeals();
    }

    public LiveData<List<Meal>> getAllMeals() {
        return allMeals;
    }

    public void insert(Meal meal) {
        mealRepository.insert(meal);
    }

    public void delete(Meal mealToDelete) {
        mealRepository.delete(mealToDelete);
    }
}

