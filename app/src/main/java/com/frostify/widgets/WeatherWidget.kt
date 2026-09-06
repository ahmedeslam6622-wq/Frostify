package com.frostify.widgets

import android.content.Context
import android.widget.RemoteViews

/**
 * Placeholder weather widget. No API key is bundled (none should ever be
 * committed to a public repo). To make this live: sign up for a free key
 * at a provider like Open-Meteo (no key needed) or OpenWeatherMap, fetch
 * in a WorkManager periodic job, and update these text views from there.
 */
class WeatherWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_weather
    override val tint = GlassRenderer.Tint.MINT

    companion object {
        const val PREFS = "frostify_weather"
        const val KEY_TEMP = "last_temp"
        const val KEY_CONDITION = "last_condition"
    }

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val temp = prefs.getString(KEY_TEMP, "--°")
        val condition = prefs.getString(KEY_CONDITION, "Open Frostify to set location")
        views.setTextViewText(R.id.widget_temp, temp)
        views.setTextViewText(R.id.widget_condition, condition)
    }
}
