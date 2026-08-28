package cz.petane.smbpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileCard extends LinearLayout {

    public ProfileCard(
            Context context,
            Profile profile,
            View.OnClickListener listener
    ) {
        super(context);

        setOrientation(VERTICAL);
        setPadding(20, 20, 20, 20);

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(Color.TRANSPARENT);
        background.setStroke(2, Color.GRAY);
        setBackground(background);

        TextView name = new TextView(context);
        name.setText(profile.getName());
        name.setTextSize(20);
        addView(name);

        Button update = new Button(context);
        update.setText("Aktualizovat");
        update.setOnClickListener(listener);
        addView(update);

        Button settings = new Button(context);
        settings.setText("Nastavení");
        settings.setOnClickListener(v ->
                ((MainActivity) context)
                        .openSettings(profile)
        );
        addView(settings);

        Button episodes = new Button(context);
        episodes.setText("Zobrazit epizody");
        episodes.setOnClickListener(v ->
                ((MainActivity) context)
                        .openEpisodes(profile)
        );
        addView(episodes);

        Button delete = new Button(context);
        delete.setText("Smazat");
        delete.setOnClickListener(v ->
                ((MainActivity) context)
                        .deleteProfile(profile)
        );
        addView(delete);
    }
}
