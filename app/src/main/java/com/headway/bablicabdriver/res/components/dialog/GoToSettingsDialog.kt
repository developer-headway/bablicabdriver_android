package com.headway.bablicabdriver.res.components.dialog

import android.app.AlertDialog
import android.content.Context
import com.headway.bablicabdriver.R


fun goToSettingsDialog(
    context : Context,
    onGoToSettings : () -> Unit
){
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setCancelable(true)
    dialogBuilder.setTitle(context.getString(R.string.alert))
    dialogBuilder.setMessage(context.getString(R.string.this_feature_requires_few_permissions_kindly_go_to_settings_and_grant_them))
    dialogBuilder.setPositiveButton(
        context.getString(R.string.go_to_settings)
    ) { dialog, _ ->
        dialog.cancel()
        onGoToSettings()
    }
    dialogBuilder.setNegativeButton(
        context.getString(R.string.cancel)
    ) { dialog, _ ->
        dialog.cancel()
    }

    dialogBuilder.create()
    dialogBuilder.show()
}