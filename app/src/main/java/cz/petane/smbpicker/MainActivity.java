package cz.petane.smbpicker;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {


    private LinearLayout layout;

    private ProfileManager profileManager;

    private ArrayList<Profile> profiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        profileManager = new ProfileManager(this);


        createLayout();


        loadProfiles();

    }





    @Override
    protected void onResume() {

        super.onResume();

        if(profileManager != null && layout != null) {

            loadProfiles();

        }

    }






    private void createLayout() {


        layout = new LinearLayout(this);


        layout.setOrientation(
                LinearLayout.VERTICAL
        );


        layout.setPadding(
                0,
                150,
                0,
                0
        );



        TextView title =
                new TextView(this);


        title.setText(
                "SMB Random Picker"
        );


        title.setTextSize(26);



        layout.addView(title);




        Button add =
                new Button(this);


        add.setText(
                "+ Přidat profil"
        );



        add.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    AddProfileActivity.class
                            );


                    startActivity(intent);

                }
        );



        layout.addView(add);



        setContentView(layout);

    }






    private void loadProfiles() {


        profiles =
                profileManager.getProfiles();


        showProfiles();

    }







    private void showProfiles() {


        // odstraní starý seznam,
        // aby se profily neduplikovaly po návratu


        while(layout.getChildCount() > 2) {

            layout.removeViewAt(2);

        }




        for(Profile profile : profiles) {


            TextView item =
                    new TextView(this);



            item.setText(
                    "📁 "
                    + profile.getName()
            );



            item.setTextSize(20);



            layout.addView(item);

        }


    }



}
