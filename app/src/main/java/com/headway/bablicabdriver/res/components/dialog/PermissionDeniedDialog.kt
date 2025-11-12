package com.headway.bablicabdriver.res.components.dialog

import android.app.AlertDialog
import android.content.Context
import com.headway.bablicabdriver.R

fun permissionDeniedDialog(
    context : Context,
    onGrant : () -> Unit
){
    val dialogBuilder = AlertDialog.Builder(context)
    dialogBuilder.setCancelable(true)
    dialogBuilder.setMessage(context.getString(R.string.this_feature_requires_few_permissions_kindly_grant_the_permissions_to_enjoy_app_s_feature_properly))
    dialogBuilder.setPositiveButton(
        context.getString(R.string.ok)
    ) { dialog, _ ->
        dialog.cancel()
        onGrant()
    }
    dialogBuilder.setNegativeButton(
        context.getString(R.string.cancel)
    ) { dialog, _ ->
        dialog.cancel()
    }

    dialogBuilder.create()
    dialogBuilder.show()
}