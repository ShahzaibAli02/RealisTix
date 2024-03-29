package com.playsnyc.realistix.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.playsnyc.realistix.ui.theme.MyColors

@Composable fun TwoColumnGridCell(
    modifier: Modifier = Modifier,
    txt1: String,
    txt2: String? = null,
    selectedText: String? = null,
    onEventClicked: (selectedEvent: String) -> Unit,
)
{

    Row(
            modifier = modifier
                .height(IntrinsicSize.Min)
                .padding(vertical = 5.dp)
    ) {

        Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable {
                        onEventClicked(txt1)
                    },
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = if (selectedText == txt1) MyColors.current.primary_color else MyColors.current._FFFFFF),
                border = BorderStroke(
                        1.dp,
                        Color.Black
                )
        ) {
            Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    text = txt1,
                    color = if (selectedText == txt1) MyColors.current._FFFFFF else MyColors.current._9A9A9A,
                    style = TextStyle()
            )
        }
        if (txt2 != null)
        {
            Spacer(modifier = Modifier.width(10.dp))
            Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clickable {
                            onEventClicked(txt2)
                        },
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(containerColor = if (selectedText == txt2) MyColors.current.primary_color else MyColors.current._FFFFFF),
                    border = BorderStroke(
                            1.dp,
                            Color.Black
                    )
            ) {
                Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                        text = txt2,
                        color = if (selectedText == txt2) MyColors.current._FFFFFF else MyColors.current._9A9A9A,
                        style = TextStyle()
                )
            }
        }

    }
}
