package com.headway.bablicabdriver.utils.permissionhandler

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.core.net.toUri

private const val PERMISSIONS_CLICK_DELAY_MS = 200

private var lastPermissionRequestLaunchedAt = 0L

@Composable
fun rememberPermissionsState(
    permissions: List<String>,
    onGrantedAction: () -> Unit = {},
    onDeniedAction: (List<String>) -> Unit = {},
    onPermanentlyDeniedAction: (List<String>) -> Unit = {}
): MultiplePermissionsState {
    // Create mutable permissions that can be requested individually
    val mutablePermissions = rememberMutablePermissionsState(permissions)

    // Refresh permissions when the lifecycle is resumed.
    PermissionsLifecycleCheckerEffect(mutablePermissions)

    val multiplePermissionsState = remember(permissions) {
        MultiplePermissionsState(mutablePermissions)
    }

    // Remember RequestMultiplePermissions launcher and assign it to multiplePermissionsState
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        multiplePermissionsState.updatePermissionsStatus(permissionsResult)

        if (!permissionsResult.containsValue(false)) {
            onGrantedAction()
        } else if (System.currentTimeMillis() - PERMISSIONS_CLICK_DELAY_MS
            < lastPermissionRequestLaunchedAt
        ) {
            onPermanentlyDeniedAction(permissionsResult.filter { !it.value }.keys.toList())
        } else {
            onDeniedAction(permissionsResult.filter { !it.value }.keys.toList())
        }
    }
    DisposableEffect(multiplePermissionsState, launcher) {
        multiplePermissionsState.launcher = launcher
        onDispose {
            multiplePermissionsState.launcher = null
        }
    }

    return multiplePermissionsState
}

@Composable
private fun rememberMutablePermissionsState(
    permissions: List<String>
): List<PermissionState> {
    val context = LocalContext.current
    val activity = context.findActivity()

    val mutablePermissions: List<PermissionState> = remember(permissions) {
        return@remember permissions.map { PermissionState(it, context, activity) }
    }

    return mutablePermissions
}

@Composable
private fun PermissionsLifecycleCheckerEffect(
    permissions: List<PermissionState>,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_RESUME
) {
    // Check if the permission was granted when the lifecycle is resumed.
    // The user might've gone to the Settings screen and granted the permission.
    val permissionsCheckerObserver = remember(permissions) {
        LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                for (permission in permissions) {
                    // If the permission is revoked, check again. We don't check if the permission
                    // was denied as that triggers a process restart.
                    if (permission.status != PermissionStatus.Granted) {
                        permission.refreshPermissionStatus()
                    }
                }
            }
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, permissionsCheckerObserver) {
        lifecycle.addObserver(permissionsCheckerObserver)
        onDispose { lifecycle.removeObserver(permissionsCheckerObserver) }
    }
}



@Stable
sealed interface PermissionStatus {
    data object Granted : PermissionStatus
    data class Denied(
        val shouldShowRationale: Boolean
    ) : PermissionStatus
}

val PermissionStatus.isGranted: Boolean
    get() = this == PermissionStatus.Granted

val PermissionStatus.shouldShowRationale: Boolean
    get() = when (this) {
        PermissionStatus.Granted -> false
        is PermissionStatus.Denied -> shouldShowRationale
    }

@Stable
class PermissionState(
    /**
     * The full name of the permission in the Android SDK,
     * e.g. android.permission.ACCESS_FINE_LOCATION
     */
    val name: String,
    private val context: Context,
    private val activity: Activity
) {
    var status: PermissionStatus by mutableStateOf(getPermissionStatus())
        private set


    internal fun refreshPermissionStatus() {
        status = getPermissionStatus()
    }

    private fun getPermissionStatus(): PermissionStatus {
        val hasPermission = context.checkPermission(name)
        return if (hasPermission) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied(activity.shouldShowRationale(name))
        }
    }
}

/**
 * A state object that can be hoisted to control and observe multiple permission status changes.
 *
 * In most cases, this will be created via [rememberPermissionsState].
 *
 * @param mutablePermissions list of mutable permissions to control and observe.
 */
@Stable
class MultiplePermissionsState(
    private val mutablePermissions: List<PermissionState>
) {
    val permissions: List<PermissionState> = mutablePermissions

    val revokedPermissions: List<PermissionState> by derivedStateOf {
        permissions.filter { it.status != PermissionStatus.Granted }
    }

    val allPermissionsGranted: Boolean by derivedStateOf {
        permissions.all { it.status.isGranted } || // Up to date when the lifecycle is resumed
                revokedPermissions.isEmpty() // Up to date when the user launches the action
    }

    val shouldShowRationale: Boolean by derivedStateOf {
        permissions.any { it.status.shouldShowRationale }
    }

    /**
     * Request the [permissions] to the user and use actions declared in [rememberPermissionsState].
     *
     * This should always be triggered from non-composable scope, for example, from a side-effect
     * or a non-composable callback. Otherwise, this will result in an IllegalStateException.
     *
     * This triggers a system dialog that asks the user to grant or revoke the permission.
     * Note that this dialog might not appear on the screen if the user doesn't want to be asked
     * again or has denied the permission multiple times.
     * This behavior varies depending on the Android level API.
     */
    fun launchPermissionRequestsAndAction() {
        lastPermissionRequestLaunchedAt = System.currentTimeMillis()

        launcher?.launch(
            permissions.map { it.name }.toTypedArray()
        ) ?: throw IllegalStateException("ActivityResultLauncher cannot be null")
    }

    internal var launcher: ActivityResultLauncher<Array<String>>? = null

    internal fun updatePermissionsStatus(permissionsStatus: Map<String, Boolean>) {
        // Update all permissions with the result
        for (permission in permissionsStatus.keys) {
            mutablePermissions.firstOrNull { it.name == permission }?.apply {
                permissionsStatus[permission]?.let {
                    this.refreshPermissionStatus()
                }
            }
        }
    }
}

private fun Activity.shouldShowRationale(permission: String): Boolean {
    return ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
}

private fun Context.checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

/**
 * Find the closest Activity in a given Context.
 */
private fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}


fun Context.goToSettings(revokedPermissions: List<String> = emptyList()) {

    startActivity(
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = "package:$packageName".toUri()
        }
    )
}