package cz.petane.smbpicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jcifs.smb.SmbFile;

public class EpisodePicker {

    private final Profile profile;

    public EpisodePicker(Profile profile) {
        this.profile = profile;
    }

    public List<String> prepareEpisodes() {
        List<String> selected = new ArrayList<>();

        try {
            SmbManager smb = new SmbManager(profile);

            smb.moveAll(
                    profile.getTarget(),
                    profile.getSource()
            );

            SmbFile[] files =
                    smb.listFolder(profile.getSource());

            if(files == null) return selected;

            ArrayList<String> available =
                    new ArrayList<>();

            for(SmbFile file : files) {
                if(file.isFile())
                    available.add(file.getName());
            }

            EpisodeHistoryManager history =
                    new EpisodeHistoryManager(
                            profile.getContext()
                    );

            Set<String> blocked =
                    history.getBlocked(
                            profile.getName(),
                            profile.getHistorySize()
                    );

            ArrayList<String> filtered =
                    new ArrayList<>();

            for(String file : available) {
                if(!blocked.contains(file))
                    filtered.add(file);
            }

            if(filtered.size() < profile.getCount()) {
                for(int h = profile.getHistorySize() - 1; h >= 0; h--) {
                    blocked = history.getBlocked(profile.getName(), h);
                    filtered.clear();

                    for(String file : available) {
                        if(!blocked.contains(file))
                            filtered.add(file);
                    }

                    if(filtered.size() >= profile.getCount())
                        break;
                }
            }

            int count = Math.min(
                    profile.getCount(),
                    filtered.size()
            );

            for(int i = 0; i < count; i++) {
                int index =
                        (int)(Math.random() * filtered.size());
                selected.add(filtered.remove(index));
            }

            for(String file : selected) {
                smb.moveFile(
                        profile.getSource(),
                        profile.getTarget(),
                        file
                );
            }

            history.add(
                    profile.getName(),
                    selected,
                    profile.getHistorySize()
            );

        } catch(Exception e) {
            e.printStackTrace();
        }

        return selected;
    }
}
