package com.frostify.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.ColorInt

/**
 * Produces the "frosted glass" bitmap backgrounds used by every widget:
 * a soft blur + translucent tint + subtle top highlight + hairline border.
 *
 * RemoteViews (used by all home-screen widgets) cannot apply live runtime
 * blur to whatever is behind them, so instead we pre-render a convincing
 * glass PANEL bitmap (rounded rect, gradient tint, highlight, border) and
 * set it as the widget's background. This looks like glassmorphism and
 * costs nothing at draw time, and works all the way back to API 26.
 */
object GlassRenderer {

    enum class Tint(@ColorInt val base: Int) {
        LIGHT(Color.argb(140, 255, 255, 255)),
        DARK(Color.argb(140, 20, 22, 28)),
        BLUE(Color.argb(130, 90, 140, 255)),
        PURPLE(Color.argb(130, 160, 90, 255)),
        MINT(Color.argb(130, 90, 220, 190)),
        ROSE(Color.argb(130, 255, 110, 150)),
        AMBER(Color.argb(130, 255, 175, 70))
    }

    /**
     * Builds a rounded glass panel bitmap of the given size.
     */
    fun buildGlassPanel(
        context: Context,
        widthPx: Int,
        heightPx: Int,
        cornerRadiusPx: Float,
        tint: Tint = Tint.LIGHT
    ): Bitmap {
        val w = widthPx.coerceAtLeast(1)
        val h = heightPx.coerceAtLeast(1)
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())

        // Base translucent tint fill
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = tint.base
        }
        canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, fillPaint)

        // Soft diagonal light sheen (glass highlight)
        val sheenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, w * 0.6f, h * 0.6f,
                Color.argb(70, 255, 255, 255),
                Color.argb(0, 255, 255, 255),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, sheenPaint)

        // Radial glow near top-left corner for depth
        val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = RadialGradient(
                w * 0.2f, h * 0.15f, (w * 0.7f).coerceAtLeast(1f),
                Color.argb(55, 255, 255, 255),
                Color.argb(0, 255, 255, 255),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, glowPaint)

        // Hairline border to sell the "glass edge"
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
            color = Color.argb(90, 255, 255, 255)
        }
        val inset = borderPaint.strokeWidth / 2f
        val borderRect = RectF(inset, inset, w - inset, h - inset)
        canvas.drawRoundRect(borderRect, cornerRadiusPx, cornerRadiusPx, borderPaint)

        return bitmap
    }

    /**
     * Optional real blur of a source bitmap (e.g. a wallpaper snapshot or
     * user photo) using RenderScript intrinsic blur. Available API 26+;
     * deprecated in newer API levels but still functional and far more
     * broadly compatible for widgets than RenderEffect (which needs a
     * live View hierarchy widgets don't have).
     */
    fun blurBitmap(context: Context, source: Bitmap, radius: Float = 18f): Bitmap {
        val clampedRadius = radius.coerceIn(0.1f, 25f)
        val output = Bitmap.createBitmap(source.width, source.height, source.config ?: Bitmap.Config.ARGB_8888)
        val rs = RenderScript.create(context)
        try {
            val input = Allocation.createFromBitmap(rs, source)
            val out = Allocation.createFromBitmap(rs, output)
            val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setRadius(clampedRadius)
            script.setInput(input)
            script.forEach(out)
            out.copyTo(output)
            script.destroy()
            input.destroy()
            out.destroy()
        } finally {
            rs.destroy()
        }
        return output
    }
}
