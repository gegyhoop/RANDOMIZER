package cz.petane.smbpicker;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;



public class ProfileManager {


    private static final String PREF = "profiles";


    private static final String KEY = "data";


    private final Context context;




    public ProfileManager(Context context) {

        this.context = context;

    }






    public ArrayList<Profile> getProfiles() {


        try {


            String json =
                    context
                            .getSharedPreferences(PREF, 0)
                            .getString(KEY, "");



            if(json.isEmpty()) {

                return new ArrayList<>();

            }




            Type type =
                    new TypeToken<ArrayList<Profile>>(){}.getType();



            ArrayList<Profile> profiles =
                    new Gson().fromJson(json, type);



            if(profiles == null) {

                return new ArrayList<>();

            }


            return profiles;



        } catch(Exception e) {


            return new ArrayList<>();

        }


    }








    public void saveProfiles(
            ArrayList<Profile> profiles
    ) {



        String json =
                new Gson().toJson(profiles);



        context
                .getSharedPreferences(PREF, 0)
                .edit()
                .putString(KEY, json)
                .apply();


    }









    public Profile getProfileById(
            String id
    ) {



        ArrayList<Profile> profiles =
                getProfiles();



        for(Profile profile : profiles) {



            if(profile.getName() != null &&
                    profile.getName().equals(id)) {



                return profile;


            }


        }



        return new Profile();


    }









    public void updateProfile(
            Profile profile
    ) {



        ArrayList<Profile> profiles =
                getProfiles();



        boolean updated = false;




        for(int i = 0; i < profiles.size(); i++) {



            Profile old =
                    profiles.get(i);




            if(old.getName() != null &&
                    old.getName().equals(profile.getName())) {



                profiles.set(i, profile);


                updated = true;


                break;


            }


        }





        if(!updated) {


            profiles.add(profile);


        }




        saveProfiles(profiles);



    }







    public void deleteProfile(
            Profile profile
    ) {



        ArrayList<Profile> profiles =
                getProfiles();



        profiles.remove(profile);



        saveProfiles(profiles);


    }


}
