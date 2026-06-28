package com.example.petbeats.core.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

object PermissionHelper {
    fun hasLocationPermission(context: Context): Boolean {
        val isFineGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val isCoarseGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return isFineGranted || isCoarseGranted
    }
}