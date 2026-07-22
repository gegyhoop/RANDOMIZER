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

        profileManager = new ProfileManager(this);

        createLayout();
        loadExistingProfile();
    }


    private EditText addField(LinearLayout layout, String text) {

        TextView label = new TextView(this);
        label.setText(text);
        layout.addView(label);

        EditText field = new EditText(this);
        field.setHint(text);
        layout.addView(field);

        return field;
    }


    private void createLayout() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20,100,20,20);


        TextView title = new TextView(this);
        title.setText("Nastavení profilu");
        title.setTextSize(26);
        layout.addView(title);


        name = addField(layout,"Název");

        server = addField(layout,"SMB server");


        anonymous = new CheckBox(this);
        anonymous.setText("Anonymní přihlášení");
        layout.addView(anonymous);


        username = addField(layout,"Uživatel");

        password = addField(layout,"Heslo");
        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );


        anonymous.setOnCheckedChangeListener((buttonView, checked) -> {

            username.setEnabled(!checked);
            password.setEnabled(!checked);

            if(checked){
                username.setText("");
                password.setText("");
            }

        });


        source = addField(layout,"Zdrojová složka");

        target = addField(layout,"Cílová složka");

        count = addField(layout,"Počet dílů");
        count.setText("1");


        Button test = new Button(this);
        test.setText("Test SMB připojení");
        test.setOnClickListener(v -> testConnection());
        layout.addView(test);


        Button save = new Button(this);
        save.setText("Uložit");
        save.setOnClickListener(v -> saveProfile());
        layout.addView(save);


        setContentView(layout);
    }


    private Profile createProfileFromFields(){

        Profile p = new Profile();

        p.setName(name.getText().toString());
        p.setServer(server.getText().toString());
        p.setUsername(username.getText().toString());
        p.setPassword(password.getText().toString());
        p.setAnonymous(anonymous.isChecked());
        p.setSource(source.getText().toString());
        p.setTarget(target.getText().toString());

        try {

            p.setCount(
                    Integer.parseInt(
                            count.getText().toString()
                    )
            );

        }
        catch(Exception e){

            p.setCount(1);

        }

        return p;
    }


    private void testConnection(){

        Toast.makeText(
                this,
                "Testuji připojení...",
                Toast.LENGTH_SHORT
        ).show();


        new Thread(() -> {


            boolean result = false;


            try {


                Profile testProfile =
                        createProfileFromFields();


                SmbManager smb =
                        new SmbManager(testProfile);


                result =
                        smb.testConnection();


            }
            catch(Exception e){

                e.printStackTrace();

            }


            boolean finalResult = result;


            runOnUiThread(() -> {


                if(finalResult){

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


            });


        }).start();

    }


    private void loadExistingProfile(){

        String profileName =
                getIntent()
                        .getStringExtra("profileName");


        if(profileName == null)
            return;


        editingProfile =
                profileManager.getProfileById(profileName);


        if(editingProfile == null)
            return;


        name.setText(editingProfile.getName());
        server.setText(editingProfile.getServer());

        username.setText(editingProfile.getUsername());
        password.setText(editingProfile.getPassword());

        anonymous.setChecked(
                editingProfile.isAnonymous()
        );

        source.setText(editingProfile.getSource());
        target.setText(editingProfile.getTarget());

        count.setText(
                String.valueOf(
                        editingProfile.getCount()
                )
        );

    }


    private void saveProfile(){

        if(editingProfile == null){

            editingProfile = new Profile();

        }


        Profile p =
                createProfileFromFields();


        editingProfile.setName(p.getName());
        editingProfile.setServer(p.getServer());
        editingProfile.setUsername(p.getUsername());
        editingProfile.setPassword(p.getPassword());
        editingProfile.setAnonymous(p.isAnonymous());
        editingProfile.setSource(p.getSource());
        editingProfile.setTarget(p.getTarget());
        editingProfile.setCount(p.getCount());


        profileManager.updateProfile(
                editingProfile
        );


        finish();

    }

}
