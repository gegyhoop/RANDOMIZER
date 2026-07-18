package cz.petane.smbpicker

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val button = Button(this)
        button.text = "Vybrat nové díly"

        button.setOnClickListener {
            Toast.makeText(
                this,
                "Aplikace funguje",
                Toast.LENGTH_SHORT
            ).show()
        }

        layout.addView(button)

        setContentView(layout)
    }
}
