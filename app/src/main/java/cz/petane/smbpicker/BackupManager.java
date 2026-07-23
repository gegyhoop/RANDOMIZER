package cz.petane.smbpicker;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class BackupManager {

    private final Context context;

    public BackupManager(Context context) {
        this.context = context;
    }


    public File exportProfiles() throws Exception {

        ProfileManager manager =
                new ProfileManager(context);

        ArrayList<Profile> profiles =
                manager.getProfiles();


        String json =
                new Gson().toJson(profiles);


        File file =
                new File(
                        context.getExternalFilesDir(null),
                        "SMB_Random_Picker_backup.json"
                );


        FileOutputStream output =
                new FileOutputStream(file);


        output.write(
                json.getBytes(StandardCharsets.UTF_8)
        );


        output.close();


        return file;

    }



    public void importProfiles(File file) throws Exception {


        FileInputStream input =
                new FileInputStream(file);


        byte[] data =
                new byte[(int)file.length()];


        input.read(data);

        input.close();


        String json =
                new String(
                        data,
                        StandardCharsets.UTF_8
                );


        Type type =
                new TypeToken<ArrayList<Profile>>(){}.getType();


        ArrayList<Profile> profiles =
                new Gson().fromJson(json,type);



        if(profiles == null){

            profiles =
                    new ArrayList<>();

        }


        ProfileManager manager =
                new ProfileManager(context);


        manager.saveProfiles(profiles);

    }

}
