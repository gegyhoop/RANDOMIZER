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


    private LinearLayout layout;


    private TextView result;





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



        layout =
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

                        + "\n\nPočet:\n"
                        + profile.getCount()

        );



        layout.addView(info);








        Button run =
                new Button(this);



        run.setText(
                "Najít nové díly"
        );



        run.setOnClickListener(
                v -> runPicker()
        );



        layout.addView(run);







        result =
                new TextView(this);



        result.setTextSize(18);



        layout.addView(result);







        setContentView(layout);



    }









    private void runPicker(){



        try {


            EpisodePicker picker =
                    new EpisodePicker(profile);



            List<String> files =
                    picker.getRandomFiles();




            if(files.isEmpty()) {


                result.setText(
                        "Nenalezeny žádné soubory"
                );


                return;


            }






            StringBuilder text =
                    new StringBuilder();



            text.append(
                    "Vybrané soubory:\n\n"
            );




            for(String file : files) {


                text.append(
                        file
                );


                text.append(
                        "\n"
                );


            }




            result.setText(
                    text.toString()
            );



        }
        catch(Exception e){



            Toast.makeText(
                    this,
                    "Chyba: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();



        }


    }



}
