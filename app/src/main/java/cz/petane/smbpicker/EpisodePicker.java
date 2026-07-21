package cz.petane.smbpicker;

import java.util.ArrayList;
import java.util.List;
import jcifs.smb.SmbFile;

public class EpisodePicker {

    private final Profile profile;

    public EpisodePicker(Profile profile) {
        this.profile = profile;
    }

    public List<String> getRandomFiles() {
        List<String> selected = new ArrayList<>();

        try {
            SmbManager smbManager = new SmbManager(profile);
            String path = "smb://" + profile.getServer() + "/" + profile.getSource() + "/";

            SmbFile dir = new SmbFile(path, smbManager.getContext());
            SmbFile[] files = dir.listFiles();

            if (files == null || files.length == 0) {
                return selected;
            }

            // Jednoduchý random výběr (pro test)
            int count = Math.min(profile.getCount(), files.length);
            // TODO: lepší random + filtrace (jen videa atd.)

            for (int i = 0; i < count; i++) {
                int randomIndex = (int) (Math.random() * files.length);
                selected.add(files[randomIndex].getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return selected;
    }
}
