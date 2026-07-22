package cz.petane.smbpicker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;




public class EpisodeActivity extends AppCompatActivity {



    private Profile profile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        String name =
                getIntent()
                        .getStringExtra("profileName");



        ProfileManager manager =
                new ProfileManager(this);



        profile =
                manager.getProfileById(name);




        createLayout();



    }








    private void createLayout(){



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
                "Nové díly - "
                + profile.getName()
        );



        title.setTextSize(26);



        layout.addView(title);







        TextView info =
                new TextView(this);



        info.setText(

                "Server:\n"
                + profile.getServer()
                + "\n\nZdroj:\n"
                + profile.getSource()
                + "\n\nCíl:\n"
                + profile.getTarget()

        );



        layout.addView(info);







        Button run =
                new Button(this);



        run.setText(
                "Přesunout nové díly"
        );



        run.setOnClickListener(
                v -> runPicker()
        );



        layout.addView(run);




        setContentView(layout);



    }









    private void runPicker(){



        Toast.makeText(
                this,
                "Zatím test - připravíme SMB",
                Toast.LENGTH_LONG
        ).show();



    }



}
