package cz.petane.smbpicker;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {



    private LinearLayout layout;


    private ProfileManager profileManager;


    private ArrayList<Profile> profiles;






    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        profileManager =
                new ProfileManager(this);



        createLayout();



    }







    @Override
    protected void onResume() {


        super.onResume();


        loadProfiles();


    }








    private void createLayout() {



        layout =
                new LinearLayout(this);



        layout.setOrientation(
                LinearLayout.VERTICAL
        );



        layout.setPadding(
                0,
                150,
                0,
                20
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
                v -> openAddProfile()
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



        // smažeme staré karty

        while(layout.getChildCount() > 2) {


            layout.removeViewAt(2);


        }





        for(Profile profile : profiles) {



            ProfileCard card =
                    new ProfileCard(
                            this,
                            profile,
                            this
                    );



            layout.addView(card);



        }


    }










    private void openAddProfile() {



        Intent intent =
                new Intent(
                        this,
                        AddProfileActivity.class
                );



        startActivity(intent);



    }









    public void openSettings(Profile profile) {



        Intent intent =
                new Intent(
                        this,
                        AddProfileActivity.class
                );



        intent.putExtra(
                "profileName",
                profile.getName()
        );



        startActivity(intent);



    }









    public void deleteProfile(Profile profile) {



        profileManager.deleteProfile(profile);



        Toast.makeText(
                this,
                "Profil smazán",
                Toast.LENGTH_SHORT
        ).show();



        loadProfiles();



    }






}
