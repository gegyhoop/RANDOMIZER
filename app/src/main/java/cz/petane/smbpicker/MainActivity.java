package cz.petane.smbpicker;

import android.os.Bundle;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 50, 50, 50);

        TextView text = new TextView(this);
        text.setText("SMB Random Picker\n\nTestovací verze");
        text.setTextSize(24);
        text.setTextColor(Color.BLACK);

        Button button = new Button(this);
        button.setText("Vybrat nové díly");

        button.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Aplikace funguje",
                        Toast.LENGTH_SHORT
                ).show()
        );

        layout.addView(text);
        layout.addView(button);

        setContentView(layout);
    }
}
