package cz.petane.smbpicker;


import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



public class AddProfileActivity extends AppCompatActivity {



    private EditText name;
    private EditText server;
    private EditText username;
    private EditText password;
    private EditText source;
    private EditText target;
    private EditText count;

    private CheckBox anonymous;



    private ProfileManager profileManager;

    private Profile editingProfile;









    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        profileManager =
                new ProfileManager(this);



        createLayout();



        loadExistingProfile();



    }









    private EditText addField(
            LinearLayout layout,
            String text
    ){


        TextView label =
                new TextView(this);


        label.setText(text);


        layout.addView(label);




        EditText field =
                new EditText(this);


        field.setHint(text);


        layout.addView(field);


        return field;


    }









    private void createLayout(){


        LinearLayout layout =
                new LinearLayout(this);


        layout.setOrientation(
                LinearLayout.VERTICAL
        );


        layout.setPadding(
                20,
                100,
                20,
                20
        );




        TextView title =
                new TextView(this);


        title.setText(
                "Nastavení profilu"
        );


        title.setTextSize(26);


        layout.addView(title);





        name =
                addField(
                        layout,
                        "Název"
                );



        server =
                addField(
                        layout,
                        "SMB server"
                );



        anonymous =
                new CheckBox(this);


        anonymous.setText(
                "Anonymní přihlášení"
        );


        anonymous.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {


                    username.setEnabled(!isChecked);

                    password.setEnabled(!isChecked);


                    if(isChecked){

                        username.setText("");

                        password.setText("");

                    }


                }
        );


        layout.addView(anonymous);





        username =
                addField(
                        layout,
                        "Uživatel"
                );



        password =
                addField(
                        layout,
                        "Heslo"
                );


        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );



        source =
                addField(
                        layout,
                        "Zdrojová složka"
                );



        target =
                addField(
                        layout,
                        "Cílová složka"
                );



        count =
                addField(
                        layout,
                        "Počet souborů"
                );


        count.setText("1");





        Button test =
                new Button(this);


        test.setText(
                "Test SMB připojení"
        );


        test.setOnClickListener(
                v -> testConnection()
        );


        layout.addView(test);





        Button save =
                new Button(this);


        save.setText(
                "Uložit"
        );


        save.setOnClickListener(
                v -> saveProfile()
        );


        layout.addView(save);



        setContentView(layout);



    }
    private void loadExistingProfile(){


        String profileName =
                getIntent()
                        .getStringExtra("profileName");



        if(profileName == null){

            return;

        }





        editingProfile =
                profileManager.getProfileById(profileName);





        if(editingProfile != null &&
                editingProfile.getName() != null){



            name.setText(
                    editingProfile.getName()
            );



            server.setText(
                    editingProfile.getServer()
            );



            username.setText(
                    editingProfile.getUsername()
            );



            password.setText(
                    editingProfile.getPassword()
            );



            anonymous.setChecked(
                    editingProfile.isAnonymous()
            );



            source.setText(
                    editingProfile.getSource()
            );



            target.setText(
                    editingProfile.getTarget()
            );



            count.setText(
                    String.valueOf(
                            editingProfile.getCount()
                    )
            );


        }



    }









    private Profile createProfileFromFields(){


        Profile profile =
                new Profile();



        profile.setName(
                name.getText().toString()
        );



        profile.setServer(
                server.getText().toString()
        );



        profile.setUsername(
                username.getText().toString()
        );



        profile.setPassword(
                password.getText().toString()
        );



        profile.setAnonymous(
                anonymous.isChecked()
        );



        profile.setSource(
                source.getText().toString()
        );



        profile.setTarget(
                target.getText().toString()
        );



        try {


            profile.setCount(
                    Integer.parseInt(
                            count.getText().toString()
                    )
            );


        }
        catch(Exception e){


            profile.setCount(1);


        }



        return profile;


    }









    private void testConnection(){



        Profile testProfile =
                createProfileFromFields();




        SmbManager smb =
                new SmbManager(testProfile);




        boolean result =
                smb.testConnection();





        if(result){



            Toast.makeText(
                    this,
                    "Připojení OK",
                    Toast.LENGTH_LONG
            ).show();



        }
        else {



            Toast.makeText(
                    this,
                    "Připojení selhalo - zkontroluj Logcat",
                    Toast.LENGTH_LONG
            ).show();



        }



    }









    private void saveProfile(){



        if(editingProfile == null){



            editingProfile =
                    new Profile();



        }





        Profile newProfile =
                createProfileFromFields();




        editingProfile.setName(
                newProfile.getName()
        );


        editingProfile.setServer(
                newProfile.getServer()
        );


        editingProfile.setUsername(
                newProfile.getUsername()
        );


        editingProfile.setPassword(
                newProfile.getPassword()
        );


        editingProfile.setAnonymous(
                newProfile.isAnonymous()
        );


        editingProfile.setSource(
                newProfile.getSource()
        );


        editingProfile.setTarget(
                newProfile.getTarget()
        );


        editingProfile.setCount(
                newProfile.getCount()
        );






        profileManager.updateProfile(
                editingProfile
        );



        finish();



    }


}
