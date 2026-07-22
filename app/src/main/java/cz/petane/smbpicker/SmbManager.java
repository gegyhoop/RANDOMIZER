package cz.petane.smbpicker;


import jcifs.CIFSContext;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

import java.util.Properties;



public class SmbManager {



    private final Profile profile;



    public SmbManager(Profile profile) {

        this.profile = profile;

    }







    public CIFSContext getContext() throws Exception {



        Properties props = new Properties();



        // D-Link router podporuje SMB 2.0 - 2.1
        props.setProperty(
                "jcifs.smb.client.minVersion",
                "SMB202"
        );


        props.setProperty(
                "jcifs.smb.client.maxVersion",
                "SMB210"
        );




        CIFSContext base =
                new BaseContext(
                        new PropertyConfiguration(props)
                );





        // Pokud není anonymní přístup,
        // použij přihlašovací údaje
        if (!profile.isAnonymous()) {


            base =
                    base.withCredentials(
                            new NtlmPasswordAuthenticator(
                                    "",
                                    profile.getUsername(),
                                    profile.getPassword()
                            )
                    );


        }



        return base;


    }









    public boolean testConnection() {



        try {



            SmbFile source =
                    new SmbFile(
                            getPath(profile.getSource()),
                            getContext()
                    );



            return source.exists();



        }
        catch(Exception e){



            e.printStackTrace();


            return false;


        }


    }









    public SmbFile[] listFolder(String folder)
            throws Exception {



        SmbFile dir =
                new SmbFile(
                        getPath(folder),
                        getContext()
                );



        return dir.listFiles();



    }









    public boolean moveFile(
            String fromFolder,
            String toFolder,
            String filename
    ){



        try {



            SmbFile source =
                    new SmbFile(
                            getPath(fromFolder)
                                    + filename,
                            getContext()
                    );



            SmbFile target =
                    new SmbFile(
                            getPath(toFolder)
                                    + filename,
                            getContext()
                    );





            source.renameTo(target);




            return !source.exists()
                    &&
                    target.exists();



        }
        catch(Exception e){



            e.printStackTrace();


            return false;


        }


    }









    public int moveAll(
            String fromFolder,
            String toFolder
    ){



        int moved = 0;



        try {



            SmbFile[] files =
                    listFolder(fromFolder);





            if(files == null){

                return 0;

            }






            for(SmbFile file : files){



                if(!file.isFile()){

                    continue;

                }





                if(moveFile(
                        fromFolder,
                        toFolder,
                        file.getName()
                )){


                    moved++;


                }



            }



        }
        catch(Exception e){


            e.printStackTrace();


        }



        return moved;



    }









    private String getPath(String folder){



        if(folder == null){

            folder = "";

        }



        while(folder.startsWith("/")){


            folder =
                    folder.substring(1);


        }





        if(folder.endsWith("/")){


            return "smb://"
                    + profile.getServer()
                    + "/"
                    + folder;


        }





        return "smb://"
                + profile.getServer()
                + "/"
                + folder
                + "/";


    }



}
