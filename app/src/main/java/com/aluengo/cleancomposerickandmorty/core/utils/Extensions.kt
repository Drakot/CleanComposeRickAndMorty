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

inline fun <T1 : Any, T2 : Any, R : Any> whenNotNull(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}