package com.zybooks.nutrifittracker.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.zybooks.nutrifittracker.model.Meal;

import java.util.List;

public class MealRepository {
    private MealDao mealDao;
    private LiveData<List<Meal>> allMeals;

    public MealRepository(Application application) {
        MealDatabase database = MealDatabase.getInstance(application);
        mealDao = database.mealDao();
        allMeals = mealDao.getAllMeals();
    }

    public LiveData<List<Meal>> getAllMeals() {
        return allMeals;
    }

    public void insert(Meal meal) {
        MealDatabase.databaseWriteExecutor.execute(() -> {
            mealDao.insert(meal);
        });
    }

    public void delete(Meal mealToDelete) {
        MealDatabase.databaseWriteExecutor.execute(() -> {
            mealDao.delete(mealToDelete);
        });
    }
}

