package cz.petane.smbpicker;

import jcifs.smb.SmbFile;
import java.util.List;

public class SmbFileMover {

    private final Profile profile;

    public SmbFileMover(Profile profile) {
        this.profile = profile;
    }

    public boolean moveAllBack() {
        // TODO: Implementace vrácení souborů
        return true; // placeholder
    }

    public boolean moveFiles(List<String> files) {
        try {
            SmbManager smb = new SmbManager(profile);
            // TODO: Plná implementace přesouvání
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
