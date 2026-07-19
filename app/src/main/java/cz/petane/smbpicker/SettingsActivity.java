package cz.petane.randomfuturama;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settings;

    private LinearLayout layout;

    private EditText server;
    private EditText source;
    private EditText target;
    private EditText count;

    private EditText username;
    private EditText password;

    private CheckBox anonymous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = new SettingsManager(this);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);


        TextView title = new TextView(this);
        title.setText("Nastavení SMB");
        title.setTextSize(24);

        layout.addView(title);


        server = createField(
                "Server",
                settings.getServer()
        );


        source = createField(
                "Zdrojová složka",
                settings.getSource()
        );


        target = createField(
                "Cílová složka",
                settings.getTarget()
        );


        count = createField(
                "Počet souborů",
                String.valueOf(settings.getCount())
        );


        anonymous = new CheckBox(this);
        anonymous.setText("Anonymní přihlášení");
        anonymous.setChecked(settings.isAnonymous());

        layout.addView(anonymous);


        username = createField(
                "Uživatel",
                settings.getUsername()
        );


        password = createField(
                "Heslo",
                settings.getPassword()
        );

        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );


        Button save = new Button(this);
        save.setText("ULOŽIT");

        save.setOnClickListener(v -> {

            saveSettings();

            Toast.makeText(
                    this,
                    "Uloženo",
                    Toast.LENGTH_SHORT
            ).show();

        });

        layout.addView(save);



        Button test = new Button(this);
        test.setText("TEST PŘIPOJENÍ");


        test.setOnClickListener(v -> {

            saveSettings();

            new Thread(() -> {

                SmbManager smb =
                        new SmbManager(settings);

                boolean result =
                        smb.testConnection();


                runOnUiThread(() -> {

                    if (result) {

                        Toast.makeText(
                                this,
                                "SMB připojení OK",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                this,
                                "SMB připojení selhalo",
                                Toast.LENGTH_LONG
                        ).show();

                    }

                });


            }).start();

        });


        layout.addView(test);


        setContentView(layout);
    }



    private EditText createField(String label, String value) {

        TextView text = new TextView(this);

        text.setText(label);
        text.setTextSize(16);

        layout.addView(text);


        EditText field = new EditText(this);

        field.setText(value);

        layout.addView(field);


        return field;
    }



    private void saveSettings() {

        settings.setServer(
                server.getText().toString()
        );


        settings.setSource(
                source.getText().toString()
        );


        settings.setTarget(
                target.getText().toString()
        );


        settings.setCount(
                Integer.parseInt(
                        count.getText().toString()
                )
        );


        settings.setAnonymous(
                anonymous.isChecked()
        );


        settings.setUsername(
                username.getText().toString()
        );


        settings.setPassword(
                password.getText().toString()
        );
    }
}
