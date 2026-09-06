package com.frostify.widgets

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import java.io.FileOutputStream

class PhotoPickActivity : Activity() {

    companion object {
        const val EXTRA_WIDGET_ID = "extra_widget_id"
        private const val REQUEST_PICK_IMAGE = 501
    }

    private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widgetId = intent.getIntExtra(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickIntent, REQUEST_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data?.data != null) {
            val uri: Uri = data.data!!
            try {
                val input = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(input)
                input?.close()

                val file = PhotoWidget.photoFile(this, widgetId)
                FileOutputStream(file).use { out ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
                }

                val manager = AppWidgetManager.getInstance(this)
                PhotoWidget().onUpdate(this, manager, intArrayOf(widgetId))
            } catch (e: Exception) {
                // Silently ignore; widget keeps showing its hint text.
            }
        }
        finish()
    }
}
