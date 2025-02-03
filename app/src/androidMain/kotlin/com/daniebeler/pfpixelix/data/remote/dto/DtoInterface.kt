package com.daniebeler.pfpixelix.data.remote.dto

interface DtoInterface<T> {
    fun toModel(): T
}