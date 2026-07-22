package cz.petane.smbpicker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class AddProfileActivity extends AppCompatActivity {


    EditText name;
    EditText server;
    EditText source;
    EditText target;
    EditText count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        createLayout();

    }




    private EditText addField(
            LinearLayout layout,
            String text
    ){

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
                "Nový profil"
        );


        title.setTextSize(26);


        layout.addView(title);




        name =
                addField(
                        layout,
                        "Název"
                );


        server =
                addField(
                        layout,
                        "SMB server"
                );


        source =
                addField(
                        layout,
                        "Zdrojová složka"
                );


        target =
                addField(
                        layout,
                        "Cílová složka"
                );


        count =
                addField(
                        layout,
                        "Počet souborů"
                );


        count.setText("1");





        Button save =
                new Button(this);


        save.setText(
                "Uložit"
        );


        save.setOnClickListener(
                v -> save()
        );


        layout.addView(save);



        setContentView(layout);

    }







    private void save(){


        Profile profile =
                new Profile();



        profile.setName(
                name.getText().toString()
        );


        profile.setServer(
                server.getText().toString()
        );


        profile.setSource(
                source.getText().toString()
        );


        profile.setTarget(
                target.getText().toString()
        );



        try {


            profile.setCount(
                    Integer.parseInt(
                            count.getText().toString()
                    )
            );


        } catch(Exception e){


            profile.setCount(1);

        }



        ProfileManager manager =
                new ProfileManager(this);


        manager.updateProfile(profile);



        finish();

    }


}
