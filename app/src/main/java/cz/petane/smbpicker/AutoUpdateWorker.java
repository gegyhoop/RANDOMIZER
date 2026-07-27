package cz.petane.smbpicker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
                    getInputData().getString("profileName");

            if (profileName == null) {
                return Result.failure();
            }

            ProfileManager manager =
                    new ProfileManager(getApplicationContext());

            Profile profile =
                    manager.getProfileById(profileName);

            if (profile.getName() == null) {
                return Result.failure();
            }

            EpisodePicker picker =
                    new EpisodePicker(profile);

            List<String> files =
                    picker.prepareEpisodes();

            if (files.isEmpty()) {
                return Result.retry();
            }

            NotificationHelper.showNotification(
                    getApplicationContext(),
                    profile.getName(),
                    files
            );

            return Result.success();

        } catch (Exception e) {

            e.printStackTrace();

            return Result.retry();
        }
    }

}
