package com.frostify.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

/**
 * Shared plumbing for every Frostify widget: reads the current widget size,
 * builds a glass panel bitmap to fit, and applies it as the background of
 * the RemoteViews' root ImageView (R.id.glass_background) before subclasses
 * fill in their own content.
 */
abstract class BaseGlassWidget : AppWidgetProvider() {

    abstract val layoutId: Int
    open val tint: GlassRenderer.Tint = GlassRenderer.Tint.LIGHT

    /** Subclasses populate text/content views here. */
    abstract fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (id in appWidgetIds) {
            updateWidget(context, appWidgetManager, id)
        }
    }

    protected fun updateWidget(context: Context, manager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, layoutId)

        val options = manager.getAppWidgetOptions(appWidgetId)
        val minWidthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 110)
        val minHeightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 110)

        val density = context.resources.displayMetrics.density
        val widthPx = (minWidthDp * density).toInt().coerceAtLeast(50)
        val heightPx = (minHeightDp * density).toInt().coerceAtLeast(50)
        val cornerPx = 28f * density

        val glassBitmap = GlassRenderer.buildGlassPanel(context, widthPx, heightPx, cornerPx, tint)
        views.setImageViewBitmap(R.id.glass_background, glassBitmap)

        bindContent(context, views, appWidgetId)

        manager.updateAppWidget(appWidgetId, views)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: android.os.Bundle?
    ) {
        updateWidget(context, appWidgetManager, appWidgetId)
    }
}
