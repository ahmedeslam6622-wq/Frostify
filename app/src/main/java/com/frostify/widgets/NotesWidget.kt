package com.frostify.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class NotesWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_notes
    override val tint = GlassRenderer.Tint.AMBER

    companion object {
        const val PREFS = "frostify_notes"
        fun keyFor(id: Int) = "note_$id"
    }

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val saved = prefs.getString(keyFor(appWidgetId), null)
        views.setTextViewText(R.id.widget_note_text, saved ?: "Tap to edit this note")

        val intent = Intent(context, NoteEditActivity::class.java).apply {
            putExtra(NoteEditActivity.EXTRA_WIDGET_ID, appWidgetId)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, appWidgetId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_note_text, pendingIntent)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        for (id in appWidgetIds) editor.remove(keyFor(id))
        editor.apply()
    }
}
