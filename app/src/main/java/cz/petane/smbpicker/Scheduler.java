package cz.petane.smbpicker;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    private static final String MANUAL_UPDATE_ALL =
            "MANUAL_UPDATE_ALL";

    private static String getWorkName(Profile profile) {
        return "AUTO_UPDATE_" + profile.getName();
    }

    public static void schedule(
            Context context,
            Profile profile
    ) {

        if(!profile.isAutoUpdate()) {

            cancel(
                    context,
                    profile
            );

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
                        .putBoolean(
                                "manualUpdate",
                                false
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

    public static void updateAllProfiles(
            Context context,
            ArrayList<Profile> profiles
    ) {

        if(profiles == null ||
                profiles.isEmpty()) {

            return;
        }

        WorkManager workManager =
                WorkManager.getInstance(context);

        workManager.cancelUniqueWork(
                MANUAL_UPDATE_ALL
        );

        String updateId =
                UUID.randomUUID().toString();

        Context appContext =
                context.getApplicationContext();

        appContext
                .getSharedPreferences(
                        "manual_update",
                        Context.MODE_PRIVATE
                )
                .edit()
                .putString(
                        "current_update_id",
                        updateId
                )
                .putStringSet(
                        "updated_files_" + updateId,
                        new java.util.LinkedHashSet<>()
                )
                .apply();

        WorkContinuation continuation = null;

        for(int i = 0;
                i < profiles.size();
                i++) {

            Profile profile =
                    profiles.get(i);

            boolean last =
                    i == profiles.size() - 1;

            Data data =
                    new Data.Builder()
                            .putString(
                                    "profileName",
                                    profile.getName()
                            )
                            .putBoolean(
                                    "manualUpdate",
                                    true
                            )
                            .putBoolean(
                                    "lastManualUpdate",
                                    last
                            )
                            .putString(
                                    "manualUpdateId",
                                    updateId
                            )
                            .build();

            OneTimeWorkRequest request =
                    new OneTimeWorkRequest.Builder(
                            AutoUpdateWorker.class
                    )
                    .setInputData(data)
                    .build();

            if(continuation == null) {

                continuation =
                        workManager.beginUniqueWork(
                                MANUAL_UPDATE_ALL,
                                ExistingWorkPolicy.REPLACE,
                                request
                        );

            } else {

                continuation =
                        continuation.then(request);
            }
        }

        if(continuation != null) {
            continuation.enqueue();
        }
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
