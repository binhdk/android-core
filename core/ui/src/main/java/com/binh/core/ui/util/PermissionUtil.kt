package com.binh.core.ui.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.binh.core.data.util.preferencesSataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


private const val PREFS_LOCATION_REQUIRED = "location_required"
private const val PREFS_LOCATION_PERMISSION_REQUESTED = "location_permission_requested"
private const val PREFS_BLUETOOTH_PERMISSION_REQUESTED = "bluetooth_permission_requested"

/**
 * Checks whether Bluetooth is enabled.
 *
 * @return true if Bluetooth is enabled, false otherwise.
 */
fun isBleEnabled(context: Context): Boolean {
    val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    return bluetoothManager.adapter.isEnabled
}

/**
 * Returns whether Bluetooth Scan permission has been granted.
 *
 * @param context the context.
 * @return Whether Bluetooth Scan permission has been granted.
 */
fun isBluetoothScanPermissionGranted(context: Context): Boolean {
    return if (!isSorAbove())
        true
    else
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Returns whether Bluetooth Connect permission has been granted.
 *
 * @param context the context.
 * @return Whether Bluetooth Connect permission has been granted.
 */
fun isBluetoothConnectPermissionGranted(context: Context): Boolean {
    return if (!isSorAbove())
        true
    else
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Returns whether location permission and service is required in order to scan
 * for Bluetooth LE devices. This app does not need beacons and other location-intended
 * devices, and requests BLUETOOTH_SCAN permission with "never for location" flag.
 *
 * @return Whether the location permission and service running are required.
 */
fun isLocationPermissionRequired(): Boolean {
    // Location is required only for Android 6-11.
    return isMarshmallowOrAbove() && !isSorAbove()
}

/**
 * Checks for required permissions.
 *
 * @return True if permissions are already granted, false otherwise.
 */
fun isLocationPermissionGranted(context: Context): Boolean {
    return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
}

/**
 * Returns true if Bluetooth Scan permission has been requested at least twice and
 * user denied it, and checked 'Don't ask again'.
 *
 * @param activity the activity.
 * @return True if permission has been denied and the popup will not come up any more,
 * false otherwise.
 */
suspend fun isBluetoothScanPermissionDeniedForever(activity: Activity): Boolean {
    return (!isLocationPermissionGranted(activity) // Location permission must be denied
            && activity.preferencesSataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(PREFS_BLUETOOTH_PERMISSION_REQUESTED)] ?: false
    }.firstOrNull() == true
            && !ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.ACCESS_FINE_LOCATION
    )) // This method should return false
}

/**
 * Returns true if location permission has been requested at least twice and
 * user denied it, and checked 'Don't ask again'.
 *
 * @param activity the activity.
 * @return True if permission has been denied and the popup will not come up any more,
 * false otherwise.
 */
suspend fun isLocationPermissionDeniedForever(activity: Activity): Boolean {
    return (!isLocationPermissionGranted(activity) // Location permission must be denied
            && activity.preferencesSataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(PREFS_LOCATION_PERMISSION_REQUESTED)] ?: false
    }.firstOrNull() == true // Permission must have been requested before
            && !ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        Manifest.permission.ACCESS_FINE_LOCATION
    )) // This method should return false
}

/**
 * On some devices running Android Marshmallow or newer location services must be enabled in
 * order to scan for Bluetooth LE devices. This method returns whether the Location has been
 * enabled or not.
 *
 * @return True on Android 6.0+ if location mode is different than LOCATION_MODE_OFF.
 * It always returns true on Android versions prior to Marshmallow.
 */
fun isLocationEnabled(context: Context): Boolean {
    if (isMarshmallowOrAbove()) {
        val lm = context.getSystemService(LocationManager::class.java)
        return LocationManagerCompat.isLocationEnabled(lm)
    }
    return true
}

/**
 * Location enabled is required on some phones running Android 6 - 11
 * (for example on Nexus and Pixel devices). Initially, Samsung phones didn't require it,
 * but that has been fixed for those phones in Android 9.
 *
 * @param context the context.
 * @return False if it is known that location is not required, true otherwise.
 */
suspend fun isLocationRequired(context: Context): Boolean {
    return context.preferencesSataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(PREFS_LOCATION_REQUIRED)]
            ?: (isMarshmallowOrAbove() && !isSorAbove())
    }.firstOrNull() == true // Permission must have been requested before

}

/**
 * When a Bluetooth LE packet is received while Location is disabled it means that Location
 * is not required on this device in order to scan for LE devices. This is a case of Samsung
 * phones, for example. Save this information for the future to keep the Location info hidden.
 *
 * @param context the context.
 */
suspend fun markLocationNotRequired(context: Context) {
    context.preferencesSataStore.edit { preferences ->
        val key = booleanPreferencesKey(PREFS_LOCATION_REQUIRED)
        preferences[key] = false
    }
}

/**
 * The first time an app requests a permission there is no 'Don't ask again' checkbox and
 * [ActivityCompat.shouldShowRequestPermissionRationale] returns false.
 * This situation is similar to a permission being denied forever, so to distinguish both cases
 * a flag needs to be saved.
 *
 * @param context the context.
 */
suspend fun markBluetoothScanPermissionRequested(context: Context) {
    context.preferencesSataStore.edit { preferences ->
        val key = booleanPreferencesKey(PREFS_BLUETOOTH_PERMISSION_REQUESTED)
        preferences[key] = true
    }
}

suspend fun clearBluetoothPermissionRequested(context: Context) {
    context.preferencesSataStore.edit { preferences ->
        val key = booleanPreferencesKey(PREFS_BLUETOOTH_PERMISSION_REQUESTED)
        preferences[key] = false
    }
}

/**
 * The first time an app requests a permission there is no 'Don't ask again' checkbox and
 * [ActivityCompat.shouldShowRequestPermissionRationale] returns false.
 * This situation is similar to a permission being denied forever, so to distinguish both cases
 * a flag needs to be saved.
 *
 * @param context the context.
 */
suspend fun markLocationPermissionRequested(context: Context) {
    context.preferencesSataStore.edit { preferences ->
        val key = booleanPreferencesKey(PREFS_LOCATION_PERMISSION_REQUESTED)
        preferences[key] = true
    }
}

suspend fun clearLocationPermissionRequested(context: Context) {
    context.preferencesSataStore.edit { preferences ->
        val key = booleanPreferencesKey(PREFS_LOCATION_PERMISSION_REQUESTED)
        preferences[key] = false
    }
}


@SuppressLint("ObsoleteSdkInt")
fun isMarshmallowOrAbove(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

fun isSorAbove(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

/**
 * Opens application settings in Android Settings app.
 */
fun openPermissionSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", context.packageName, null)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

/**
 * Opens Location settings.
 */
fun openLocationSettings(
    requestLocationTurnOn: ActivityResultLauncher<Intent>
) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    requestLocationTurnOn.launch(intent)
}

/**
 * Shows a prompt to the user to enable Bluetooth on the device.
 *
 * @implSpec On Android 12+ BLUETOOTH_CONNECT permission needs to be granted before calling
 * this method. Otherwise, the app would crash with [SecurityException].
 * @see BluetoothAdapter.ACTION_REQUEST_ENABLE
 */
fun requestBluetoothEnabled(
    context: Context,
    requestBluetoothTurnOn: ActivityResultLauncher<Intent>
) {
    if (isBluetoothConnectPermissionGranted(context)) {
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestBluetoothTurnOn.launch(enableIntent)
    }
}