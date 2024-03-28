package uz.maxtac.glossary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import uz.maxtac.glossary.R

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object Utils {

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    @SuppressLint("HardwareIds")
    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
    fun dialogDouble(context: Context?, title: String?, callback: DialogListener) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_dialog_double)
        dialog.setCanceledOnTouchOutside(true)
        val d_title = dialog.findViewById<TextView>(R.id.d_title)
        val d_confirm = dialog.findViewById<TextView>(R.id.d_confirm)
        val d_cancel = dialog.findViewById<TextView>(R.id.d_cancel)
        d_title.text = title
        d_confirm.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(true)
        }
        d_cancel.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(false)
        }
        dialog.show()
    }

    fun validEditText(editText: EditText?, errorText: String): Boolean {
        val fullName: String = editText?.text.toString()
        return if (fullName.isEmpty()) {
            if (editText != null) {
                editText.error = errorText
            }
            false
        } else {
            if (editText != null) {
                editText.error = null
            }
            true
        }
    }


    fun replaceWords(word: String, replace: String?, newWord: String): String? {
        return word.replace(replace!!, newWord)
    }

    fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    fun getTimeAgo(created_at: String): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        return try {
            val time: Long = sdf.parse(created_at)!!.time
            val now = System.currentTimeMillis()
            val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
            ago.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun formatingDateAsTime(created_at: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        return try {
            val date = df.parse(created_at)
            val dateString = DateFormat.format("HH:mm", date).toString()
            dateString.toString()
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun getCurrentTime(): String {
        val formatted =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
        return formatted.toString()
    }

    fun getUUID(): String {
        val uuid: String = UUID.randomUUID().toString().replace("-", "");
        return uuid
    }

    fun setLocaleLanguage(activity: Activity, languageCode: String?): Configuration {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        return config
    }

    fun addSpaceToNumber(number: String): String {
        val dec = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))
        return dec.format(number.toInt()).replace(",", " ")
    }

    fun showLoading(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideLoading(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }

//    fun showEmptyState(
//        emptyState: ItemEmptyStateBinding,
//        visibility: Int,
//        image: Int,
//        title: String,
//        message: String
//    ) {
//        emptyState.llEmpty.visibility = visibility
//        emptyState.ivImageEmptyState.setImageResource(image)
//        emptyState.tvTitleEmptyState.text = title
//        emptyState.tvMessageEmptyState.text = message
//    }

}

interface DialogListener {
    fun onCallback(isChosen: Boolean)
}
