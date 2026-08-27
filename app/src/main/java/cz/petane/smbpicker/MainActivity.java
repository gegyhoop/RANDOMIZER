package cz.petane.smbpicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private ProfileManager profileManager;
    private ArrayList<Profile> profiles;

    private static final int EXPORT_FILE = 1;
    private static final int IMPORT_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileManager = new ProfileManager(this);
        createLayout();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    100
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfiles();
    }

    private void createLayout() {
        ScrollView scrollView = new ScrollView(this);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 100, 20, 20);

        TextView title = new TextView(this);
        title.setText("SMB Random Picker");
        title.setTextSize(26);
        layout.addView(title);

        Button add = new Button(this);
        add.setText("+ Přidat profil");
        add.setOnClickListener(v -> openAddProfile());
        layout.addView(add);

        scrollView.addView(layout);
        setContentView(scrollView);
    }

    private void loadProfiles() {
        profiles = profileManager.getProfiles();
        showProfiles();
    }

    private void showProfiles() {
        while(layout.getChildCount() > 2)
            layout.removeViewAt(2);

        for(Profile profile : profiles)
            layout.addView(new ProfileCard(this, profile, v ->
                    Scheduler.updateProfile(this, profile)
            ));

        Button updateAll = new Button(this);
        updateAll.setText("Aktualizovat všechny profily");
        updateAll.setTextColor(Color.WHITE);
        updateAll.setBackgroundColor(Color.rgb(33, 150, 243));
        updateAll.setOnClickListener(v -> updateAllProfiles());
        layout.addView(updateAll);

        Button export = new Button(this);
        export.setText("Export nastavení");
        export.setOnClickListener(v -> exportProfiles());
        layout.addView(export);

        Button importButton = new Button(this);
        importButton.setText("Import nastavení");
        importButton.setOnClickListener(v -> importProfiles());
        layout.addView(importButton);
    }

    private void updateAllProfiles() {
        if(profiles == null || profiles.isEmpty()) {
            Toast.makeText(
                    this,
                    "Žádné profily k aktualizaci",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Scheduler.updateAllProfiles(this, profiles);

        Toast.makeText(
                this,
                "Aktualizace všech profilů spuštěna",
                Toast.LENGTH_LONG
        ).show();
    }

    private void openAddProfile() {
        startActivity(
                new Intent(this, AddProfileActivity.class)
        );
    }

    public void openSettings(Profile profile) {
        Intent intent =
                new Intent(this, AddProfileActivity.class);

        intent.putExtra(
                "profileName",
                profile.getName()
        );

        startActivity(intent);
    }

    public void openEpisodes(Profile profile) {
        Intent intent =
                new Intent(this, EpisodeActivity.class);

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

    private void exportProfiles() {
        Intent intent =
                new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.setType("application/json");
        intent.putExtra(
                Intent.EXTRA_TITLE,
                "SMB_Random_Picker_backup.json"
        );

        startActivityForResult(intent, EXPORT_FILE);
    }

    private void importProfiles() {
        Intent intent =
                new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.setType("application/json");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, IMPORT_FILE);
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if(resultCode != RESULT_OK || data == null)
            return;

        try {
            Uri uri = data.getData();

            if(requestCode == EXPORT_FILE) {
                BackupManager backup =
                        new BackupManager(this);

                File temp =
                        backup.exportProfiles();

                FileInputStream input =
                        new FileInputStream(temp);

                OutputStream output =
                        getContentResolver()
                                .openOutputStream(uri);

                byte[] buffer = new byte[4096];
                int length;

                while((length = input.read(buffer)) > 0)
                    output.write(buffer, 0, length);

                input.close();

                if(output != null)
                    output.close();

                Toast.makeText(
                        this,
                        "Export hotový",
                        Toast.LENGTH_LONG
                ).show();
            }

            if(requestCode == IMPORT_FILE) {
                File temp =
                        new File(
                                getCacheDir(),
                                "import.json"
                        );

                java.io.InputStream input =
                        getContentResolver()
                                .openInputStream(uri);

                OutputStream output =
                        new java.io.FileOutputStream(temp);

                byte[] buffer = new byte[4096];
                int length;

                while((length = input.read(buffer)) > 0)
                    output.write(buffer, 0, length);

                input.close();
                output.close();

                new BackupManager(this)
                        .importProfiles(temp);

                ArrayList<Profile> imported =
                        profileManager.getProfiles();

                for(Profile profile : imported) {
                    if(profile.isAutoUpdate())
                        Scheduler.schedule(this, profile);
                }

                Toast.makeText(
                        this,
                        "Import hotový",
                        Toast.LENGTH_LONG
                ).show();

                loadProfiles();
            }

        } catch(Exception e) {
            Toast.makeText(
                    this,
                    "Chyba: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {
        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if(requestCode == 100 &&
                grantResults.length > 0 &&
                grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(
                    this,
                    "Oznámení povolena",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
