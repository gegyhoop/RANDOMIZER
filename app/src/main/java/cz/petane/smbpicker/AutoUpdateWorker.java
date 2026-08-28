package cz.petane.smbpicker;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AutoUpdateWorker extends Worker {

    public AutoUpdateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {

            String profileName =
                    getInputData().getString("profileName");

            boolean manualUpdate =
                    getInputData().getBoolean(
                            "manualUpdate",
                            false
                    );

            boolean lastManualUpdate =
                    getInputData().getBoolean(
                            "lastManualUpdate",
                            false
                    );

            String manualUpdateId =
                    getInputData().getString(
                            "manualUpdateId"
                    );

            if(profileName == null)
                return Result.failure();

            ProfileManager manager =
                    new ProfileManager(
                            getApplicationContext()
                    );

            Profile profile =
                    manager.getProfileById(profileName);

            if(profile == null ||
                    profile.getName() == null)
                return Result.failure();

            EpisodePicker picker =
                    new EpisodePicker(
                            profile,
                            getApplicationContext()
                    );

            List<String> files =
                    picker.prepareEpisodes();

            if(!manualUpdate) {

                if(files != null && !files.isEmpty()) {

                    NotificationHelper.showNotification(
                            getApplicationContext(),
                            profile.getName(),
                            files
                    );
                }

                Scheduler.schedule(
                        getApplicationContext(),
                        profile
                );

                return Result.success();
            }

            if(manualUpdateId == null ||
                    manualUpdateId.isEmpty())
                return Result.failure();

            Context context =
                    getApplicationContext();

            SharedPreferences preferences =
                    context.getSharedPreferences(
                            "manual_update",
                            Context.MODE_PRIVATE
                    );

            String currentUpdateId =
                    preferences.getString(
                            "current_update_id",
                            null
                    );

            if(!manualUpdateId.equals(currentUpdateId))
                return Result.failure();

            String key =
                    "updated_files_" + manualUpdateId;

            Set<String> storedFiles =
                    preferences.getStringSet(
                            key,
                            new LinkedHashSet<>()
                    );

            LinkedHashSet<String> allFiles =
                    new LinkedHashSet<>(storedFiles);

            if(files != null) {

                for(String file : files) {

                    if(file != null && !file.isEmpty()) {

                        allFiles.add(
                                profile.getName()
                                        + ": "
                                        + file
                        );
                    }
                }
            }

            preferences
                    .edit()
                    .putStringSet(key, allFiles)
                    .apply();

            if(lastManualUpdate) {

                ArrayList<String> notificationFiles =
                        new ArrayList<>(allFiles);

                NotificationHelper
                        .showManualUpdateNotification(
                                context,
                                notificationFiles
                        );

                preferences
                        .edit()
                        .remove(key)
                        .remove("current_update_id")
                        .apply();
            }

            return Result.success();

        } catch(Exception e) {

            e.printStackTrace();

            return Result.retry();
        }
    }
}
