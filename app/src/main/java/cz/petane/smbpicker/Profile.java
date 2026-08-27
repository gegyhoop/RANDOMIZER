package cz.petane.smbpicker;

import android.content.Context;

public class Profile {

    private String name;
    private String server;
    private String username;
    private String password;
    private String source;
    private String target;
    private boolean anonymous;
    private int count = 1;

    private boolean autoUpdate;
    private int updateHour = 20;
    private int updateMinute = 0;
    private int historySize = 3;

    private transient Context context;

    public Profile() {}

    public String getName() { return name; }
    public void setName(String v) { name = v; }

    public String getServer() { return server; }
    public void setServer(String v) { server = v; }

    public String getUsername() { return username; }
    public void setUsername(String v) { username = v; }

    public String getPassword() { return password; }
    public void setPassword(String v) { password = v; }

    public String getSource() { return source; }
    public void setSource(String v) { source = v; }

    public String getTarget() { return target; }
    public void setTarget(String v) { target = v; }

    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean v) { anonymous = v; }

    public int getCount() { return count; }
    public void setCount(int v) { count = v; }

    public boolean isAutoUpdate() { return autoUpdate; }
    public void setAutoUpdate(boolean v) { autoUpdate = v; }

    public int getUpdateHour() { return updateHour; }
    public void setUpdateHour(int v) { updateHour = v; }

    public int getUpdateMinute() { return updateMinute; }
    public void setUpdateMinute(int v) { updateMinute = v; }

    public int getHistorySize() { return historySize; }
    public void setHistorySize(int v) { historySize = v; }

    public Context getContext() { return context; }
    public void setContext(Context v) { context = v; }
}
