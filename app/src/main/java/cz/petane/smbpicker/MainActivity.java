package cz.petane.smbpicker;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText("Appka startovala\n\nPokud vidíš toto, dashboard selhal dříve.");
        tv.setTextSize(18);
        setContentView(tv);

        try {
            ProfileManager pm = new ProfileManager(this);
            Toast.makeText(this, "ProfileManager OK - počet profilů: " + pm.getAllProfiles().size(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "CHYBA: " + e.getClass().getSimpleName() + "\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
