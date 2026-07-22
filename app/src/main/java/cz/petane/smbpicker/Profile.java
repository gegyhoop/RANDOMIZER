package cz.petane.smbpicker;

public class Profile {

    public String name;

    public String server;

    public String username;

    public String password;

    public String sourceFolder;

    public String targetFolder;

    public int filesToMove = 1;


    public Profile() {

    }


    public Profile(
            String name,
            String server,
            String username,
            String password,
            String sourceFolder,
            String targetFolder
    ) {

        this.name = name;
        this.server = server;
        this.username = username;
        this.password = password;
        this.sourceFolder = sourceFolder;
        this.targetFolder = targetFolder;
    }

}
