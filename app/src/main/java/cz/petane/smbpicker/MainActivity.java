package cz.petane.smbpicker;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileManager = new ProfileManager(this);
        showDashboard();
    }

    private void showDashboard() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("SMB Random Picker");
        title.setTextSize(28);
        mainLayout.addView(title);

        List<Profile> profiles = profileManager.getAllProfiles();

        if (profiles.isEmpty()) {
            // První spuštění - vytvořit výchozí profil
            Profile defaultProfile = new Profile();
            defaultProfile.setName("Výchozí seriál");
            profileManager.addProfile(defaultProfile);
            profiles = profileManager.getAllProfiles();
        }

        for (Profile profile : profiles) {
            LinearLayout tile = createProfileTile(profile);
            mainLayout.addView(tile);
        }

        // Tlačítko + na přidání nové dlaždice
        Button addButton = new Button(this);
        addButton.setText("+ Přidat nový seriál / složku");
        addButton.setOnClickListener(v -> addNewProfile());
        mainLayout.addView(addButton);

        setContentView(mainLayout);
    }

    private LinearLayout createProfileTile(Profile profile) {
        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setPadding(24, 24, 24, 24);
        tile.setBackgroundColor(0xFF333333); // tmavá dlaždice
        tile.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tile.setPadding(24, 24, 24, 24);

        TextView name = new TextView(this);
        name.setText(profile.getName());
        name.setTextSize(20);
        tile.addView(name);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);

        Button pickBtn = new Button(this);
        pickBtn.setText("NOVÉ DÍLY");
        pickBtn.setOnClickListener(v -> startPicking(profile));
        buttons.addView(pickBtn);

        Button settingsBtn = new Button(this);
        settingsBtn.setText("NASTAVENÍ");
        settingsBtn.setOnClickListener(v -> openSettings(profile));
        buttons.addView(settingsBtn);

        Button deleteBtn = new Button(this);
        deleteBtn.setText("X");
        deleteBtn.setOnClickListener(v -> deleteProfile(profile));
        buttons.addView(deleteBtn);

        tile.addView(buttons);
        return tile;
    }

    private void startPicking(Profile profile) {
        // Zatím placeholder - později přesuneme logiku z EpisodePicker
        Toast.makeText(this, "Vybrané díly pro: " + profile.getName(), Toast.LENGTH_SHORT).show();
        // TODO: Spustit EpisodePicker s profile
    }

    private void openSettings(Profile profile) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("profile_id", profile.getId());
        startActivity(intent);
    }

    private void deleteProfile(Profile profile) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Smazat profil")
                .setMessage("Opravdu smazat " + profile.getName() + "?")
                .setPositiveButton("Ano", (d, w) -> {
                    profileManager.deleteProfile(profile.getId());
                    showDashboard(); // refresh
                })
                .setNegativeButton("Ne", null)
                .show();
    }

    private void addNewProfile() {
        Profile newProfile = new Profile();
        newProfile.setName("Nový seriál " + (profileManager.getAllProfiles().size() + 1));
        profileManager.addProfile(newProfile);
        showDashboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDashboard(); // refresh po návratu z nastavení
    }
}
