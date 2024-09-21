package com.example.pomodoro;

import android.content.Context;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TimerWorker extends Worker {
    public TimerWorker(Context context, WorkerParameters params){
        super(context,params);
    }

    @NonNull
    @Override
    public Result doWork() {
        int secds = getInputData().getInt("minutes", 0) * 60;

        while (secds>0) {

            if (isStopped()) {
                return Result.failure();
            }

            secds--;

            int min = secds / 60;
            int sec = secds % 60;

            setProgressAsync(new Data.Builder()
                    .putInt("minRemaining", min)
                    .putInt("secRemaining", sec)
                    .build());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }

        Data outputData = new Data.Builder()
                .putString("mips", "Chamba finished")
                .build();

        return Result.success(outputData);
    }


}
