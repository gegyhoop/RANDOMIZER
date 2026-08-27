package cz.petane.smbpicker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProfileManager {

    private static final String PREF = "profiles";
    private static final String KEY = "data";

    private final Context context;

    public ProfileManager(Context context) {
        this.context = context;
    }

    public ArrayList<Profile> getProfiles() {
        try {
            String json = context.getSharedPreferences(PREF, 0)
                    .getString(KEY, "");

            if(json.isEmpty()) return new ArrayList<>();

            Type type = new TypeToken<ArrayList<Profile>>(){}.getType();
            ArrayList<Profile> profiles = new Gson().fromJson(json, type);

            if(profiles == null) return new ArrayList<>();

            for(Profile profile : profiles)
                profile.setContext(context);

            return profiles;

        } catch(Exception e) {
            return new ArrayList<>();
        }
    }

    public void saveProfiles(ArrayList<Profile> profiles) {
        for(Profile profile : profiles)
            profile.setContext(context);

        context.getSharedPreferences(PREF, 0)
                .edit()
                .putString(KEY, new Gson().toJson(profiles))
                .apply();
    }

    public Profile getProfileById(String id) {
        for(Profile profile : getProfiles()) {
            if(profile.getName() != null &&
                    profile.getName().equals(id))
                return profile;
        }

        return null;
    }

    public void updateProfile(Profile profile) {
        ArrayList<Profile> profiles = getProfiles();

        for(int i = 0; i < profiles.size(); i++) {
            if(profiles.get(i).getName() != null &&
                    profiles.get(i).getName().equals(profile.getName())) {
                profiles.set(i, profile);
                saveProfiles(profiles);
                return;
            }
        }

        profiles.add(profile);
        saveProfiles(profiles);
    }

    public void deleteProfile(Profile profile) {
        Scheduler.cancel(context, profile);

        new EpisodeHistoryManager(context)
                .clear(profile.getName());

        ArrayList<Profile> profiles = getProfiles();

        profiles.removeIf(p ->
                p.getName() != null &&
                p.getName().equals(profile.getName())
        );

        saveProfiles(profiles);
    }
}
