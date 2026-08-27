package cz.petane.smbpicker;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileCard extends LinearLayout {

    public ProfileCard(
            MainActivity activity,
            Profile profile,
            View.OnClickListener ignored
    ) {
        super(activity);

        setOrientation(VERTICAL);
        setPadding(20, 20, 20, 20);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.TRANSPARENT);
        bg.setStroke(2, Color.GRAY);
        setBackground(bg);

        TextView name = new TextView(activity);
        name.setText(profile.getName());
        name.setTextSize(20);
        addView(name);

        Button episodes = new Button(activity);
        episodes.setText("Aktualizovat");
        episodes.setOnClickListener(v ->
                Scheduler.updateProfile(activity, profile)
        );
        addView(episodes);

        Button settings = new Button(activity);
        settings.setText("Nastavení");
        settings.setOnClickListener(v ->
                activity.openSettings(profile)
        );
        addView(settings);

        Button show = new Button(activity);
        show.setText("Zobrazit epizody");
        show.setOnClickListener(v ->
                activity.openEpisodes(profile)
        );
        addView(show);

        Button delete = new Button(activity);
        delete.setText("Smazat");
        delete.setOnClickListener(v ->
                activity.deleteProfile(profile)
        );
        addView(delete);
    }
}
