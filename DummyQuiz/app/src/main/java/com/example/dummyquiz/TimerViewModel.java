package com.example.dummyquiz;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {

    MutableLiveData<Integer> seconds = new MutableLiveData<>();
    CountDownTimer timer_c;
    static int timetakenstat;

    public void starttimer() {
        timer_c = new CountDownTimer(250000, 1000) {
            @Override
            public void onTick(long l) {
                int timeleft = (int) l / 1000;
                seconds.setValue(timeleft);
                timetakenstat = timeleft;
            }

            @Override
            public void onFinish() {
                Log.d("Debugger1", "Timer completed, now navigating to summary page");
            }
        }.start();
    }

    public void setSeconds(MutableLiveData<Integer> seconds) {
        this.seconds = seconds;
    }

    public MutableLiveData<Integer> getSeconds() {
        return seconds;
    }

    public int getSecondsforSummary() {
        return timetakenstat;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timer_c.cancel();
    }
}
