package com.daniebeler.pfpixelix.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberPrefIntState(key: String, default: Int): MutableIntState {
    val context = LocalKmpContext.current
    return remember { PrefMutableIntState(context, key, default) }
}

@Composable
fun rememberPrefBoolState(key: String, default: Boolean): MutableState<Boolean> {
    val context = LocalKmpContext.current
    return remember { PrefMutableBooleanState(context, key, default) }
}

private class PrefMutableBooleanState(
    val context: KmpContext,
    val key: String,
    default: Boolean,
) : MutableState<Boolean> {

    private val delegate = mutableStateOf(context.pref.getBoolean(key, default))

    override var value: Boolean
        get() = delegate.value
        set(value) {
            delegate.value = value
            context.pref.putBoolean(key, value)
        }

    override fun component1(): Boolean {
        return delegate.component1()
    }

    override fun component2(): (Boolean) -> Unit {
        return delegate.component2()
    }

}

private class PrefMutableIntState(
    val context: KmpContext,
    val key: String,
    default: Int,
) : MutableIntState {

    private val delegate = mutableIntStateOf(context.pref.getInt(key, default))

    override var intValue: Int
        get() {
            return delegate.intValue
        }
        set(value) {
            delegate.intValue = value
            context.pref.putInt(key, value)
        }

    override fun component1(): Int {
        return delegate.component1()
    }

    override fun component2(): (Int) -> Unit {
        return delegate.component2()
    }
}