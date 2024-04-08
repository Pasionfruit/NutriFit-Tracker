package com.zybooks.nutrifittracker.ui.macros;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MacrosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MacrosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Macros fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}