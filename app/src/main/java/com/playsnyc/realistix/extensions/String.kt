package com.playsnyc.realistix.extensions


import androidx.compose.ui.graphics.Color

public val String.color
    get() = Color(android.graphics.Color.parseColor(this))



public fun String.startEndIndexOf(text:String):Pair<Int,Int>
{

    val startIndex=this.indexOf(text)
    return Pair(startIndex,startIndex+(text.length))

}
//@Composable
//public fun Int.ssp(): TextUnit {
//    val fontScale = LocalContext.current.resources.configuration.fontScale
//    return   (dimensionResource(id = com.intuit.ssp.R.dimen._14ssp).value * fontScale).nonScaledSp
//}