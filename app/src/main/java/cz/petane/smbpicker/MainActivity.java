package cz.petane.randomfuturama;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


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

            new Thread(() -> {

                EpisodePicker picker =
                        new EpisodePicker(settings);


                List<String> files =
                        picker.getRandomFiles();


                runOnUiThread(() -> {


                    if (files.isEmpty()) {

                        Toast.makeText(
                                this,
                                "Nenalezen žádný soubor",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }



                    StringBuilder text =
                            new StringBuilder();


                    for (String file : files) {

                        text.append(file)
                                .append("\n");
                    }



                    new android.app.AlertDialog.Builder(this)

                            .setTitle("Vybrané díly")

                            .setMessage(text.toString())


                            .setNegativeButton(
                                    "ZRUŠIT",
                                    null
                            )


                            .setPositiveButton(
                                    "PROVEĎ",
                                    (dialog, which) -> {


                                        new Thread(() -> {


                                            try {


                                                SmbFileMover mover =
                                                        new SmbFileMover(settings);



                                                boolean back =
                                                        mover.moveAllBack();



                                                if (!back) {

                                                    throw new Exception(
                                                            "Nepodařilo se vrátit staré soubory"
                                                    );

                                                }



                                                boolean moved =
                                                        mover.moveFiles(files);



                                                if (!moved) {

                                                    throw new Exception(
                                                            "Nepodařilo se přesunout nové soubory"
                                                    );

                                                }



                                                runOnUiThread(() -> {

                                                    Toast.makeText(
                                                            this,
                                                            "Hotovo",
                                                            Toast.LENGTH_LONG
                                                    ).show();

                                                });



                                            } catch (Exception e) {


                                                runOnUiThread(() -> {


                                                    new android.app.AlertDialog.Builder(this)

                                                            .setTitle("Chyba přenosu")

                                                            .setMessage(
                                                                    e.getClass().getName()
                                                                            + "\n\n"
                                                                            + e.getMessage()
                                                            )

                                                            .setPositiveButton(
                                                                    "OK",
                                                                    null
                                                            )

                                                            .show();


                                                });


                                                e.printStackTrace();

                                            }


                                        }).start();


                                    }
                            )


                            .show();


                });


            }).start();

        });


        layout.addView(pick);




        Button settingsButton = new Button(this);

        settingsButton.setText("NASTAVENÍ");


        settingsButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );


            startActivity(intent);

        });


        layout.addView(settingsButton);



        setContentView(layout);
    }
}
