package cz.petane.smbpicker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



public class AddProfileActivity extends AppCompatActivity {



    private EditText name;

    private EditText server;

    private EditText source;

    private EditText target;

    private EditText count;



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



        if(profileName == null) {


            return;


        }






        editingProfile =
                profileManager.getProfileById(profileName);






        if(editingProfile.getName() != null){



            name.setText(
                    editingProfile.getName()
            );



            server.setText(
                    editingProfile.getServer()
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









    private void testConnection(){



    Profile testProfile =
            new Profile();




    testProfile.setName(
            name.getText().toString()
    );



    testProfile.setServer(
            server.getText().toString()
    );



    testProfile.setSource(
            source.getText().toString()
    );



    testProfile.setTarget(
            target.getText().toString()
    );





    SmbManager smb =
            new SmbManager(testProfile);





    try {



        boolean sourceOK =
                new jcifs.smb.SmbFile(
                        "smb://"
                        + testProfile.getServer()
                        + "/"
                        + testProfile.getSource().replaceFirst("^/+", "")
                        + "/",
                        smb.getContext()
                ).exists();





        boolean targetOK =
                new jcifs.smb.SmbFile(
                        "smb://"
                        + testProfile.getServer()
                        + "/"
                        + testProfile.getTarget().replaceFirst("^/+", "")
                        + "/",
                        smb.getContext()
                ).exists();







        if(sourceOK && targetOK){



            Toast.makeText(
                    this,
                    "Zdroj i cíl OK",
                    Toast.LENGTH_LONG
            ).show();



        }
        else if(!sourceOK){



            Toast.makeText(
                    this,
                    "Zdrojová složka nenalezena",
                    Toast.LENGTH_LONG
            ).show();



        }
        else {



            Toast.makeText(
                    this,
                    "Cílová složka nenalezena",
                    Toast.LENGTH_LONG
            ).show();



        }



    }
    catch(Exception e){



        Toast.makeText(
                this,
                "Chyba připojení",
                Toast.LENGTH_LONG
        ).show();



        e.printStackTrace();



    }



}



        Profile testProfile =
                new Profile();



        testProfile.setName(
                name.getText().toString()
        );



        testProfile.setServer(
                server.getText().toString()
        );



        testProfile.setSource(
                source.getText().toString()
        );



        testProfile.setTarget(
                target.getText().toString()
        );



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
                    "Připojení selhalo",
                    Toast.LENGTH_LONG
            ).show();


        }



    }









    private void saveProfile(){



        if(editingProfile == null){



            editingProfile =
                    new Profile();



        }







        editingProfile.setName(
                name.getText().toString()
        );



        editingProfile.setServer(
                server.getText().toString()
        );



        editingProfile.setSource(
                source.getText().toString()
        );



        editingProfile.setTarget(
                target.getText().toString()
        );






        try {



            editingProfile.setCount(
                    Integer.parseInt(
                            count.getText().toString()
                    )
            );



        }
        catch(Exception e){



            editingProfile.setCount(1);



        }






        profileManager.updateProfile(
                editingProfile
        );



        finish();



    }



}
