package cz.petane.randomfuturama;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jcifs.CIFSContext;
import jcifs.smb.SmbFile;

public class EpisodePicker {

    private final SettingsManager settings;
    private final SmbManager smbManager;

    public EpisodePicker(SettingsManager settings) {
        this.settings = settings;
        this.smbManager = new SmbManager(settings);
    }


    public List<String> getRandomFiles() {

        List<String> result = new ArrayList<>();

        try {

            CIFSContext context = smbManager.getContext();

           String path =
        "smb://" +
        settings.getServer() +
        "/" +
        settings.getSource() +
        "/";


            SmbFile folder =
                    new SmbFile(path, context);


            SmbFile[] files =
                    folder.listFiles();


            List<String> names = new ArrayList<>();

            for (SmbFile file : files) {

                if (!file.isDirectory()) {
                    names.add(file.getName());
                }
            }


            Collections.shuffle(
                    names,
                    new Random()
            );


            int count =
                    Math.min(
                            settings.getCount(),
                            names.size()
                    );


            for (int i = 0; i < count; i++) {
                result.add(names.get(i));
            }


        } catch (Exception e) {

            e.printStackTrace();

        }


        return result;
    }
}
