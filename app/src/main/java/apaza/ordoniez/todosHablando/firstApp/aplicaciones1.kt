package apaza.ordoniez.todosHablando.firstApp

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import apaza.ordoniez.todosHablando.R
import java.util.concurrent.TimeUnit

class aplicaciones1 : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: AppListAdapter1

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_aplicaciones1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtenemos la referencia del ListView
        listView = findViewById(R.id.listView)

        // comprovemos los permisos
        checkUsageStatsPermission()
    }

    private fun checkUsageStatsPermission() {
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            "android:get_usage_stats",
            android.os.Process.myUid(),
            packageName
        )
        if (mode == android.app.AppOpsManager.MODE_ALLOWED) {
            // Permiso otorgado, mostrar las aplicaciones instaladas
            displayInstalledApps()
        } else {
            // Solicitar permiso al usuario
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Intentar mostrar las aplicaciones nuevamente al regresar de la configuración de permisos
        checkUsageStatsPermission()
    }

    private fun displayInstalledApps() {
        // Verificar si se tiene permiso de acceso a las estadísticas de uso
        val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            "android:get_usage_stats",
            android.os.Process.myUid(),
            packageName
        )
        if (mode != android.app.AppOpsManager.MODE_ALLOWED) {
            Log.d("AppList", "Permiso de acceso a estadísticas de uso no otorgado.")
            return
        }

        // Obtener el UsageStatsManager para acceder a las estadísticas de uso
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // Definir el rango de tiempo para obtener las estadísticas de uso (últimos 7 días)
        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(7)

        // Obtener las estadísticas de uso en el rango de tiempo definido
        val usageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startTime,
            endTime
        )

        // Comprobar si se obtuvieron estadísticas de uso
        if (usageStats.isEmpty()) {
            Log.d("AppList", "No se obtuvieron estadísticas de uso.")
            return
        }

        // Crear un mapa para almacenar el tiempo de uso total por paquete
        val usageMap = mutableMapOf<String, Long>()
        for (stat in usageStats) {
            val totalTime = usageMap.getOrDefault(stat.packageName, 0L) + stat.totalTimeInForeground
            usageMap[stat.packageName] = totalTime
        }

        // Obtener el PackageManager para acceder a las aplicaciones instaladas
        val packageManager: PackageManager = packageManager
        val packages: List<ApplicationInfo> =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        // Filtrar y ordenar las aplicaciones instaladas por su uso
        val appsByUsage = packages.filter {
            usageMap.containsKey(it.packageName)
        }.sortedByDescending {
            usageMap[it.packageName]
        }

        // Si no hay aplicaciones que mostrar, registrar un mensaje
        if (appsByUsage.isEmpty()) {
            Log.d("AppList", "No hay aplicaciones para mostrar.")
        }

        // Configurar el adaptador personalizado con las aplicaciones ordenadas
        adapter = AppListAdapter1(this, appsByUsage, packageManager)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val app = appsByUsage[position]
            val launchIntent = packageManager.getLaunchIntentForPackage(app.packageName)
            if (launchIntent != null) {
                startActivity(launchIntent)
            } else {
                Toast.makeText(this, "No se puede abrir la aplicación", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
