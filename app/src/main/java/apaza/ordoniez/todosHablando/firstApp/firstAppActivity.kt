package apaza.ordoniez.todosHablando.firstApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import apaza.ordoniez.todosHablando.R

class firstAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_app)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Capturamos la pulsación del botón
        val btnSaludar = findViewById<Button>(R.id.btnSaludar)
        btnSaludar.setOnClickListener { saludo() }
    }

    // Con esta función accedemos a la siguiente pantalla
    fun saludo() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}
