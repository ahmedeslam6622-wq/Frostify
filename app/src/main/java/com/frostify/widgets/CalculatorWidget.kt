package com.frostify.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class CalculatorWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_calculator
    override val tint = GlassRenderer.Tint.DARK

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        // Launch whatever calculator app is registered on the device.
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_CALCULATOR)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, appWidgetId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_calc_label, pendingIntent)
    }
}
