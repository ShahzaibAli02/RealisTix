package com.playsnyc.realistix.utils
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.playsnyc.realistix.R

object MyFonts {
    val montserrat = FontFamily(
        Font(R.font.montserrat_medium),
        Font(R.font.montserrat_bold, FontWeight.Bold),
    )
    @Composable
    fun poppins() = FontFamily(
        Font("Poppins-Bold.ttf",LocalContext.current.assets,FontWeight.Bold),
        Font("Poppins-Thin.ttf",LocalContext.current.assets,FontWeight.Thin),
        Font("poppins_medium.ttf",LocalContext.current.assets,FontWeight.Normal)

    )


    @Composable
    fun inter() = FontFamily(
        Font("Inter.ttf",LocalContext.current.assets),
        Font("Inter-Medium.ttf",LocalContext.current.assets,FontWeight.Medium),
        Font("Inter-ExtraLight.ttf",LocalContext.current.assets,FontWeight.Light),
        Font("Inter-ExtraBold.ttf",LocalContext.current.assets,FontWeight.ExtraBold),
        Font("Inter-Bold.ttf",LocalContext.current.assets,FontWeight.Bold)
    )
    // Define more fonts as needed
}