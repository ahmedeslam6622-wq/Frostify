package com.frostify.widgets

import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_calendar
    override val tint = GlassRenderer.Tint.PURPLE

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val cal = Calendar.getInstance()
        views.setTextViewText(R.id.widget_month, SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.time).uppercase())
        views.setTextViewText(R.id.widget_day_number, cal.get(Calendar.DAY_OF_MONTH).toString())
        views.setTextViewText(R.id.widget_weekday, SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.time))
    }
}
