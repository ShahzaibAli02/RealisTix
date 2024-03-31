package com.playsnyc.realistix.ui.screens.contact

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview



 @Composable
 fun DiscoverScreen()
{

     val typography=MaterialTheme.typography
Column (modifier = Modifier.fillMaxSize()){
    Text(
            modifier = Modifier.fillMaxSize(),
            text = "Discover Screen",
            style = typography.titleMedium.copy(textAlign = TextAlign.Center)
    )

}

}