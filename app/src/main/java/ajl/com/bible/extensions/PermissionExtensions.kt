package ajl.com.bible.extensions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 * Extension for checking from activity
 */
fun Activity.isHasPermission(vararg permissions: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        permissions.all { singlePermission ->
            applicationContext.checkSelfPermission(singlePermission) == PackageManager.PERMISSION_GRANTED
        }
    else true
}

/**
 * Request permission from activity
 */
fun Activity.askPermission(vararg permissions: String, requestCode: Int) =
    ActivityCompat.requestPermissions(this, permissions, requestCode)

/**
 *  Extension for checking from fragment
 */
fun Fragment.isHasPermission(vararg permissions: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        permissions.all { singlePermission ->
            context!!.checkSelfPermission(singlePermission) == PackageManager.PERMISSION_GRANTED
        }
    else true
}

/**
 * Request permission from fragment
 */
fun Fragment.askPermission(vararg permissions: String, requestCode: Int) =
    ActivityCompat.requestPermissions(activity!!, permissions, requestCode)

