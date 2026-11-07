package com.picke.presentation.update

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallException
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallErrorCode
import com.google.android.play.core.install.model.UpdateAvailability
import io.sentry.Sentry
import com.google.android.play.core.install.model.ActivityResult as UpdateResult

class InAppUpdateManager(private val activity: ComponentActivity) {

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(activity) }

    private val updateLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result -> handleUpdateResult(result) }

    fun checkForUpdate() {
        requestUpdateInfo { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startImmediateUpdate(info)
            }
        }
    }

    fun resumeUpdate() {
        requestUpdateInfo { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startImmediateUpdate(info)
            }
        }
    }

    private fun requestUpdateInfo(onSuccess: (AppUpdateInfo) -> Unit) {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { info -> onSuccess(info) }
            .addOnFailureListener { e -> reportFailure(e) }
    }

    private fun startImmediateUpdate(info: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            info,
            updateLauncher,
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
        )
    }

    private fun handleUpdateResult(result: ActivityResult) {
        when (result.resultCode) {
            Activity.RESULT_CANCELED -> activity.finish()
            UpdateResult.RESULT_IN_APP_UPDATE_FAILED -> Sentry.captureMessage("In-app update failed")
        }
    }

    private fun reportFailure(e: Exception) {
        if (e is InstallException && e.errorCode in EXPECTED_ERROR_CODES) return
        Sentry.captureException(e)
    }

    private companion object {
        val EXPECTED_ERROR_CODES = setOf(
            InstallErrorCode.ERROR_APP_NOT_OWNED,
            InstallErrorCode.ERROR_API_NOT_AVAILABLE,
            InstallErrorCode.ERROR_PLAY_STORE_NOT_FOUND
        )
    }
}