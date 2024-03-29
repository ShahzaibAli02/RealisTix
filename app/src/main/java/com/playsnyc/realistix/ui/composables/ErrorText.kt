package com.playsnyc.realistix.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playsnyc.realistix.utils.MyFonts



@Composable
fun ErrorText(modifier:Modifier = Modifier,  textAlign:TextAlign = TextAlign.End,text:String)
{
    Text(modifier = modifier.fillMaxWidth().padding(10.dp),
            textAlign = textAlign,
            fontSize = 15.sp,
            color = Color.Red,
            text = text,
            fontFamily = MyFonts.poppins())
}