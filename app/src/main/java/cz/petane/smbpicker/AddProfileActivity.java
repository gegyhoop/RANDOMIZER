package cz.petane.smbpicker;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddProfileActivity extends AppCompatActivity {

    private EditText name, server, username, password, source, target, count;
    private CheckBox anonymous, autoUpdate;
    private Button timeButton;
    private int updateHour = 20, updateMinute = 0, historySize = 3;
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
        layout.setPadding(20, 100, 20, 20);

        TextView title = new TextView(this);
        title.setText("Nastavení profilu");
        title.setTextSize(26);
        layout.addView(title);

        name = addField(layout, "Název");
        server = addField(layout, "SMB server");
        username = addField(layout, "Uživatel");
        password = addField(layout, "Heslo");
        source = addField(layout, "Zdrojová složka");
        target = addField(layout, "Cílová složka");
        count = addField(layout, "Počet souborů");
        count.setText("1");

        anonymous = new CheckBox(this);
        anonymous.setText("Anonymní přihlášení");
        anonymous.setChecked(true);
        layout.addView(anonymous);

        autoUpdate = new CheckBox(this);
        autoUpdate.setText("Automatická aktualizace");
        layout.addView(autoUpdate);

        historySize = 3;

        EditText history = addField(layout, "Počet posledních výběrů bez opakování");
        history.setText("3");

        timeButton = new Button(this);
        updateTimeText();
        timeButton.setOnClickListener(v -> showTimePicker());
        layout.addView(timeButton);

        Button test = new Button(this);
        test.setText("Test SMB připojení");
        test.setOnClickListener(v -> testConnection());
        layout.addView(test);

        Button save = new Button(this);
        save.setText("Uložit");
        save.setOnClickListener(v -> saveProfile());
        layout.addView(save);

        setContentView(layout);

        history.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                try {
                    historySize = Math.max(0,
                            Integer.parseInt(history.getText().toString()));
                } catch(Exception e) {
                    historySize = 3;
                    history.setText("3");
                }
            }
        });
    }

    private void updateTimeText() {
        timeButton.setText(String.format(
                "Čas aktualizace: %02d:%02d",
                updateHour, updateMinute
        ));
    }

    private void showTimePicker() {
        new TimePickerDialog(
                this,
                (view, hour, minute) -> {
                    updateHour = hour;
                    updateMinute = minute;
                    updateTimeText();
                },
                updateHour,
                updateMinute,
                true
        ).show();
    }

    private void loadExistingProfile() {
        String profileName =
                getIntent().getStringExtra("profileName");

        if(profileName == null) return;

        editingProfile =
                profileManager.getProfileById(profileName);

        if(editingProfile == null) return;

        name.setText(editingProfile.getName());
        server.setText(editingProfile.getServer());
        username.setText(editingProfile.getUsername());
        password.setText(editingProfile.getPassword());
        source.setText(editingProfile.getSource());
        target.setText(editingProfile.getTarget());
        count.setText(String.valueOf(editingProfile.getCount()));

        anonymous.setChecked(editingProfile.isAnonymous());
        autoUpdate.setChecked(editingProfile.isAutoUpdate());

        updateHour = editingProfile.getUpdateHour();
        updateMinute = editingProfile.getUpdateMinute();
        historySize = editingProfile.getHistorySize();

        updateTimeText();
    }

    private void testConnection() {
        Profile profile = new Profile();

        profile.setServer(server.getText().toString());
        profile.setSource(source.getText().toString());
        profile.setTarget(target.getText().toString());
        profile.setAnonymous(anonymous.isChecked());
        profile.setUsername(username.getText().toString());
        profile.setPassword(password.getText().toString());

        new Thread(() -> {
            boolean result =
                    new SmbManager(profile).testConnection();

            runOnUiThread(() ->
                    Toast.makeText(
                            this,
                            result ? "Připojení OK" : "Připojení selhalo",
                            Toast.LENGTH_LONG
                    ).show()
            );
        }).start();
    }

    private void saveProfile() {
        if(editingProfile == null)
            editingProfile = new Profile();

        editingProfile.setName(name.getText().toString());
        editingProfile.setServer(server.getText().toString());
        editingProfile.setUsername(username.getText().toString());
        editingProfile.setPassword(password.getText().toString());
        editingProfile.setSource(source.getText().toString());
        editingProfile.setTarget(target.getText().toString());
        editingProfile.setAnonymous(anonymous.isChecked());
        editingProfile.setAutoUpdate(autoUpdate.isChecked());
        editingProfile.setUpdateHour(updateHour);
        editingProfile.setUpdateMinute(updateMinute);
        editingProfile.setHistorySize(historySize);

        try {
            editingProfile.setCount(
                    Integer.parseInt(count.getText().toString())
            );
        } catch(Exception e) {
            editingProfile.setCount(1);
        }

        profileManager.updateProfile(editingProfile);

        if(editingProfile.isAutoUpdate())
            Scheduler.schedule(this, editingProfile);
        else
            Scheduler.cancel(this, editingProfile);

        finish();
    }
}
