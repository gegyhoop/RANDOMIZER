package cz.petane.smbpicker;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ProfileManager {


    private static final String PREF = "profiles";


    private final Context context;


    public ProfileManager(Context context) {

        this.context = context;

    }



    public ArrayList<Profile> getProfiles() {


        String json =
                context
                .getSharedPreferences(PREF,0)
                .getString("data","");


        if(json.isEmpty()) {

            return new ArrayList<>();

        }


        Type type =
                new TypeToken<ArrayList<Profile>>(){}.getType();


        return new Gson().fromJson(json,type);

    }




    public void saveProfiles(ArrayList<Profile> profiles) {


        String json =
                new Gson().toJson(profiles);


        context
                .getSharedPreferences(PREF,0)
                .edit()
                .putString("data",json)
                .apply();

    }




    public Profile getProfileById(String id) {


        ArrayList<Profile> profiles = getProfiles();


        for(Profile p : profiles) {


            if(p.getName()!=null &&
                    p.getName().equals(id)) {

                return p;

            }

        }


        return new Profile();

    }





    public void updateProfile(Profile profile) {


        ArrayList<Profile> profiles = getProfiles();


        boolean updated = false;


        for(int i=0;i<profiles.size();i++) {


            if(profiles.get(i).getName()
                    .equals(profile.getName())) {


                profiles.set(i,profile);

                updated = true;

                break;

            }

        }



        if(!updated) {

            profiles.add(profile);

        }



        saveProfiles(profiles);

    }


}
