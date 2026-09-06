package com.frostify.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.RemoteViews
import java.io.File

class PhotoWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_photo
    override val tint = GlassRenderer.Tint.LIGHT

    companion object {
        fun photoFile(context: Context, appWidgetId: Int): File =
            File(context.filesDir, "widget_photo_$appWidgetId.jpg")
    }

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val file = photoFile(context, appWidgetId)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (bitmap != null) {
                views.setImageViewBitmap(R.id.widget_photo_image, bitmap)
                views.setViewVisibility(R.id.widget_photo_hint, android.view.View.GONE)
            }
        } else {
            views.setViewVisibility(R.id.widget_photo_hint, android.view.View.VISIBLE)
        }

        val intent = Intent(context, PhotoPickActivity::class.java).apply {
            putExtra(PhotoPickActivity.EXTRA_WIDGET_ID, appWidgetId)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, appWidgetId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_photo_image, pendingIntent)
        views.setOnClickPendingIntent(R.id.widget_photo_hint, pendingIntent)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (id in appWidgetIds) photoFile(context, id).delete()
    }
}
