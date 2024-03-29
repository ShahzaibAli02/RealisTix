package com.playsnyc.realistix.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.playsnyc.realistix.ui.theme.RealisTixTheme


@Composable fun DialogLoadingTextView(
    modifier: Modifier = Modifier,
    progress: Float = -1.0f,
    message: String = "",
    extraContent: (@Composable ColumnScope.() -> Unit)? = null,
)
{
    val typography = MaterialTheme.typography
    Dialog(onDismissRequest = {

    }) {
        Card(modifier = modifier.padding(10.dp)) {
            Column(modifier = modifier.padding(5.dp)) {
                if (message.isNotBlank()) Text(
                        text = message,
                        style = typography.titleMedium
                )
                if (progress == -1.0f)
                {
                    LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .padding(2.dp),
                            strokeCap = StrokeCap.Butt
                    )
                } else
                {
                    LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .padding(2.dp),
                            strokeCap = StrokeCap.Butt
                    )
                }
                extraContent?.invoke(this)
            }
        }

    }

}

@Preview(
        widthDp = 400,
        heightDp = 400,
        showBackground = true
) @Composable fun LoadingTextViewPrev()
{
    RealisTixTheme {
        DialogLoadingTextView(modifier = Modifier.fillMaxSize()) {
            Button(onClick = { /*TODO*/ }) {
                Text(
                        text = "Cancel",
                )

            }
        }
    }
}