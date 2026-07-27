package cz.petane.smbpicker;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    public static void schedule(Context context, Profile profile) {

        cancel(context, profile);

        if (!profile.isAutoUpdate()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime next =
                now.withHour(profile.getUpdateHour())
                        .withMinute(profile.getUpdateMinute())
                        .withSecond(0)
                        .withNano(0);

        if (next.isBefore(now)) {
            next = next.plusDays(1);
        }

        long delay =
                Duration.between(now, next).toMinutes();

        Data data =
                new Data.Builder()
                        .putString("profileName", profile.getName())
                        .build();

        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        AutoUpdateWorker.class,
                        1,
                        TimeUnit.DAYS
                )
                        .setInitialDelay(
                                delay,
                                TimeUnit.MINUTES
                        )
                        .setInputData(data)
                        .build();

        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                        profile.getName(),
                        ExistingPeriodicWorkPolicy.UPDATE,
                        request
                );
    }

    public static void cancel(Context context, Profile profile) {

        WorkManager.getInstance(context)
                .cancelUniqueWork(profile.getName());
    }

}
