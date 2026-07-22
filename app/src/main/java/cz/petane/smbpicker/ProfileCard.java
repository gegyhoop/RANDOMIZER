package cz.petane.smbpicker;


import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ProfileCard extends LinearLayout {


    private Profile profile;


    private MainActivity activity;


    private TextView countText;





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
                25,
                20,
                25
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









        LinearLayout countLayout =
                new LinearLayout(getContext());


        countLayout.setOrientation(
                HORIZONTAL
        );


        countLayout.setGravity(
                Gravity.CENTER_VERTICAL
        );






        TextView label =
                new TextView(getContext());


        label.setText(
                "Počet dílů:"
        );


        label.setTextSize(18);



        countLayout.addView(label);







        Button minus =
                new Button(getContext());


        minus.setText(
                "▼"
        );



        minus.setOnClickListener(
                v -> changeCount(-1)
        );



        countLayout.addView(minus);








        countText =
                new TextView(getContext());


        countText.setText(
                String.valueOf(
                        profile.getCount()
                )
        );


        countText.setTextSize(20);


        countText.setGravity(
                Gravity.CENTER
        );


        countLayout.addView(
                countText,
                new LayoutParams(
                        80,
                        LayoutParams.WRAP_CONTENT
                )
        );








        Button plus =
                new Button(getContext());


        plus.setText(
                "▲"
        );


        plus.setOnClickListener(
                v -> changeCount(1)
        );



        countLayout.addView(plus);





        addView(countLayout);









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









    private void changeCount(int change){



        int value =
                profile.getCount();



        value += change;



        if(value < 1){

            value = 1;

        }



        profile.setCount(value);



        countText.setText(
                String.valueOf(value)
        );




        ProfileManager manager =
                new ProfileManager(getContext());



        manager.updateProfile(profile);



    }



}
