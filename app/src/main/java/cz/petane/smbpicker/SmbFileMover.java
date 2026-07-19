package cz.petane.smbpicker;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

import java.util.List;
import java.util.Properties;


public class SmbFileMover {

    private final SettingsManager settings;
    private final CIFSContext context;


    public SmbFileMover(SettingsManager settings) {

        this.settings = settings;

        try {

            Properties props = new Properties();

            props.setProperty(
                    "jcifs.smb.client.minVersion",
                    "SMB202"
            );

            props.setProperty(
                    "jcifs.smb.client.maxVersion",
                    "SMB210"
            );


            context =
                    new BaseContext(
                            new PropertyConfiguration(props)
                    );


        } catch (CIFSException e) {

            throw new RuntimeException(e);
        }
    }



    private String url(String path) {

        return "smb://"
                + settings.getServer()
                + "/"
                + path
                + "/";
    }



    private CIFSContext getContext() {

        if (settings.isAnonymous()) {
            return context;
        }


        return context.withCredentials(
                new NtlmPasswordAuthenticator(
                        "",
                        settings.getUsername(),
                        settings.getPassword()
                )
        );
    }



    public boolean move(String source, String target) {

        try {

            SmbFile from =
                    new SmbFile(
                            url(source),
                            getContext()
                    );


            SmbFile to =
                    new SmbFile(
                            url(target),
                            getContext()
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
                            url(settings.getTarget()),
                            getContext()
                    );


            SmbFile sourceFolder =
                    new SmbFile(
                            url(settings.getSource()),
                            getContext()
                    );


            SmbFile[] files =
                    targetFolder.listFiles();


            for (SmbFile file : files) {

                file.renameTo(
                        new SmbFile(
                                sourceFolder.getPath()
                                        + file.getName(),
                                getContext()
                        )
                );
            }


            return true;


        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }



    public boolean moveFiles(List<String> files) {

        try {

            for (String file : files) {


                SmbFile from =
                        new SmbFile(
                                url(settings.getSource() + "/" + file),
                                getContext()
                        );


                SmbFile to =
                        new SmbFile(
                                url(settings.getTarget() + "/" + file),
                                getContext()
                        );


                from.renameTo(to);

            }


            return true;


        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}
