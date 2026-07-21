package cz.petane.smbpicker;

import java.util.UUID;

public class Profile {
    private String id;
    private String name;
    private String server;
    private String source;
    private String target;
    private int count;
    private boolean anonymous;
    private String username;
    private String password;

    public Profile() {
        this.id = UUID.randomUUID().toString();
        this.count = 1;
        this.anonymous = true;
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getName() { return name != null ? name : "Nový profil"; }
    public void setName(String name) { this.name = name; }

    public String getServer() { return server != null ? server : "192.168.0.1"; }
    public void setServer(String server) { this.server = server; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }

    public int getCount() { return count > 0 ? count : 1; }
    public void setCount(int count) { this.count = count; }

    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
