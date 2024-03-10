package com.playsnyc.realistix.utils;

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class MyPermissionHelper {

    var requestCameraPermissionResultcomposable: ManagedActivityResultLauncher<String, Boolean>? =
        null
    var requestCameraPermissionResultActivity: ActivityResultLauncher<Array<String>>? = null
    var callback: ((Boolean) -> Unit)? = null
    var isActivity: Boolean = false
    lateinit var context: Context
    fun hasPermission(permission: String): Boolean {
        if (::context.isInitialized.not()) {
            throw Exception("MyPermissionHelper Not Initialized")
        }
        val granted = PackageManager.PERMISSION_GRANTED
        return ContextCompat.checkSelfPermission(context, permission) == granted;
    }

    fun hasPermissions(permissions: Array<String>): Boolean {
        if (::context.isInitialized.not()) {
            throw Exception("MyPermissionHelper Not Initialized")
        }
        permissions.forEach {
            if (!hasPermission(it)) {
                return false
            }
        }
        return true
    }

    fun hasPermissionsElseTake(vararg permission: String, callback: ((Boolean) -> Unit)?): Boolean {
        this.callback = callback
        permission.forEach {
            if (!hasPermission(it)) {
                requestCameraPermissionResultActivity?.launch(permission.asList().toTypedArray())
            }
        }
        return true
    }

    fun takePermissions(permissions: Array<String>, callback: ((Boolean) -> Unit)?): Boolean {
        this.callback = callback


        val shouldShowPermissionRationale=permissions.find { (context as Activity).shouldShowRequestPermissionRationale(it) }
        if (shouldShowPermissionRationale!=null) {
            Toast.makeText(context, "Please Allow permissions", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
            callback?.invoke(false)
            return false

        } else {
            requestCameraPermissionResultActivity?.launch(permissions)
        }

        return true
    }

    fun hasPermissionElseTake(permission: String, callback: ((Boolean) -> Unit)?) {
        val permissionGranted = hasPermission(permission)
        if (permissionGranted) {
            callback?.invoke(true)
        } else {
            takePermission(permission, callback)
        }
    }


    fun takePermission(
        permission: String,
        callback: ((Boolean) -> Unit)? = null
    ) {
        if (::context.isInitialized.not()) {
            throw Exception("MyPermissionHelper Not Initialized")
        }
        requestCameraPermissionResultcomposable?.launch(permission)
        this.callback = callback
    }



    @Composable
    fun initComposable(): MyPermissionHelper {
        context = LocalContext.current
        isActivity = false
        requestCameraPermissionResultcomposable = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            this.callback?.invoke(isGranted)
        }
        return this
    }

    fun initActivity(activity: AppCompatActivity): MyPermissionHelper {
        context = activity
        isActivity = true
        requestCameraPermissionResultActivity =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { entry ->
                    val permission = entry.key
                    val isGranted = entry.value
                    if (!isGranted) {
                        this.callback?.invoke(false)
                    }
                }
                this.callback?.invoke(true)
            }
        return this
    }

}