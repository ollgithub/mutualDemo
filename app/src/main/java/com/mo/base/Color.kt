package com.mo.base

data class ARGB(
    var alpha: Char,
    var red: Char,
    var green: Char,
    var blue: Char
) {
    fun color(): Color {
        return Color(
            (alpha.code shl 24) or (red.code shl 16) or (green.code shl 8) or (blue.code)
        )
    }
}

data class Color(
    val value: Int = WHITE.value
) {

    companion object {
        val TRANSPARENT = Color(0)
        val ORANGE = Color(0xFFFFA500.toInt())
        val DARK_ORANGE = Color(0xFFFF8C00.toInt())
        val CYAN = Color(0xFF00FFFF.toInt())
        val BLACK = Color(0xFF000000.toInt())
        val DKGRAY = Color(0xFF444444.toInt())
        val GRAY = Color(0xFF888888.toInt())
        val LTGRAY = Color(0xFFCCCCCC.toInt())
        val WHITE = Color(0xFFFFFFFF.toInt())
        val RED = Color(0xFFFF0000.toInt())
        val GREEN = Color(0xFF00FF00.toInt())
        val BLUE = Color(0xFF0000FF.toInt())
        val YELLOW = Color(0xFFFFFF00.toInt())
        val MAGENTA = Color(0xFF00FFFF.toInt())
        val OR = Color(0xFFF26522.toInt())
    }

    fun alpha(alpha: Float): Color {
        return Color((value and 0xFFFFFF) or ((alpha * 255).toInt() shl 24))
    }

    fun argb(): ARGB {
        return ARGB(
            (value shr 24).toChar(),
            ((value and 0x00FF0000) shr 16).toChar(),
            ((value and 0x0000FF00) shr 8).toChar(),
            (value and 0x000000FF).toChar(),
        )
    }
}
