package com.mo.base

enum class UpdateType {
    INSERT, REMOVE, MOVE, UPDATE
}

data class ListUpdateInfo(
    val type: UpdateType,
    val from: Int,
    val to: Int,
)

