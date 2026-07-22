package cz.petane.smbpicker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;



public class AddProfileActivity extends AppCompatActivity {


    private EditText name;
    private EditText server;
    private EditText source;
    private EditText target;
    private EditText count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        createLayout();

    }





    private EditText createField(
            String hint,
            LinearLayout layout
    ) {


        TextView label =
                new TextView(this);

        label.setText(hint);


        layout.addView(label);



        EditText edit =
                new EditText(this);


        edit.setHint(hint);


        layout.addView(edit);


        return edit;

    }






    private void createLayout() {


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
                createField(
                        "Název seriálu",
                        layout
                );



        server =
                createField(
                        "SMB server",
                        layout
                );



        source =
                createField(
                        "Zdrojová složka",
                        layout
                );



        target =
                createField(
                        "Cílová složka",
                        layout
                );



        count =
                createField(
                        "Počet dílů",
                        layout
                );


        count.setText("1");




        Button save =
                new Button(this);


        save.setText(
                "Uložit"
        );


        save.setOnClickListener(
                v -> saveProfile()
        );


        layout.addView(save);



        setContentView(layout);

    }






    private void saveProfile() {


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


        } catch(Exception e) {

            profile.setCount(1);

        }




        ProfileManager manager =
                new ProfileManager(this);



        manager.updateProfile(profile);



        finish();

    }


}
