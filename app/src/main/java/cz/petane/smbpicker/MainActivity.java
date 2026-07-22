package cz.petane.smbpicker;


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





    private void createLayout() {


        layout = new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );


        // odsazení od horní hrany displeje
        layout.setPadding(
                0,
                150,
                0,
                0
        );



        TextView title = new TextView(this);

        title.setText(
                "SMB Random Picker"
        );

        title.setTextSize(26);


        layout.addView(title);




        Button add = new Button(this);

        add.setText(
                "+ Přidat profil"
        );


        add.setOnClickListener(
                v -> addTestProfile()
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





    private void addTestProfile() {


        Profile p =
                new Profile();


        p.setName(
                "Test seriál"
        );


        p.setServer(
                "192.168.1.1"
        );


        profiles.add(p);


        profileManager.updateProfile(p);


        recreate();

    }

}
