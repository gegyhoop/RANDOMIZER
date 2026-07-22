package cz.petane.smbpicker;


import android.util.Log;

import jcifs.CIFSContext;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

import java.util.Properties;



public class SmbManager {


    private static final String TAG = "SmbManager";


    private final Profile profile;



    public SmbManager(Profile profile) {

        this.profile = profile;

    }







    public CIFSContext getContext() throws Exception {


        Properties props = new Properties();



        // SMB2 zapnuto
        props.setProperty(
                "jcifs.smb.client.enableSMB2",
                "true"
        );



        // SMB1 zakázáno
        props.setProperty(
                "jcifs.smb.client.disableSMB1",
                "true"
        );



        // Povolit guest přístup
        props.setProperty(
                "jcifs.smb.client.allowGuestFallback",
                "true"
        );



        // Timeouty
        props.setProperty(
                "jcifs.smb.client.soTimeout",
                "30000"
        );


        props.setProperty(
                "jcifs.smb.client.responseTimeout",
                "30000"
        );





        CIFSContext base =
                new BaseContext(
                        new PropertyConfiguration(props)
                );







        if(!profile.isAnonymous()) {


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



            SmbFile target =
                    new SmbFile(
                            getPath(profile.getTarget()),
                            getContext()
                    );




            boolean sourceExists =
                    source.exists();



            boolean targetExists =
                    target.exists();



            Log.d(
                    TAG,
                    "Source exists: "
                            + sourceExists
                            + " Target exists: "
                            + targetExists
            );



            return sourceExists
                    &&
                    targetExists;



        }
        catch(Exception e){


            Log.e(
                    TAG,
                    "SMB connection failed",
                    e
            );


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





            boolean sourceGone =
                    !source.exists();



            boolean targetExists =
                    target.exists();





            return sourceGone
                    &&
                    targetExists;



        }
        catch(Exception e){



            Log.e(
                    TAG,
                    "Move failed",
                    e
            );


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



            Log.e(
                    TAG,
                    "Move all failed",
                    e
            );


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
