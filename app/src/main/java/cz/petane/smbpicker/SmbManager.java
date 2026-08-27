package cz.petane.smbpicker;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

public class SmbManager {

    private final CIFSContext context;

    public SmbManager(Profile profile) {
        if(profile.isAnonymous()) {
            context = SingletonContext.getInstance();
        } else {
            context = SingletonContext.getInstance()
                    .withCredentials(
                            new NtlmPasswordAuthenticator(
                                    profile.getUsername(),
                                    profile.getPassword()
                            )
                    );
        }
    }

    public SmbFile[] listFolder(String path) throws Exception {
        return new SmbFile(path, context).listFiles();
    }

    public boolean moveFile(
            String source,
            String target,
            String fileName
    ) throws Exception {

        SmbFile from = new SmbFile(
                source + "/" + fileName,
                context
        );

        SmbFile to = new SmbFile(
                target + "/" + fileName,
                context
        );

        from.renameTo(to);
        return true;
    }

    public int moveAll(
            String source,
            String target
    ) throws Exception {

        int count = 0;

        SmbFile[] files = listFolder(source);

        if(files == null) return 0;

        for(SmbFile file : files) {
            if(file.isFile()) {
                moveFile(
                        source,
                        target,
                        file.getName()
                );
                count++;
            }
        }

        return count;
    }

    public boolean testConnection() {
        try {
            return new SmbFile(
                    "smb://",
                    context
            ).exists();
        } catch(Exception e) {
            return false;
        }
    }
}
