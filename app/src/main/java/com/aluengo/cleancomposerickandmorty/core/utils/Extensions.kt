package com.aluengo.cleancomposerickandmorty.core.utils

import timber.log.Timber
fun Any.logd(message: String?){
    Timber.tag(this.javaClass.simpleName).d(message)
}

fun Any.loge(message: String?){
    Timber.tag(this.javaClass.simpleName).e(message)
}


fun Any.logw(message: String?){
    Timber.tag(this.javaClass.simpleName).w(message)
}

inline fun <T1 : Any, T2 : Any, R : Any> whenNotNull(first: T1?, second: T2?, block: (T1, T2) -> R?): R? {
    return if (first != null && second != null) block(first, second) else null
}

fun String.extractPageNumber(): Int {
    val regex = """page=(\d+)""".toRegex()
    val matchResult = regex.find(this)
    return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 1
}