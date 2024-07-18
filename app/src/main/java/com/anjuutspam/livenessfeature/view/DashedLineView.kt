package com.anjuutspam.livenessfeature.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.anjuutspam.livenessfeature.R

class DashedLineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.gray) // Warna abu-abu
        style = Paint.Style.STROKE
        strokeWidth = 5f
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f) // Garis putus-putus
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val halfHeight = height / 4f

        // Gambar garis putus-putus horizontal di tengah view
        canvas.drawLine(centerX - width / 2, centerY, centerX + width / 2, centerY, paint)

        // Gambar garis putus-putus vertikal di tengah view
        canvas.drawLine(centerX, centerY - halfHeight, centerX, centerY + halfHeight, paint)

        // Gambar badan manusia
        canvas.drawCircle(centerX, centerY - halfHeight - 125f, 250f, paint) // Kepala
        canvas.drawLine(centerX, centerY - halfHeight - 75f, centerX, centerY - halfHeight + 25f, paint) // Leher
        canvas.drawLine(centerX - 25f, centerY - halfHeight + 25f, centerX + 25f, centerY - halfHeight + 25f, paint) // Bahu
        canvas.drawLine(centerX, centerY - halfHeight + 25f, centerX, centerY - halfHeight + 75f, paint) // Badan
    }
}