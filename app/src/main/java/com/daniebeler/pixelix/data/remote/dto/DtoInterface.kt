package com.daniebeler.pixelix.data.remote.dto

interface DtoInterface<T> {
    fun toModel(): T
}