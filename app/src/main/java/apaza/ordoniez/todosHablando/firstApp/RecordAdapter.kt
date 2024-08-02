package apaza.ordoniez.todosHablando.firstApp

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import apaza.ordoniez.todosHablando.R

class RecordAdapter(
    private val context: Context,
    private val appList: List<ApplicationInfo>,
    private val packageManager: PackageManager
) : BaseAdapter() {

    override fun getCount(): Int {
        return appList.size
    }

    override fun getItem(position: Int): Any {
        return appList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_app_registro, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.app_icon),
                view.findViewById(R.id.app_name),
                view.findViewById(R.id.btnPlay) // Asegúrate de que este ID coincida con el del XML
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val appInfo = appList[position]
        val appName = packageManager.getApplicationLabel(appInfo)
        holder.name.text = appName
        holder.icon.setImageDrawable(appInfo.loadIcon(packageManager))

        holder.listenButton.setOnClickListener {
            val intent = Intent(context, RegistroDetailActivity::class.java).apply {
                putExtra("APP_NAME", appName.toString())
                putExtra("APP_PACKAGE_NAME", appInfo.packageName)
                putExtra("APP_ICON", drawableToBitmap(appInfo.loadIcon(packageManager)))
            }
            context.startActivity(intent)
        }

        return view
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private class ViewHolder(val icon: ImageView, val name: TextView, val listenButton: Button)
}
