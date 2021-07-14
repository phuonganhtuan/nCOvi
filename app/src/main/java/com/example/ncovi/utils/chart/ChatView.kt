package com.example.ncovi.utils.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class ChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var itemDistance = 0f
    private var data = mutableListOf<Float>()
    private var chartColor = Color.BLUE
    private var itemDotSize = 0.1
    private var dots = mutableListOf<Dot>()
    private var controls = mutableListOf<Dot>()
    private var tempDots = mutableListOf<Dot>()
    private var chartHeight = 0f
    private val path = Path()
    private var unit = 0

    private val paint by lazy {
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = Color.BLUE
        p.strokeWidth = 12f
        p.style = Paint.Style.STROKE
        p
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (data.size >= 2) {
            canvas?.drawLine(dots[0].x, dots[0].y, controls[0].x, controls[0].y, paint)
            for (i in 0 until controls.size - 1) {
                path.moveTo(controls[i].x, controls[i].y)
                path.quadTo(dots[i + 1].x, dots[i + 1].y, controls[i + 1].x, controls[i + 1].y)
                canvas?.drawPath(path, paint)
            }
            canvas?.drawLine(
                controls.last().x,
                controls.last().y,
                dots.last().x,
                dots.last().y,
                paint
            )
        }
    }

    fun displayChart(
        data: List<Float>,
        color: Int,
        chartHeight: Int,
        maxNum: Int
    ) {
        path.reset()
        this.unit = maxNum
        this.data = data.toMutableList()
        this.chartColor = color
        paint.color = color
        itemDistance = width.toFloat() / (data.size - 1)
        this.chartHeight = height.toFloat()
        convertDataToDots()
        this.invalidate()
    }

    private fun convertDataToDots() {
        dots.clear()
        controls.clear()
        for (i in 0 until data.size) {
            val xOffset = i * itemDistance + i * itemDotSize
            val yOffset = chartHeight - (chartHeight * data[i] / unit)
            dots.add(Dot(x = xOffset.toFloat(), y = yOffset))
        }
        for (i in 0 until data.size - 1) {
            val xOffset = i * itemDistance + 0.5 * itemDistance
            val yOffset = (dots[i].y + dots[i + 1].y) / 2
            controls.add(Dot(x = xOffset.toFloat(), y = yOffset))
        }
        for (i in 0 until data.size - 1) {
            tempDots.add(dots[i])
            tempDots.add(controls[i])
        }
        tempDots.add(dots.last())
    }
}

data class Dot(
    var x: Float = 0f,
    var y: Float = 0f
)
