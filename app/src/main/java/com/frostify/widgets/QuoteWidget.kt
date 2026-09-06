package com.frostify.widgets

import android.content.Context
import android.widget.RemoteViews
import java.util.Calendar

class QuoteWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_quote
    override val tint = GlassRenderer.Tint.PURPLE

    private val quotes = listOf(
        "Small steps every day." to "Frostify",
        "Done is better than perfect." to "Unknown",
        "Make it work, then make it beautiful." to "Unknown",
        "The best time to start was yesterday. The next best is now." to "Unknown",
        "Discipline is choosing what you want most over what you want now." to "Unknown",
        "Progress, not perfection." to "Unknown",
        "Rest, don't quit." to "Unknown"
    )

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val (text, author) = quotes[dayOfYear % quotes.size]
        views.setTextViewText(R.id.widget_quote_text, "\u201C$text\u201D")
        views.setTextViewText(R.id.widget_quote_author, "\u2014 $author")
    }
}
