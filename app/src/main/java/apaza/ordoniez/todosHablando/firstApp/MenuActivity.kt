package apaza.ordoniez.todosHablando.firstApp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import apaza.ordoniez.todosHablando.R

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu) // Asegúrate de que el layout sea el correcto

        val btnMenuApps: Button = findViewById(R.id.btnMenuApps)
        val btnProfile: Button = findViewById(R.id.btnProfile)
        val btnPassword: Button = findViewById(R.id.btnPassword)
        val btnSettings: Button = findViewById(R.id.btnSettings)

        btnMenuApps.setOnClickListener {
            // aqui vamos al menu de aplicaciones
            val intent = Intent(this, aplicaciones1::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            //aqui se navegara al perfil del usuario
        }

        btnPassword.setOnClickListener {
            // aqui vamos ala pantalla de registro de contraseñas
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btnSettings.setOnClickListener {
            //seria el menu de ajustes
        }
    }
}
