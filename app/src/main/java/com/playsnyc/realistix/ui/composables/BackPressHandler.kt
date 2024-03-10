package com.playsnyc.realistix.ui.composables

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner

@Composable
fun BackPressHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current

    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    DisposableEffect(lifecycleOwner, dispatcherOwner, savedStateRegistryOwner) {
        dispatcherOwner?.onBackPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
