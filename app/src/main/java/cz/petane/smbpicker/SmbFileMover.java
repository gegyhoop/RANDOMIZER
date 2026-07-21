package cz.petane.filmy;

import jcifs.CIFSContext;
import jcifs.smb.SmbFile;

import java.util.List;


public class SmbFileMover {

    private final SettingsManager settings;


    public SmbFileMover(SettingsManager settings) {

        this.settings = settings;
    }



    private String url(String path) {

        return "smb://"
                + settings.getServer()
                + "/"
                + path
                + "/";
    }



    private CIFSContext getContext() throws Exception {
        return new SmbManager(settings).getContext();
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

                if (file.isDirectory()) {
                    continue;
                }

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
