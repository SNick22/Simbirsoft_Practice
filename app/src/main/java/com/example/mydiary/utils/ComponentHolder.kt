package com.example.mydiary.utils

import android.content.Context
import java.lang.ref.WeakReference

abstract class ComponentHolder<T: BaseComponent> {

    private var component: WeakReference<T>? = null

    fun get(context: Context): T =
        component?.get() ?: run {
            build(context).also(::set)
            get(context)
        }

    private fun set(component: T) {
        this.component = WeakReference(component)
    }

    protected abstract fun build(context: Context): T
}