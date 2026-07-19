package cz.petane.smbpicker;

import jcifs.CIFSContext;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

import java.util.Properties;

public class SmbManager {

    private final SettingsManager settings;

    public SmbManager(SettingsManager settings) {
        this.settings = settings;
    }
public CIFSContext getContext() throws Exception {

    Properties props = new Properties();

    props.setProperty(
            "jcifs.smb.client.enableSMB2",
            "true"
    );

    props.setProperty(
            "jcifs.smb.client.disableSMB1",
            "false"
    );


    CIFSContext base =
            new BaseContext(
                    new PropertyConfiguration(props)
            );


    if (!settings.isAnonymous()) {

        base =
                base.withCredentials(
                        new NtlmPasswordAuthenticator(
                                "",
                                settings.getUsername(),
                                settings.getPassword()
                        )
                );
    }


    return base;
}

    public boolean testConnection() {

        try {

            Properties props = new Properties();

            props.setProperty(
                    "jcifs.smb.client.enableSMB2",
                    "true"
            );

            props.setProperty(
                    "jcifs.smb.client.disableSMB1",
                    "false"
            );


            CIFSContext base =
                    new BaseContext(
                            new PropertyConfiguration(props)
                    );


            if (!settings.isAnonymous()) {

                base = base.withCredentials(
                        new NtlmPasswordAuthenticator(
                                "",
                                settings.getUsername(),
                                settings.getPassword()
                        )
                );
            }


    String path =
        "smb://" +
        settings.getServer() +
        "/" +
        settings.getSource() +
        "/";


            SmbFile file = new SmbFile(path, base);


            return file.exists();


        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}
