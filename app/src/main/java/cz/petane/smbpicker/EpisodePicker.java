package cz.petane.smbpicker;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jcifs.smb.SmbFile;

public class EpisodePicker {

    private final Profile profile;
    private final Context context;

    public EpisodePicker(Profile profile, Context context) {
        this.profile = profile;
        this.context = context;
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

            if(files == null)
                return selected;

            ArrayList<String> available =
                    new ArrayList<>();

            for(SmbFile file : files) {
                if(file.isFile())
                    available.add(file.getName());
            }

            int count = Math.min(
                    profile.getCount(),
                    available.size()
            );

            EpisodeHistoryManager history =
                    new EpisodeHistoryManager(context);

            ArrayList<String> pool =
                    new ArrayList<>();

            for(int h = 3; h >= 0; h--) {

                Set<String> blocked =
                        history.getBlocked(
                                profile.getName(),
                                h
                        );

                pool.clear();

                for(String file : available) {
                    if(!blocked.contains(file))
                        pool.add(file);
                }

                if(pool.size() >= count)
                    break;
            }

            for(int i = 0;
                    i < count && !pool.isEmpty();
                    i++) {

                int index =
                        (int)(Math.random() * pool.size());

                selected.add(
                        pool.remove(index)
                );
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
                    selected
            );

        } catch(Exception e) {
            e.printStackTrace();
        }

        return selected;
    }
}
