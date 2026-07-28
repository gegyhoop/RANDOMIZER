package cz.petane.smbpicker;

import android.app.TimePickerDialog;
import android.os.Bundle;
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
    private CheckBox autoUpdate;

    private Button timeButton;

    private int updateHour = 20;
    private int updateMinute = 0;

    private ProfileManager profileManager;
    private Profile editingProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileManager = new ProfileManager(this);

        createLayout();

        loadExistingProfile();
    }

    private EditText addField(
            LinearLayout layout,
            String text
    ) {

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

    private void createLayout() {

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

        name = addField(layout,"Název");
        server = addField(layout,"SMB server");
        username = addField(layout,"Uživatel");
        password = addField(layout,"Heslo");
        source = addField(layout,"Zdrojová složka");
        target = addField(layout,"Cílová složka");
        count = addField(layout,"Počet souborů");

        count.setText("1");

        anonymous =
                new CheckBox(this);

        anonymous.setText(
                "Anonymní přihlášení"
        );

        anonymous.setChecked(true);

        layout.addView(anonymous);

        autoUpdate =
                new CheckBox(this);

        autoUpdate.setText(
                "Automatická aktualizace"
        );

        layout.addView(autoUpdate);

        timeButton =
                new Button(this);

        updateTimeText();

        timeButton.setOnClickListener(
                v -> showTimePicker()
        );

        layout.addView(timeButton);

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

    private void updateTimeText() {

        timeButton.setText(
                String.format(
                        "Čas aktualizace: %02d:%02d",
                        updateHour,
                        updateMinute
                )
        );
    }

    private void showTimePicker() {

        TimePickerDialog dialog =
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
                );

        dialog.show();
    }

    private void loadExistingProfile() {

        String profileName =
                getIntent()
                        .getStringExtra("profileName");

        if(profileName == null) {
            return;
        }

        editingProfile =
                profileManager.getProfileById(profileName);

        if(editingProfile.getName() != null) {

            name.setText(editingProfile.getName());
            server.setText(editingProfile.getServer());
            username.setText(editingProfile.getUsername());
            password.setText(editingProfile.getPassword());
            source.setText(editingProfile.getSource());
            target.setText(editingProfile.getTarget());

            count.setText(
                    String.valueOf(
                            editingProfile.getCount()
                    )
            );

            anonymous.setChecked(
                    editingProfile.isAnonymous()
            );

            autoUpdate.setChecked(
                    editingProfile.isAutoUpdate()
            );

            updateHour =
                    editingProfile.getUpdateHour();

            updateMinute =
                    editingProfile.getUpdateMinute();

            updateTimeText();
        }
    }

    private void testConnection() {

        Profile testProfile =
                new Profile();

        testProfile.setServer(
                server.getText().toString()
        );

        testProfile.setSource(
                source.getText().toString()
        );

        testProfile.setTarget(
                target.getText().toString()
        );

        testProfile.setAnonymous(
                anonymous.isChecked()
        );

        testProfile.setUsername(
                username.getText().toString()
        );

        testProfile.setPassword(
                password.getText().toString()
        );

        new Thread(() -> {

            SmbManager smb =
                    new SmbManager(testProfile);

            boolean result =
                    smb.testConnection();

            runOnUiThread(() -> {

                Toast.makeText(
                        this,
                        result ?
                                "Připojení OK" :
                                "Připojení selhalo",
                        Toast.LENGTH_LONG
                ).show();

            });

        }).start();
    }

    private void saveProfile() {

        if(editingProfile == null) {

            editingProfile =
                    new Profile();
        }

        editingProfile.setName(
                name.getText().toString()
        );

        editingProfile.setServer(
                server.getText().toString()
        );

        editingProfile.setUsername(
                username.getText().toString()
        );

        editingProfile.setPassword(
                password.getText().toString()
        );

        editingProfile.setSource(
                source.getText().toString()
        );

        editingProfile.setTarget(
                target.getText().toString()
        );

        editingProfile.setAnonymous(
                anonymous.isChecked()
        );

        editingProfile.setAutoUpdate(
                autoUpdate.isChecked()
        );

        editingProfile.setUpdateHour(
                updateHour
        );

        editingProfile.setUpdateMinute(
                updateMinute
        );

        try {

            editingProfile.setCount(
                    Integer.parseInt(
                            count.getText().toString()
                    )
            );

        } catch(Exception e) {

            editingProfile.setCount(1);
        }

        profileManager.updateProfile(
                editingProfile
        );

        if(editingProfile.isAutoUpdate()) {

            Scheduler.schedule(
                    this,
                    editingProfile
            );

        } else {

            Scheduler.cancel(
                    this,
                    editingProfile
            );
        }

        finish();
    }
}
