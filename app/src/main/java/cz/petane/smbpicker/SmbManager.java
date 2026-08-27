package cz.petane.smbpicker;

import java.util.ArrayList;
import java.util.List;

import jcifs.CIFSContext;
import jcifs.config.PropertyConfiguration;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

public class SmbManager {

    private final Profile profile;
    private final CIFSContext context;

    public SmbManager(Profile profile) {
        this.profile = profile;

        try {
            PropertyConfiguration config =
                    new PropertyConfiguration(
                            SingletonContext.getInstance().getConfig().getProperties()
                    );

            CIFSContext base =
                    SingletonContext.getInstance().withConfig(config);

            if(profile.isAnonymous()) {
                context = base.withCredentials(
                        new NtlmPasswordAuthenticator(null, null, null)
                );
            } else {
                context = base.withCredentials(
                        new NtlmPasswordAuthenticator(
                                profile.getUsername(),
                                profile.getPassword()
                        )
                );
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SmbFile folder(String path) throws Exception {
        String server = profile.getServer();

        if(!server.endsWith("/"))
            server += "/";

        if(path.startsWith("/"))
            path = path.substring(1);

        return new SmbFile(
                "smb://" + server + path,
                context
        );
    }

    public boolean testConnection() {
        try {
            SmbFile source = folder(profile.getSource());
            SmbFile target = folder(profile.getTarget());

            return source.exists() &&
                    source.isDirectory() &&
                    target.exists() &&
                    target.isDirectory();

        } catch(Exception e) {
            return false;
        }
    }

    public SmbFile[] listFolder(String path) {
        try {
            return folder(path).listFiles();
        } catch(Exception e) {
            return null;
        }
    }

    public boolean moveFile(
            String fromPath,
            String toPath,
            String fileName
    ) {
        try {
            SmbFile from =
                    new SmbFile(
                            folder(fromPath),
                            fileName
                    );

            SmbFile to =
                    new SmbFile(
                            folder(toPath),
                            fileName
                    );

            if(!from.exists() || !from.isFile())
                return false;

            from.renameTo(to);

            return !from.exists() && to.exists();

        } catch(Exception e) {
            return false;
        }
    }

    public int moveAll(
            String fromPath,
            String toPath
    ) {
        int moved = 0;

        try {
            SmbFile[] files =
                    listFolder(fromPath);

            if(files == null)
                return 0;

            for(SmbFile file : files) {
                if(file.isFile() &&
                        moveFile(
                                fromPath,
                                toPath,
                                file.getName()
                        )) {
                    moved++;
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return moved;
    }
}
