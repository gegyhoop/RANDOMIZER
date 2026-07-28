package cz.petane.smbpicker;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    private static String getWorkName(Profile profile) {
        return "AUTO_UPDATE_" + profile.getName();
    }

    public static void schedule(
            Context context,
            Profile profile
    ) {

        if(!profile.isAutoUpdate()) {
            cancel(context, profile);
            return;
        }

        LocalDateTime now =
                LocalDateTime.now();

        LocalDateTime next =
                now.withHour(
                        profile.getUpdateHour()
                )
                .withMinute(
                        profile.getUpdateMinute()
                )
                .withSecond(0)
                .withNano(0);

        if(!next.isAfter(now)) {
            next = next.plusDays(1);
        }

        long delay =
                Duration.between(
                        now,
                        next
                ).toMinutes();

        Data data =
                new Data.Builder()
                        .putString(
                                "profileName",
                                profile.getName()
                        )
                        .build();

        OneTimeWorkRequest request =
                new OneTimeWorkRequest.Builder(
                        AutoUpdateWorker.class
                )
                .setInitialDelay(
                        delay,
                        TimeUnit.MINUTES
                )
                .setInputData(data)
                .build();

        WorkManager.getInstance(context)
                .enqueueUniqueWork(
                        getWorkName(profile),
                        ExistingWorkPolicy.REPLACE,
                        request
                );
    }

    public static void cancel(
            Context context,
            Profile profile
    ) {

        WorkManager.getInstance(context)
                .cancelUniqueWork(
                        getWorkName(profile)
                );
    }
}
