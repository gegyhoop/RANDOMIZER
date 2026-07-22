package cz.petane.smbpicker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import java.util.List;



public class EpisodeActivity extends AppCompatActivity {



    private Profile profile;


    private TextView result;








    @Override
    protected void onCreate(Bundle savedInstanceState){



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
                "Aktualizovat díly - "
                + profile.getName()
        );



        title.setTextSize(26);



        layout.addView(title);








        TextView info =
                new TextView(this);



        info.setText(

                "Zdroj:\n"
                + profile.getSource()

                + "\n\nCíl:\n"
                + profile.getTarget()

                + "\n\nPočet dílů:\n"
                + profile.getCount()

        );



        layout.addView(info);








        Button run =
                new Button(this);



        run.setText(
                "Aktualizovat díly"
        );



        run.setOnClickListener(
                v -> updateEpisodes()
        );



        layout.addView(run);







        result =
                new TextView(this);



        result.setTextSize(18);



        layout.addView(result);







        setContentView(layout);



    }









    private void updateEpisodes(){



        try {



            EpisodePicker picker =
                    new EpisodePicker(profile);





            List<String> files =
                    picker.prepareEpisodes();







            if(files.size() == profile.getCount()){



                result.setText(
                        "HOTOVO"
                );



                Toast.makeText(
                        this,
                        "HOTOVO",
                        Toast.LENGTH_SHORT
                ).show();



            }
            else {



                result.setText(
                        "SELHALO"
                );



                Toast.makeText(
                        this,
                        "SELHALO",
                        Toast.LENGTH_SHORT
                ).show();



            }



        }
        catch(Exception e){



            result.setText(
                    "SELHALO"
            );



            Toast.makeText(
                    this,
                    "SELHALO",
                    Toast.LENGTH_SHORT
            ).show();



        }



    }



}
