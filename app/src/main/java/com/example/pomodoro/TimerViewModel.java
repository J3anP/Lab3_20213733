package com.example.pomodoro;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {
    private final MutableLiveData<Integer> timer = new MutableLiveData<>();
    public MutableLiveData<Integer> getTimer(){
        return timer;
    }
}
