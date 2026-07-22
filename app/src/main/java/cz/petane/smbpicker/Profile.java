package cz.petane.smbpicker;


public class Profile {


    private String name;

    private String server;

    private String username;

    private String password;

    private String source;

    private String target;

    private boolean anonymous;

    private int count = 1;



    public Profile() {

    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }



    public String getServer() {
        return server;
    }


    public void setServer(String server) {
        this.server = server;
    }



    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }



    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }



    public String getSource() {
        return source;
    }


    public void setSource(String source) {
        this.source = source;
    }



    public String getTarget() {
        return target;
    }


    public void setTarget(String target) {
        this.target = target;
    }



    public boolean isAnonymous() {
        return anonymous;
    }


    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }



    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }

}
