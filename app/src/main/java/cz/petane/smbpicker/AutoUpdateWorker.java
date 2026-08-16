package cz.petane.smbpicker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

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
                    getInputData()
                            .getString("profileName");

            boolean manualUpdate =
                    getInputData()
                            .getBoolean(
                                    "manualUpdate",
                                    false
                            );

            boolean lastManualUpdate =
                    getInputData()
                            .getBoolean(
                                    "lastManualUpdate",
                                    false
                            );

            if(profileName == null) {
                return Result.failure();
            }

            ProfileManager manager =
                    new ProfileManager(
                            getApplicationContext()
                    );

            Profile profile =
                    manager.getProfileById(
                            profileName
                    );

            if(profile == null ||
                    profile.getName() == null) {

                return Result.failure();
            }

            EpisodePicker picker =
                    new EpisodePicker(profile);

            List<String> files =
                    picker.prepareEpisodes();

            if(!manualUpdate) {

                if(!files.isEmpty()) {

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

            ArrayList<String> allFiles =
                    new ArrayList<>();

            String[] previousFiles =
                    getInputData()
                            .getStringArray(
                                    "updatedFiles"
                            );

            if(previousFiles != null) {

                for(String file : previousFiles) {

                    if(file != null &&
                            !file.isEmpty()) {

                        allFiles.add(file);
                    }
                }
            }

            if(files != null) {

                for(String file : files) {

                    if(file != null &&
                            !file.isEmpty()) {

                        allFiles.add(
                                profile.getName()
                                        + ": "
                                        + file
                        );
                    }
                }
            }

            if(lastManualUpdate) {

                NotificationHelper
                        .showManualUpdateNotification(
                                getApplicationContext(),
                                allFiles
                        );

            }

            Data output =
                    new Data.Builder()
                            .putStringArray(
                                    "updatedFiles",
                                    allFiles.toArray(
                                            new String[0]
                                    )
                            )
                            .build();

            return Result.success(output);

        }
        catch(Exception e) {

            e.printStackTrace();

            return Result.retry();
        }
    }
}
