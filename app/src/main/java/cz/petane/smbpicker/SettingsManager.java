package cz.petane.smbpicker;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private static final String PREFS = "smb_random_picker";

    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void setServer(String value) {
        prefs.edit().putString("server", value).apply();
    }

    public String getServer() {
        return prefs.getString("server", "192.168.0.1");
    }

    public void setSource(String value) {
        prefs.edit().putString("source", value).apply();
    }

    public String getSource() {
        return prefs.getString("source", "usb1_1/zzz/fut");
    }

    public void setTarget(String value) {
        prefs.edit().putString("target", value).apply();
    }

    public String getTarget() {
        return prefs.getString("target", "usb1_1/moje");
    }

    public void setCount(int value) {
        prefs.edit().putInt("count", value).apply();
    }

    public int getCount() {
        return prefs.getInt("count", 1);
    }

    public void setAnonymous(boolean value) {
        prefs.edit().putBoolean("anonymous", value).apply();
    }

    public boolean isAnonymous() {
        return prefs.getBoolean("anonymous", true);
    }

    public void setUsername(String value) {
        prefs.edit().putString("username", value).apply();
    }

    public String getUsername() {
        return prefs.getString("username", "");
    }

    public void setPassword(String value) {
        prefs.edit().putString("password", value).apply();
    }

    public String getPassword() {
        return prefs.getString("password", "");
    }
}
