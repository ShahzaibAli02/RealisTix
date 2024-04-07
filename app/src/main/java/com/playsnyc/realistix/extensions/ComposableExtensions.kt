package com.playsnyc.realistix.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//var mLastClickTime: Long = 0
//fun shouldAllowClick(delay: Long = 600): Boolean {
//    if (SystemClock.elapsedRealtime() - mLastClickTime < delay) {
//        return false;
//    }
//    mLastClickTime = SystemClock.elapsedRealtime()
//    return true
//}


@Composable
@Stable
public fun Modifier.oneClickable(
    delay: Long = 500,
    enabled: Boolean = true,
    onClick: () -> Unit
) = this.then(
    clickable(
        enabled = enabled, onClick = {
            if (shouldAllowClick(delay)) {
                onClick()
            }
        }
    )
)
@Composable
@Stable
public fun Modifier.roundClickable(
    delay: Long = 1000,
    enabled: Boolean = true,
    onClick: () -> Unit
) = this.then(
    clickable(
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = false), onClick = {
            if (shouldAllowClick(delay)) {
                onClick()
            }
        }
    )
)

val Int.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp


val Double.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp



val Float.nonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp


@Composable
fun Int.toSp(): TextUnit {
    return dimensionResource(id = this).sp()
}
@Composable
fun Int.toDp(): Dp {
    return dimensionResource(id = this).value.dp
}


@Composable
private fun Dp.sp(): TextUnit {
    val dp=this
    return with(LocalDensity.current) { dp.toSp() }
}
@Composable
fun dpToSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

val TextUnit.nSp
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp
