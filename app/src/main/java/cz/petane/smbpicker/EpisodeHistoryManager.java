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
                JSONArray selection = history.getJSONArray(i);
                Set<String> files = new HashSet<>();

                for(int j = 0; j < selection.length(); j++)
                    files.add(selection.getString(j));

                result.add(files);
            }
        } catch(Exception ignored) {
        }

        return result;
    }

    public Set<String> getBlocked(String profile, int selections) {
        Set<String> blocked = new HashSet<>();
        List<Set<String>> history = getHistory(profile);

        for(int i = 0; i < selections && i < history.size(); i++)
            blocked.addAll(history.get(i));

        return blocked;
    }

    public void add(String profile, List<String> files) {
        try {
            List<Set<String>> history = getHistory(profile);

            Set<String> selection = new HashSet<>(files);
            history.add(0, selection);

            while(history.size() > 3)
                history.remove(history.size() - 1);

            JSONArray result = new JSONArray();

            for(Set<String> set : history) {
                JSONArray selectionJson = new JSONArray();

                for(String file : set)
                    selectionJson.put(file);

                result.put(selectionJson);
            }

            prefs.edit()
                    .putString(profile, result.toString())
                    .apply();

        } catch(Exception ignored) {
        }
    }
}
