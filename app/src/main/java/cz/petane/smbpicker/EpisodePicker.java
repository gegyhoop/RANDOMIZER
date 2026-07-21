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
            SmbManager smbManager = new SmbManager(profile); // budeme upravovat
            // Zatím jednoduchá logika - vylepšíme později
            // ... (původní logika načtení souborů z source složky)

            // Placeholder
            selected.add("test_dil_1.mkv");
            selected.add("test_dil_2.mkv");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return selected;
    }
}
