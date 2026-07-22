package cz.petane.smbpicker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;



public class EpisodeActivity extends AppCompatActivity {



    private Profile profile;


    private LinearLayout layout;


    private TextView result;



    private ArrayList<String> selectedFiles;







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



        selectedFiles =
                new ArrayList<>();



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

                        + "\n\nPočet souborů:\n"
                        + profile.getCount()

        );



        layout.addView(info);








        Button find =
                new Button(this);



        find.setText(
                "Najít nové díly"
        );



        find.setOnClickListener(
                v -> findEpisodes()
        );



        layout.addView(find);







        Button move =
                new Button(this);



        move.setText(
                "Přesunout vybrané"
        );



        move.setOnClickListener(
                v -> moveEpisodes()
        );



        layout.addView(move);







        result =
                new TextView(this);



        result.setTextSize(18);



        layout.addView(result);







        setContentView(layout);



    }









    private void findEpisodes(){



        selectedFiles.clear();



        try {



            EpisodePicker picker =
                    new EpisodePicker(profile);



            List<String> files =
                    picker.getRandomFiles();




            selectedFiles.addAll(files);




            if(selectedFiles.isEmpty()) {



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




            for(String file : selectedFiles){



                text.append(file);

                text.append("\n");



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









    private void moveEpisodes(){



        if(selectedFiles.isEmpty()) {



            Toast.makeText(
                    this,
                    "Nejdříve vyber soubory",
                    Toast.LENGTH_SHORT
            ).show();



            return;


        }







        SmbManager smb =
                new SmbManager(profile);




        int success = 0;




        for(String file : selectedFiles){



            if(smb.moveFile(file)){


                success++;


            }


        }






        Toast.makeText(
                this,
                "Přesunuto: "
                        + success
                        + "/"
                        + selectedFiles.size(),
                Toast.LENGTH_LONG
        ).show();





        selectedFiles.clear();



        result.setText(
                "Hotovo"
        );



    }



}
