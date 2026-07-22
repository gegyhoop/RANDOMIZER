package cz.petane.smbpicker;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        TextView text = new TextView(this);

        text.setText(
                "SMB Random Picker\n\n"
                + "Aplikace spuštěna"
        );

        text.setTextSize(22);


        Button button = new Button(this);

        button.setText("Test");


        android.widget.LinearLayout layout =
                new android.widget.LinearLayout(this);


        layout.setOrientation(
                android.widget.LinearLayout.VERTICAL
        );


        layout.addView(text);

        layout.addView(button);


        setContentView(layout);

    }

}
