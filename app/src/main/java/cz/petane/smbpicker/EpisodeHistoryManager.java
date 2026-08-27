package cz.petane.smbpicker;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EpisodeHistoryManager {

    private static final String PREF = "episode_history";
    private final SharedPreferences prefs;

    public EpisodeHistoryManager(Context context) {
        prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public Set<String> getBlocked(String profileId, int historySize) {
        Set<String> blocked = new LinkedHashSet<>();
        int size = prefs.getInt("size_" + profileId, 0);

        for(int i = 0; i < Math.min(size, historySize); i++) {
            Set<String> files = prefs.getStringSet(
                    "h_" + profileId + "_" + i, null
            );
            if(files != null) blocked.addAll(files);
        }

        return blocked;
    }

    public void add(
            String profileId,
            List<String> files,
            int historySize
    ) {
        if(files == null || files.isEmpty() || historySize <= 0) return;

        String key = "h_" + profileId + "_";
        int size = prefs.getInt("size_" + profileId, 0);

        SharedPreferences.Editor e = prefs.edit();

        for(int i = Math.min(size, historySize - 1); i > 0; i--) {
            Set<String> old = prefs.getStringSet(key + (i - 1), null);
            if(old != null) e.putStringSet(key + i, new LinkedHashSet<>(old));
        }

        e.putStringSet(key + "0", new LinkedHashSet<>(files));
        e.putInt("size_" + profileId, Math.min(size + 1, historySize));
        e.apply();
    }

    public void clear(String profileId) {
        int size = prefs.getInt("size_" + profileId, 0);
        SharedPreferences.Editor e = prefs.edit();

        for(int i = 0; i < size; i++)
            e.remove("h_" + profileId + "_" + i);

        e.remove("size_" + profileId);
        e.apply();
    }
}
