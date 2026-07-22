package cz.petane.smbpicker;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ProfileManager {


    private static final String FILE = "profiles.json";


    public static ArrayList<Profile> load(Context context) {


        try {

            String json =
                    context
                    .getSharedPreferences("data",0)
                    .getString(FILE,"");


            if(json.isEmpty()) {

                return new ArrayList<>();

            }


            Type type =
                    new TypeToken<ArrayList<Profile>>(){}.getType();


            return new Gson().fromJson(json,type);


        } catch(Exception e) {

            return new ArrayList<>();

        }

    }



    public static void save(
            Context context,
            ArrayList<Profile> profiles
    ) {


        String json =
                new Gson().toJson(profiles);


        context
                .getSharedPreferences("data",0)
                .edit()
                .putString(FILE,json)
                .apply();

    }

}
