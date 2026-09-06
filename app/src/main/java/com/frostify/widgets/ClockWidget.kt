package com.frostify.widgets

import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_clock
    override val tint = GlassRenderer.Tint.BLUE

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val now = Date()
        views.setTextViewText(R.id.widget_time, SimpleDateFormat("HH:mm", Locale.getDefault()).format(now))
        views.setTextViewText(R.id.widget_date, SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(now))
    }
}
