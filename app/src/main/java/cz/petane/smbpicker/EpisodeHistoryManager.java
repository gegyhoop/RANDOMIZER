package cz.petane.smbpicker;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EpisodeHistoryManager {

    private final SharedPreferences prefs;

    public EpisodeHistoryManager(Context context) {
        prefs = context.getSharedPreferences(
                "episode_history",
                Context.MODE_PRIVATE
        );
    }

    public List<Set<String>> getHistory(String profile) {
        List<Set<String>> result = new ArrayList<>();

        try {
            JSONArray history =
                    new JSONArray(prefs.getString(profile, "[]"));

            for(int i = 0; i < history.length(); i++) {
                JSONArray json = history.getJSONArray(i);
                Set<String> files = new HashSet<>();

                for(int j = 0; j < json.length(); j++)
                    files.add(json.getString(j));

                result.add(files);
            }
        } catch(Exception ignored) {}

        return result;
    }

    public Set<String> getBlocked(String profile, int count) {
        Set<String> blocked = new HashSet<>();
        List<Set<String>> history = getHistory(profile);

        for(int i = 0; i < count && i < history.size(); i++)
            blocked.addAll(history.get(i));

        return blocked;
    }

    public void add(String profile, List<String> files) {
        try {
            List<Set<String>> history = getHistory(profile);

            history.add(0, new HashSet<>(files));

            while(history.size() > 3)
                history.remove(history.size() - 1);

            JSONArray json = new JSONArray();

            for(Set<String> set : history) {
                JSONArray selection = new JSONArray();

                for(String file : set)
                    selection.put(file);

                json.put(selection);
            }

            prefs.edit()
                    .putString(profile, json.toString())
                    .apply();

        } catch(Exception ignored) {}
    }
}
