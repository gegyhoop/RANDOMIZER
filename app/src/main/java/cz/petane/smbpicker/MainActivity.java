package cz.petane.smbpicker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SettingsManager settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = new SettingsManager(this);

        showMainScreen();
    }


    private void showMainScreen() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);


        TextView title = new TextView(this);
        title.setText("SMB Random Picker");
        title.setTextSize(26);

        layout.addView(title);


        Button pick = new Button(this);
        pick.setText("NOVÉ DÍLY");

        pick.setOnClickListener(v -> {
            // výběr dílů přidáme později
        });

        layout.addView(pick);


        Button settingsButton = new Button(this);
        settingsButton.setText("NASTAVENÍ");

        settingsButton.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);

        });

        layout.addView(settingsButton);


        setContentView(layout);
    }
}
