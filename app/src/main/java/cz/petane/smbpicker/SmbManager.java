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



        Properties props =
                new Properties();



        props.setProperty(
                "jcifs.smb.client.enableSMB2",
                "true"
        );



        props.setProperty(
                "jcifs.smb.client.disableSMB1",
                "false"
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



            String path =
                    "smb://"
                    + profile.getServer()
                    + "/"
                    + profile.getSource()
                    + "/";




            SmbFile file =
                    new SmbFile(
                            path,
                            getContext()
                    );



            return file.exists();



        }
        catch(Exception e){


            e.printStackTrace();


            return false;


        }


    }









    public boolean moveFile(
            String filename
    ){



        try {



            String sourcePath =
                    "smb://"
                    + profile.getServer()
                    + "/"
                    + profile.getSource()
                    + "/"
                    + filename;




            String targetPath =
                    "smb://"
                    + profile.getServer()
                    + "/"
                    + profile.getTarget()
                    + "/"
                    + filename;







            SmbFile source =
                    new SmbFile(
                            sourcePath,
                            getContext()
                    );



            SmbFile target =
                    new SmbFile(
                            targetPath,
                            getContext()
                    );




            source.renameTo(target);



            return true;



        }
        catch(Exception e){



            e.printStackTrace();


            return false;



        }



    }



}
