package com.playsnyc.realistix.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MyAppColors(
    val _C5BBC8: Color = Color.Unspecified,
    val _496BFF: Color = Color(color = 0xFF496BFF),
    val _1F3A67: Color = Color(color = 0xFF1F3A67),
    val _FFFFFF: Color = Color(color = 0xFFFFFFFF),
    val _FFAA00: Color = Color(color = 0xFFFFAA00),
    val _000000: Color = Color(color = 0xFF000000),
    val _4A4646: Color = Color(color = 0xFF4A4646),
    val _F1F1F1: Color = Color(color = 0xFFF1F1F1),
    val _C0C0C0: Color = Color(color = 0xFFC0C0C0),
    val _EFF2FF: Color = Color(color = 0xFFEFF2FF),
    val _AAAAAA: Color = Color(color = 0xFFAAAAAA),
    val _FFF9E7: Color = Color(color = 0xFFFFF9E7),
    val _FEC00D: Color = Color(color = 0xFFFEC00D),
    val _7A90FA: Color = Color(color = 0xFF7A90FA),
    val _EAEBF3: Color = Color(color = 0xFFEAEBF3),
    val _49454F: Color = Color(color = 0xFF49454F),
    val _CBCBCB: Color = Color(color = 0xFFCBCBCB),
    val _EFEFEF: Color = Color(color = 0xFFEFEFEF),
    val _5C5C5C: Color = Color(color = 0xFF5C5C5C),
    val _283150: Color = Color(color = 0xFF283150),
    val _F4F4F4: Color = Color(color = 0xFFF4F4F4),
    val _A0A0A0: Color = Color(color = 0xFFA0A0A0),
    val _07910C: Color = Color(color = 0xFF07910C),
    val _EC1707: Color = Color(color = 0xFFEC1707),
    val _CD0002: Color = Color(color = 0xFFCD0002),
    val _004AAD: Color = Color(color = 0xFF004AAD),
    val _03BF62: Color = Color(color = 0xFF03BF62),
) {


}

val MyColors = staticCompositionLocalOf { MyAppColors() }
val MyPerColors = MyAppColors()