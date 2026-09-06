package com.frostify.widgets

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HabitCheckInReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != HabitWidget.ACTION_CHECK_IN) return
        val widgetId = intent.getIntExtra(HabitWidget.EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) return

        val prefs = context.getSharedPreferences(HabitWidget.PREFS, Context.MODE_PRIVATE)
        val today = HabitWidget.todayString()
        val lastDate = prefs.getString(HabitWidget.lastDateKey(widgetId), null)

        if (lastDate != today) {
            val currentStreak = prefs.getInt(HabitWidget.streakKey(widgetId), 0)
            prefs.edit()
                .putInt(HabitWidget.streakKey(widgetId), currentStreak + 1)
                .putString(HabitWidget.lastDateKey(widgetId), today)
                .apply()
        }

        val manager = AppWidgetManager.getInstance(context)
        HabitWidget().onUpdate(context, manager, intArrayOf(widgetId))
    }
}
