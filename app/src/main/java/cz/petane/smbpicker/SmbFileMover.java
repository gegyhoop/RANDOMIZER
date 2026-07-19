package cz.petane.smbpicker;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class SmbFileMover {

    private final SettingsManager settings;

    public SmbFileMover(SettingsManager settings) {
        this.settings = settings;
    }


    private String getBaseUrl() {

        String server = settings.getServer();

        if (settings.isAnonymous()) {
            return "smb://" + server + "/";
        } else {

            return "smb://"
                    + settings.getUsername()
                    + ":"
                    + settings.getPassword()
                    + "@"
                    + server
                    + "/";
        }
    }



    private NtlmPasswordAuthentication getAuth() {

        if (settings.isAnonymous()) {
            return null;
        }

        return new NtlmPasswordAuthentication(
                "",
                settings.getUsername(),
                settings.getPassword()
        );
    }



    public boolean move(String source, String target) {

        try {

            SmbFile from =
                    new SmbFile(
                            getBaseUrl() + source,
                            getAuth()
                    );


            SmbFile to =
                    new SmbFile(
                            getBaseUrl() + target,
                            getAuth()
                    );


            from.renameTo(to);

            return true;


        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }



    public boolean moveAllBack() {

        try {

            SmbFile targetFolder =
                    new SmbFile(
                            getBaseUrl()
                                    + settings.getTarget()
                                    + "/",
                            getAuth()
                    );


            SmbFile sourceFolder =
                    new SmbFile(
                            getBaseUrl()
                                    + settings.getSource()
                                    + "/",
                            getAuth()
                    );


            SmbFile[] files =
                    targetFolder.listFiles();


            for (SmbFile file : files) {

                file.renameTo(
                        new SmbFile(
                                sourceFolder.getPath()
                                        + file.getName(),
                                getAuth()
                        )
                );
            }


            return true;


        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}
