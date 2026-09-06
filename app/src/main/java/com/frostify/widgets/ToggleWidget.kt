package com.frostify.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class ToggleWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_toggle
    override val tint = GlassRenderer.Tint.AMBER

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val intent = Intent(context, FlashlightToggleReceiver::class.java).apply {
            action = FlashlightToggleReceiver.ACTION_TOGGLE_FLASHLIGHT
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, appWidgetId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_toggle_icon, pendingIntent)
    }
}
