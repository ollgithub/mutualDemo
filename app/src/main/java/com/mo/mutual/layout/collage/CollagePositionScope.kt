package com.mo.mutual.layout.collage

import com.mo.mutual.layout.PositionScope

class CollagePositionScope : PositionScope() {

    var newLine = false

    /**
     * 此mutual要占据新的一行或一列
     */
    fun newLine() {

        newLine = true
    }

    fun clear() {
        newLine = false
    }


}