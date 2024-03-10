package com.playsnyc.realistix.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
 fun RoundProgress(modifier:Modifier = Modifier,color:Color=Color.White)
{
    CircularProgressIndicator(
        modifier = modifier
            .zIndex(1f), color = color
    )
}
