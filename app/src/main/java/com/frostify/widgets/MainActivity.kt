package com.frostify.widgets

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = TextView(this).apply {
            text = "Frostify"
            textSize = 28f
            setTextColor(Color.WHITE)
            setPadding(48, 96, 48, 16)
        }

        val body = TextView(this).apply {
            text = "Frostify adds a set of glass-styled home screen widgets:\n\n" +
                "• Clock\n• Calendar\n• Notes\n• Weather\n• Battery\n• Music controls\n" +
                "• Photo frame\n• Calculator\n• Habit tracker\n• Quote of the day\n• Flashlight toggle\n\n" +
                "To add one: long-press your home screen → Widgets → Frostify, " +
                "then drag any widget onto your screen.\n\n" +
                "Some widgets (Notes, Photo, Habit tracker) can be tapped to edit their content directly."
            textSize = 15f
            setTextColor(Color.parseColor("#DDFFFFFF"))
            setPadding(48, 0, 48, 48)
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.TOP
            setBackgroundColor(Color.parseColor("#0F1115"))
            addView(title)
            addView(body)
        }

        setContentView(ScrollView(this).apply { addView(layout) })
    }
}
