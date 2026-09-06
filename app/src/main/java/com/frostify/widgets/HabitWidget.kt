package com.frostify.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_habit
    override val tint = GlassRenderer.Tint.MINT

    companion object {
        const val PREFS = "frostify_habit"
        const val ACTION_CHECK_IN = "com.frostify.widgets.ACTION_HABIT_CHECK_IN"
        const val EXTRA_WIDGET_ID = "extra_widget_id"
        fun streakKey(id: Int) = "streak_$id"
        fun lastDateKey(id: Int) = "last_date_$id"
        private val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        fun todayString(): String = dayFormat.format(Date())
    }

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val streak = prefs.getInt(streakKey(appWidgetId), 0)
        views.setTextViewText(R.id.widget_habit_streak, "🔥 $streak day streak")

        val intent = Intent(context, HabitCheckInReceiver::class.java).apply {
            action = ACTION_CHECK_IN
            putExtra(EXTRA_WIDGET_ID, appWidgetId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, appWidgetId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_habit_check, pendingIntent)
    }
}
