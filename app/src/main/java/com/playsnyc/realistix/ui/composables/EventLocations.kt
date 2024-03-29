package com.playsnyc.realistix.ui.composables
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.playsnyc.realistix.ui.theme.MyColors

@Composable fun EventLocations(
    modifier: Modifier = Modifier,
    onEventClicked: (selectedEvent: String) -> Unit,
)
{

    var selectedEvent by rememberSaveable { mutableStateOf("") }
    val location1 = "Venue"
    val location2 = "Online"
    val location3 = "To be announced"
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
                        selectedEvent = location1
                        onEventClicked(selectedEvent)
                    },
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = if (selectedEvent == location1) MyColors.current.primary_color else MyColors.current._FFFFFF),
                border = BorderStroke(
                        1.dp,
                        Color.Black
                )
        ) {
            Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    text = location1,
                    color = if (selectedEvent == location1) MyColors.current._FFFFFF else MyColors.current._9A9A9A,
                    style = TextStyle()
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable {
                        selectedEvent = location2
                        onEventClicked(selectedEvent)
                    },
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = if (selectedEvent == location2) MyColors.current.primary_color else MyColors.current._FFFFFF),
                border = BorderStroke(
                        1.dp,
                        Color.Black
                )
        ) {
            Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    text = location2,
                    color = if (selectedEvent == location2) MyColors.current._FFFFFF else MyColors.current._9A9A9A,
                    style = TextStyle()
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clickable {
                        selectedEvent = location3
                        onEventClicked(selectedEvent)
                    },
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = if (selectedEvent == location3) MyColors.current.primary_color else MyColors.current._FFFFFF),
                border = BorderStroke(
                        1.dp,
                        Color.Black
                )
        ) {
            Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    text = location3,
                    color = if (selectedEvent == location3) MyColors.current._FFFFFF else MyColors.current._9A9A9A,
                    style = TextStyle()
            )
        }

    }
}