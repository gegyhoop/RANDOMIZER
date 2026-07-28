package cz.petane.smbpicker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(
            Context context,
            Intent intent
    ) {

        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            ProfileManager manager =
                    new ProfileManager(context);


            ArrayList<Profile> profiles =
                    manager.getProfiles();


            for(Profile profile : profiles) {

                if(profile.isAutoUpdate()) {

                    Scheduler.schedule(
                            context,
                            profile
                    );
                }
            }
        }
    }
}
