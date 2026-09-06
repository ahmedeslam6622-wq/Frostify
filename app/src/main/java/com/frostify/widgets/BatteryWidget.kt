package com.frostify.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.RemoteViews

class BatteryWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_battery
    override val tint = GlassRenderer.Tint.ROSE

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val pct = if (level >= 0 && scale > 0) (level * 100 / scale) else 0

        val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        views.setTextViewText(R.id.widget_battery_pct, "$pct%")
        views.setTextViewText(R.id.widget_battery_status, if (isCharging) "Charging" else "Battery")
    }

    override fun onEnabled(context: Context) {
        // Refresh once immediately when the first instance is placed.
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(android.content.ComponentName(context, BatteryWidget::class.java))
        for (id in ids) updateWidget(context, manager, id)
    }
}
