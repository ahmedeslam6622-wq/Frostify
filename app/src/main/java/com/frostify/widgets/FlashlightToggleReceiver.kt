package com.frostify.widgets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager

class FlashlightToggleReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_TOGGLE_FLASHLIGHT = "com.frostify.widgets.ACTION_TOGGLE_FLASHLIGHT"
        private var isOn = false
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_TOGGLE_FLASHLIGHT) return
        try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager.getCameraCharacteristics(id)
                    .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            } ?: return
            isOn = !isOn
            cameraManager.setTorchMode(cameraId, isOn)
        } catch (e: Exception) {
            // Device may not support torch mode; fail silently.
        }
    }
}
