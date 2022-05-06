package com.mo.mutual.ability

import com.mo.mutual.Mutual

interface IDebug {

    var debug: Int

    fun debug(color: Int): Mutual
}