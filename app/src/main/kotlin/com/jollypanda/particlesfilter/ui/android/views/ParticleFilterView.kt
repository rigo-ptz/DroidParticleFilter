package com.jollypanda.particlesfilter.ui.android.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatDrawableManager
import android.util.AttributeSet
import android.view.View
import com.jollypanda.particlesfilter.R
import com.jollypanda.particlesfilter.data.Particle
import com.jollypanda.particlesfilter.data.Robot


/**
 * @author Yamushev Igor
 * @since  13.05.17
 */
class ParticleFilterView @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    var icAndroid: Drawable = AppCompatDrawableManager.get().getDrawable(context, R.drawable.ic_android)
    var icBitmap: Bitmap
    val bitmapPaint: Paint = Paint()
    val particlesPaint = Paint()

    private var robotX: Float = 0f
    private var robotY: Float = 0f
    private var ovalsList: MutableList<RectF> = mutableListOf()

    init {
        bitmapPaint.isAntiAlias = true
        bitmapPaint.style = Paint.Style.FILL
        icBitmap = getBitmapFromDrawable(icAndroid)

        particlesPaint.isAntiAlias = true
        particlesPaint.color = Color.RED
        particlesPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawBitmap(icBitmap, robotX - icBitmap.width / 2, robotY - icBitmap.height / 2, bitmapPaint)
        ovalsList.forEach {
            canvas?.drawOval(it, particlesPaint)
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bitmap: Bitmap
        if (drawable is BitmapDrawable)
            if (drawable.bitmap != null)
                return drawable.bitmap

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        else
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun updateDroid(robot: Robot, particlesList: MutableList<Particle>) {
        robotX = robot.x.toFloat()
        robotY = robot.y.toFloat()
        ovalsList.clear()
        particlesList.forEach {
            ovalsList.add(RectF(it.x.toFloat() - 10, it.y.toFloat() - 10, it.x.toFloat() + 10, it.y.toFloat() + 10))
        }
        invalidate()
    }

}