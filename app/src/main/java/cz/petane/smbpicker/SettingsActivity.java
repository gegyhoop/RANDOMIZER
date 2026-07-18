package cz.petane.smbpicker;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText server;
    private EditText share;
    private EditText source;
    private EditText target;
    private EditText count;
    private EditText username;
    private EditText password;
    private CheckBox anonymous;

    private SettingsManager settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = new SettingsManager(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        TextView title = new TextView(this);
        title.setText("SMB Random Picker - Nastavení");
        title.setTextSize(22);

        layout.addView(title);

        server = createField("Server", settings.getServer());
        share = createField("Sdílená složka", settings.getShare());
        source = createField("Zdrojová složka", settings.getSource());
        target = createField("Cílová složka", settings.getTarget());
        count = createField("Počet souborů", String.valueOf(settings.getCount()));

        anonymous = new CheckBox(this);
        anonymous.setText("Anonymní přihlášení");
        anonymous.setChecked(settings.isAnonymous());

        layout.addView(anonymous);

        username = createField("Uživatel", settings.getUsername());

        password = createField("Heslo", settings.getPassword());
        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        layout.addView(username);
        layout.addView(password);

        Button save = new Button(this);
        save.setText("ULOŽIT");

        save.setOnClickListener(v -> saveSettings());

        layout.addView(save);


        Button test = new Button(this);
        test.setText("TEST PŘIPOJENÍ");

        test.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Test SMB bude doplněn",
                        Toast.LENGTH_SHORT
                ).show()
        );

        layout.addView(test);

        setContentView(layout);
    }


    private EditText createField(String label, String value) {

        EditText field = new EditText(this);
        field.setHint(label);
        field.setText(value);

        return field;
    }


    private void saveSettings() {

        settings.setServer(server.getText().toString());
        settings.setShare(share.getText().toString());
        settings.setSource(source.getText().toString());
        settings.setTarget(target.getText().toString());

        try {
            settings.setCount(
                    Integer.parseInt(count.getText().toString())
            );
        } catch (Exception e) {
            settings.setCount(1);
        }

        settings.setAnonymous(
                anonymous.isChecked()
        );

        settings.setUsername(
                username.getText().toString()
        );

        settings.setPassword(
                password.getText().toString()
        );


        Toast.makeText(
                this,
                "Nastavení uloženo",
                Toast.LENGTH_SHORT
        ).show();
    }
}
