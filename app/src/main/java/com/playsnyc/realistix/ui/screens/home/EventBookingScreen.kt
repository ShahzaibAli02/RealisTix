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
import androidx.compose.ui.res.stringResource
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
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.DateTimeFormmater
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable fun EventBookingScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
)
{

    val event:Event  = viewModel.currentEvent?: Event().copy(name="TESTTT")
    val typography = MaterialTheme.typography
    val uistate by viewModel.uiState.collectAsState()

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
    ) {
        Text(
                modifier = Modifier.fillMaxWidth(),
                text = event.name,
                style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        )
        SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(200.dp),
                contentScale= ContentScale.Crop,
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
                        CircularProgressIndicator(modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.Center))
                    }
                },
                contentDescription = "user image"
        )
        Spacer(modifier = Modifier.height(10.dp))
        EventDetailDateTimeVenue(event)
        Spacer(modifier = Modifier.height(10.dp))
        ElevatedButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._DDDDDD),
                onClick = {
//                    if (uiState.isLoading) return@ElevatedButton
//                    if (viewModel.validateSecondPage().not()) return@ElevatedButton
//
//                    viewModel.postEvent()

                }) {

            Text(
                    text = stringResource(R.string.confirm_your_availability),
                    style = typography.titleMedium.copy(
                            color = MyColors.current._000000,
                            fontWeight = FontWeight.Bold
                    )
            )
        }
    }
}

@Composable
fun EventDetailDateTimeVenue(event: Event)
{
    val typography=MaterialTheme.typography
    Column(modifier = Modifier
        .background(MyColors.current._FFFFFF)
        .fillMaxWidth()
        .padding(10.dp)
        .border(
                BorderStroke(
                        1.5.dp,
                        MyColors.current._000000
                ),
                shape = RoundedCornerShape(10.dp)
        )
        .padding(10.dp)

    ){
        Text(
                text = "Date and Time",
                style = typography.titleLarge
                    .copy(color= MyColors.current._000000,
                            fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
        )

        Text(
                text ="${DateTimeFormmater.formateDate(event.date)} | ${DateTimeFormmater.formateTime(event.startTime)} - ${DateTimeFormmater.formateTime(event.endTime)}",
                style = typography.titleMedium
        )



        Spacer(modifier = Modifier.height(10.dp))

        Text(
                text = event.type,
                style = typography.titleLarge
                    .copy(color= MyColors.current._000000,
                            fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
        )

        Text(
                text =event.address,
                style = typography.titleMedium
        )



        Spacer(modifier = Modifier.height(10.dp))

        Text(
                text = "Price",
                style = typography.titleLarge
                    .copy(color= MyColors.current._000000,
                            fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)
        )

        Text(
                text =if(event.fee==0.0) "Free" else "\$${event.fee}",
                style = typography.titleMedium
        )


    }

}


@Preview(showBackground = true)
@Composable
fun EventBookingScreenPrev()
{

    RealisTixTheme {
        EventBookingScreen(
                NavController(LocalContext.current),
                HomeViewModel(
                DataRepository(
                        SharedPref(LocalContext.current),
                        FireStoreRepository()
                )
        ))
    }
}