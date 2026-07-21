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

public class SettingsActivity extends AppCompatActivity {

    private ProfileManager profileManager;
    private Profile currentProfile;

    private EditText nameField, server, source, target, count;
    private EditText username, password;
    private CheckBox anonymous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileManager = new ProfileManager(this);
        String profileId = getIntent().getStringExtra("profile_id");
        currentProfile = profileManager.getProfileById(profileId);

        if (currentProfile == null) {
            finish();
            return;
        }

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        TextView title = new TextView(this);
        title.setText("Nastavení: " + currentProfile.getName());
        title.setTextSize(24);
        layout.addView(title);

        // Název dlaždice
        nameField = createField("Název dlaždice", currentProfile.getName(), layout);

        server = createField("Server", currentProfile.getServer(), layout);
        source = createField("Zdrojová složka", currentProfile.getSource(), layout);
        target = createField("Cílová složka", currentProfile.getTarget(), layout);
        count = createField("Počet souborů", String.valueOf(currentProfile.getCount()), layout);

        anonymous = new CheckBox(this);
        anonymous.setText("Anonymní přihlášení");
        anonymous.setChecked(currentProfile.isAnonymous());
        layout.addView(anonymous);

        username = createField("Uživatel", currentProfile.getUsername(), layout);
        password = createField("Heslo", currentProfile.getPassword(), layout);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button save = new Button(this);
        save.setText("ULOŽIT");
        save.setOnClickListener(v -> saveSettings());
        layout.addView(save);

        Button test = new Button(this);
        test.setText("TEST PŘIPOJENÍ");
        test.setOnClickListener(v -> testConnection());
        layout.addView(test);

        setContentView(layout);
    }

    private EditText createField(String label, String value, LinearLayout layout) {
        TextView tv = new TextView(this);
        tv.setText(label);
        tv.setTextSize(16);
        layout.addView(tv);

        EditText field = new EditText(this);
        field.setText(value);
        layout.addView(field);
        return field;
    }

    private void saveSettings() {
        currentProfile.setName(nameField.getText().toString().trim());
        currentProfile.setServer(server.getText().toString().trim());
        currentProfile.setSource(source.getText().toString().trim());
        currentProfile.setTarget(target.getText().toString().trim());

        try {
            currentProfile.setCount(Integer.parseInt(count.getText().toString().trim()));
        } catch (Exception e) {
            currentProfile.setCount(1);
        }

        currentProfile.setAnonymous(anonymous.isChecked());
        currentProfile.setUsername(username.getText().toString().trim());
        currentProfile.setPassword(password.getText().toString());

        profileManager.updateProfile(currentProfile);
        Toast.makeText(this, "Uloženo", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void testConnection() {
        saveSettings(); // uložit před testem
        // TODO: Použít SmbManager s profile
        Toast.makeText(this, "Test připojení (zatím placeholder)", Toast.LENGTH_LONG).show();
    }
}
