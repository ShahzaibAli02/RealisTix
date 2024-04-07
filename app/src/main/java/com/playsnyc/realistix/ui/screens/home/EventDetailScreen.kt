package com.playsnyc.realistix.ui.screens.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.DateTimeFormmater
import org.koin.androidx.compose.koinViewModel


var isMyEvent: Boolean = false
var alreadyBooked: Boolean = false
@OptIn(ExperimentalMaterialApi::class) @Composable fun EventDetailScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
)
{

    val event: Event = viewModel.currentEvent ?: Event()
    val typography = MaterialTheme.typography
    val uistate by viewModel.uiState.collectAsState()

    val attendList by viewModel._attandeList.collectAsState()
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    isMyEvent = event.uid == uid
    alreadyBooked = attendList.contains(uid)

    LaunchedEffect(Unit) {
        viewModel.loadAttendList(event.docId)
    }
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
    ) {
        Text(
                text = event.name,
                style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        SignUpEvent(
                event = event,
                attendList = attendList
        ) {

            if(isMyEvent)
            {
                navController.navigate(Screen.CreateEventScreen.route+"?eventDocId="+event.docId)
                return@SignUpEvent
            }
            navController.navigate(Screen.EventBookingScreen.route)
        }
        Spacer(modifier = Modifier.height(10.dp))
        EventDetailDescription(event)
        Spacer(modifier = Modifier.height(10.dp))
        EventDetailDateTime(event)
    }
}

@Composable fun EventDetailDescription(event: Event)
{
    val typography = MaterialTheme.typography
    Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MyColors.current._FFFFFF)
                .border(
                        BorderStroke(
                                1.5.dp,
                                MyColors.current._000000
                        ),
                        shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
    ) {
        Text(
                text = "Description",
                style = typography.titleLarge.copy(
                            color = MyColors.current._000000,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                    )
        )

        Text(
                text = event.description,
                style = typography.titleMedium
        )


    }

}

@Composable fun EventDetailDateTime(event: Event)
{
    val typography = MaterialTheme.typography
    Column(
            modifier = Modifier
                .background(MyColors.current._FFFFFF)
                .fillMaxWidth()
                .border(
                        BorderStroke(
                                1.5.dp,
                                MyColors.current._000000
                        ),
                        shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
    ) {
        Text(
                text = "Date and Time",
                style = typography.titleLarge.copy(
                            color = MyColors.current._000000,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                    )
        )

        Text(
                text = "${DateTimeFormmater.formateDate(event.date)} | ${DateTimeFormmater.formateTime(event.startTime)} - ${DateTimeFormmater.formateTime(event.endTime)}",
                style = typography.titleMedium
        )


    }

}

@Composable fun SignUpEvent(event: Event, attendList: List<String>, onEventClick: (Event) -> Unit)
{

    val typography = MaterialTheme.typography
    var heightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current.density

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MyColors.current._FFFFFF)
        .padding(5.dp)
        .clickable {
            onEventClick(event)
        }) {

        SubcomposeAsyncImage(
                modifier = Modifier
                    .weight(1.5f)
                    .height(heightDp),
                contentScale = ContentScale.Crop,
                model = event.images.firstOrNull(),
                error = {
                    Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = ""
                    )
                },
                loading = {
                    Box {
                        CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                        )
                    }
                },
                contentDescription = "user image"
        )
        Spacer(
                modifier = Modifier.width(10.dp)
        )
        Column(modifier = Modifier
            .weight(1f)
            .wrapContentHeight()
            .onGloballyPositioned { coordinates ->
                heightDp = (coordinates.size.height / density).toInt().dp
            },
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                    text = "${attendList.size} people are participating ",
                    style = typography.titleMedium.copy(
                            textAlign = TextAlign.Center,
                            color = MyColors.current._000000,
                            fontWeight = FontWeight.ExtraBold
                    )
            )
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedButton(
                    onClick = {
                        onEventClick(event)
                    },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current.primary_color),
            ) {

                Text(
                        text = if (isMyEvent) "Edit" else if (alreadyBooked) "Already Booked" else "Sign Up",
                        style = typography.titleSmall.copy(color = MyColors.current._FFFFFF)
                )

            }


        }
    }
}

@Preview(showBackground = true) @Composable fun SignUpEventPrev()
{
    RealisTixTheme {
        SignUpEvent(
                Event().copy(
                        name = "Shahzaib Ali",
                        organizer = "Omni tech"
                ),
                emptyList()
        ) {

        }
    }
}

@Preview(showBackground = true) @Composable fun EventDetailScreenPrev()
{

    RealisTixTheme {
        EventDetailScreen(
                NavController(LocalContext.current),
                HomeViewModel(
                        DataRepository(
                                SharedPref(LocalContext.current),
                                FireStoreRepository()
                        )
                )
        )
    }
}