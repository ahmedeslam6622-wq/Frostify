package com.frostify.widgets

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.widget.RemoteViews

class MusicWidget : BaseGlassWidget() {
    override val layoutId = R.layout.widget_music
    override val tint = GlassRenderer.Tint.DARK

    companion object {
        const val ACTION_MEDIA_CONTROL = "com.frostify.widgets.ACTION_MEDIA_CONTROL"
        const val EXTRA_KEYCODE = "extra_keycode"
    }

    override fun bindContent(context: Context, views: RemoteViews, appWidgetId: Int) {
        views.setTextViewText(R.id.widget_track_title, "Media controls")

        views.setOnClickPendingIntent(R.id.widget_music_prev, controlPendingIntent(context, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 1))
        views.setOnClickPendingIntent(R.id.widget_music_play, controlPendingIntent(context, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 2))
        views.setOnClickPendingIntent(R.id.widget_music_next, controlPendingIntent(context, KeyEvent.KEYCODE_MEDIA_NEXT, 3))
    }

    private fun controlPendingIntent(context: Context, keyCode: Int, requestCode: Int): PendingIntent {
        val intent = Intent(context, MediaControlReceiver::class.java).apply {
            action = ACTION_MEDIA_CONTROL
            putExtra(EXTRA_KEYCODE, keyCode)
        }
        return PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
