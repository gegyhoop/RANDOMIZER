package cz.petane.smbpicker;


import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ProfileCard extends LinearLayout {


    private Profile profile;


    private MainActivity activity;





    public ProfileCard(
            android.content.Context context,
            Profile profile,
            MainActivity activity
    ) {


        super(context);


        this.profile = profile;

        this.activity = activity;



        create();

    }








    private void create() {


        setOrientation(
                VERTICAL
        );


        setPadding(
                20,
                20,
                20,
                20
        );


        setBackgroundColor(
                Color.LTGRAY
        );



        LinearLayout header =
                new LinearLayout(getContext());


        header.setOrientation(
                HORIZONTAL
        );


        header.setGravity(
                Gravity.CENTER_VERTICAL
        );





        TextView title =
                new TextView(getContext());



        title.setText(
                "📁 " + profile.getName()
        );


        title.setTextSize(22);



        header.addView(
                title,
                new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1
                )
        );





        Button delete =
                new Button(getContext());



        delete.setText(
                "X"
        );



        delete.setOnClickListener(
                v -> activity.deleteProfile(profile)
        );



        header.addView(delete);




        addView(header);








        LinearLayout buttons =
                new LinearLayout(getContext());



        buttons.setOrientation(
                HORIZONTAL
        );





        Button episodes =
                new Button(getContext());



        episodes.setText(
                "Nové díly"
        );



        episodes.setOnClickListener(
                v -> activity.openEpisodes(profile)
        );




        buttons.addView(
                episodes,
                new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1
                )
        );







        Button settings =
                new Button(getContext());



        settings.setText(
                "Nastavení"
        );



        settings.setOnClickListener(
                v -> activity.openSettings(profile)
        );



        buttons.addView(
                settings,
                new LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1
                )
        );





        addView(buttons);



    }


}
