@file:Suppress("DEPRECATION")

package com.hcq.android.kotlin.extensions.app

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

fun Activity.addFragment(fragment: Fragment, tag: String? = null) {
    fragmentManager.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
}

fun FragmentActivity.addFragment(fragment: androidx.fragment.app.Fragment, tag: String? = null) {
    supportFragmentManager.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
}

fun ActivityResultCaller.registerForActivityResult(callback: ActivityResultCallback<ActivityResult>): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
}

fun ActivityResultCaller.registerForPermission(callback: ActivityResultCallback<Boolean>): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)
}

fun ActivityResultCaller.registerForPermissions(callback: ActivityResultCallback<Map<String, Boolean>>): ActivityResultLauncher<Array<String>> {
    return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), callback)
}