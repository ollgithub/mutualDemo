package com.mo.mutual.element

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.mo.base.XY
import com.mo.d
import com.mo.i
import com.mo.mutual.Frame
import com.mo.mutual.layout.LayoutScope


/**
 * @param string
 * @param size 小于0时将自动为mutual大小计算合适的文字大小
 */
fun LayoutScope.Text(
    string: String,
    size: Float = 60f,
    typeface: Typeface? = null
): TextMutual = declare { justUpdate ->

    contentChange(
        string,
        size,
        typeface
    )
}

class TextMutual : ShapeMutual() {

    companion object {
        val MEASURE_PAINT = Paint()
        const val DEFAULT_FONT_SIZE = 60f
    }

    var string: String? = null
    var fontSize = DEFAULT_FONT_SIZE
    var color = Color.BLACK
    var typeface: Typeface? = null

    fun contentChange(
        string: String,
        size: Float = DEFAULT_FONT_SIZE,
        typeface: Typeface? = null
    ) {
        val tempSize = XY().set(this.currentSize)
        this.string = string
        this.fontSize = size
        this.typeface = typeface
        measure()
        if (tempSize != currentSize) {
            d("hhhh $string $tempSize  ${currentSize}")
            relayout()
        }
    }

    private var size_h = 0f

    override fun onDraw(frame: Frame) {
        val string = string ?: return
        if (fontSize == 0f) return
        val drawSize = if (fontSize < 0) {
            if (size_h <= 0) {
                val measurePaint = MEASURE_PAINT
                measurePaint.textSize = fontSize
                val h = measurePaint.descent() - measurePaint.ascent()
                size_h = this.fontSize / h
            }
            size_h * frame.currSpaceInfo.h
        } else fontSize
        frame.drawString(string, drawSize, typeface)
    }

    private fun measure() {
        val string = string ?: let {
            currentSize.set(0, 0)
            return
        }
        val measureSize = if (fontSize >= 0) fontSize else DEFAULT_FONT_SIZE
        val measurePaint = MEASURE_PAINT
        measurePaint.textSize = measureSize
        val h = measurePaint.descent() - measurePaint.ascent()
        val w = measurePaint.measureText(string)
        size_h = measureSize / h
        currentSize.set(w.i, h.i)
    }

}