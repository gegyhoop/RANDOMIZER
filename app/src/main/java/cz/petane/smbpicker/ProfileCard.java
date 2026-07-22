package cz.petane.smbpicker;


import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ProfileCard extends LinearLayout {


    private Profile profile;


    private MainActivity activity;



    public ProfileCard(
            Context context,
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





        TextView title =
                new TextView(getContext());



        title.setText(
                "📁 " + profile.getName()
        );


        title.setTextSize(22);



        addView(title);






        Button newEpisodes =
                new Button(getContext());


        newEpisodes.setText(
                "Nové díly"
        );


        addView(newEpisodes);







        Button settings =
                new Button(getContext());



        settings.setText(
                "Nastavení"
        );


        settings.setOnClickListener(
                v -> activity.openSettings(profile)
        );


        addView(settings);






        Button delete =
                new Button(getContext());


        delete.setText(
                "X"
        );


        delete.setOnClickListener(
                v -> activity.deleteProfile(profile)
        );


        addView(delete);



    }


}
