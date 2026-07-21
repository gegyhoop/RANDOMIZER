package cz.petane.smbpicker;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {
    private static final String PREFS_NAME = "smb_profiles";
    private static final String KEY_PROFILES = "profiles_list";

    private final SharedPreferences prefs;
    private final Gson gson;

    public ProfileManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public List<Profile> getAllProfiles() {
        String json = prefs.getString(KEY_PROFILES, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Profile>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public void saveProfiles(List<Profile> profiles) {
        String json = gson.toJson(profiles);
        prefs.edit().putString(KEY_PROFILES, json).apply();
    }

    public Profile getProfileById(String id) {
        for (Profile p : getAllProfiles()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void addProfile(Profile profile) {
        List<Profile> profiles = getAllProfiles();
        profiles.add(profile);
        saveProfiles(profiles);
    }

    public void updateProfile(Profile updatedProfile) {
        List<Profile> profiles = getAllProfiles();
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getId().equals(updatedProfile.getId())) {
                profiles.set(i, updatedProfile);
                break;
            }
        }
        saveProfiles(profiles);
    }

    public void deleteProfile(String id) {
        List<Profile> profiles = getAllProfiles();
        profiles.removeIf(p -> p.getId().equals(id));
        saveProfiles(profiles);
    }
}
