package cz.petane.smbpicker;


import java.util.ArrayList;
import java.util.List;


import jcifs.smb.SmbFile;



public class EpisodePicker {



    private final Profile profile;



    public EpisodePicker(Profile profile){


        this.profile = profile;


    }








    public List<String> prepareEpisodes(){



        List<String> selected =
                new ArrayList<>();



        try {



            SmbManager smb =
                    new SmbManager(profile);





            // 1) vrátit staré díly zpět

            smb.moveAll(
                    profile.getTarget(),
                    profile.getSource()
            );







            // 2) načíst zdroj



            SmbFile[] files =
                    smb.listFolder(
                            profile.getSource()
                    );





            if(files == null){

                return selected;

            }






            int count =
                    Math.min(
                            profile.getCount(),
                            files.length
                    );







            ArrayList<String> available =
                    new ArrayList<>();




            for(SmbFile file : files){



                if(file.isFile()){


                    available.add(
                            file.getName()
                    );


                }


            }







            // 3) náhodný výběr



            for(int i = 0; i < count && !available.isEmpty(); i++){



                int index =
                        (int)(
                                Math.random()
                                *
                                available.size()
                        );



                selected.add(
                        available.remove(index)
                );



            }







            // 4) přesun vybraných do cíle



            for(String file : selected){



                smb.moveFile(
                        profile.getSource(),
                        profile.getTarget(),
                        file
                );


            }




        }
        catch(Exception e){



            e.printStackTrace();


        }





        return selected;



    }



}
