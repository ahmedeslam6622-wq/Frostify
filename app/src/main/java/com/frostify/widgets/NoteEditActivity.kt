package com.frostify.widgets

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

class NoteEditActivity : Activity() {

    companion object {
        const val EXTRA_WIDGET_ID = "extra_widget_id"
    }

    private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        widgetId = intent.getIntExtra(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val prefs = getSharedPreferences(NotesWidget.PREFS, Context.MODE_PRIVATE)
        val existing = prefs.getString(NotesWidget.keyFor(widgetId), "")

        val editText = EditText(this).apply {
            setText(existing)
            hint = "Write your note…"
            setPadding(32, 32, 32, 32)
        }

        val saveButton = Button(this).apply {
            text = "Save"
            setOnClickListener {
                prefs.edit().putString(NotesWidget.keyFor(widgetId), editText.text.toString()).apply()
                val manager = AppWidgetManager.getInstance(this@NoteEditActivity)
                val provider = NotesWidget()
                provider.onUpdate(this@NoteEditActivity, manager, intArrayOf(widgetId))
                finish()
            }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
            addView(editText)
            addView(saveButton)
        }

        setContentView(layout)
    }
}
