package com.playsnyc.realistix.data.model

import androidx.compose.ui.graphics.Color
import com.playsnyc.realistix.ui.theme.MyPerColors

data class HeaderMessage(val message:String, val backGroundColor:Color= MyPerColors._004AAD, val textColor:Color=Color.White)
{
    companion object
}

fun HeaderMessage.Companion.Error(message:String):HeaderMessage
{
    return HeaderMessage(backGroundColor=Color.Red, message = message)
}
fun HeaderMessage.Companion.Success(message:String):HeaderMessage
{
    return HeaderMessage(backGroundColor= MyPerColors._03BF62, message = message)
}