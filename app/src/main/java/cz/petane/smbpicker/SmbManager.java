package cz.petane.smbpicker;


import jcifs.CIFSContext;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;

import java.util.Properties;



public class SmbManager {


    private final Profile profile;

    private final CIFSContext context;



    public SmbManager(Profile profile){


        this.profile = profile;


        try {


            Properties props =
                    new Properties();



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



            if(!profile.isAnonymous()){


                base =
                        base.withCredentials(
                                new NtlmPasswordAuthenticator(
                                        "",
                                        profile.getUsername(),
                                        profile.getPassword()
                                )
                        );


            }



            context = base;



        }
        catch(Exception e){


            throw new RuntimeException(e);


        }


    }






    public boolean testConnection(){


        try{


            SmbFile file =
                    new SmbFile(
                            getPath(profile.getSource()),
                            context
                    );


            return file.exists();


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
                        context
                );


        return dir.listFiles();


    }








    public boolean moveFile(
            String fromFolder,
            String toFolder,
            String filename
    ){


        try{


            SmbFile from =
                    new SmbFile(
                            getPath(fromFolder)
                                    + filename,
                            context
                    );



            SmbFile to =
                    new SmbFile(
                            getPath(toFolder)
                                    + filename,
                            context
                    );



            from.renameTo(to);



            return true;


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


        try{


            SmbFile folder =
                    new SmbFile(
                            getPath(fromFolder),
                            context
                    );



            SmbFile[] files =
                    folder.listFiles();




            if(files == null)
                return 0;




            for(SmbFile file : files){


                if(!file.isFile())
                    continue;



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


        while(folder.startsWith("/")){


            folder =
                    folder.substring(1);


        }



        return "smb://"
                + profile.getServer()
                + "/"
                + folder
                + "/";


    }


}
