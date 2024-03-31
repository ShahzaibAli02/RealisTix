package com.playsnyc.realistix.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.playsnyc.realistix.R
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
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
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterialApi::class) @Composable fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
)
{

    val lazyPagingItems: LazyPagingItems<Event> = viewModel.pagedList.collectAsLazyPagingItems()
    val typography = MaterialTheme.typography
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
            refreshing = uiState.isLoading,
            onRefresh = {
                Log.d(
                        "TAG",
                        "HomeScreen:onRefresh "
                )
                viewModel.loadNextPage()
            })
    LaunchedEffect(Unit) {
        HeaderText.headerText.emit("Events")
    }
    Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MyColors.current._FFFFFF)
                .pullRefresh(pullRefreshState)
    ) {
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
        ) {
            Text(
                    text = "All Events",
                    style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            if(lazyPagingItems.loadState.refresh is LoadState.Loading)
                EventListShimmer()
            else if(lazyPagingItems.loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount==0)
                EmptyEvents()
            else PaginatedEventList(lazyPagingItems) {
                viewModel.currentEvent = it
                navController.navigate(Screen.EventScreenDetail.route)
            }
        }
        PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter)

        )
    }

}

@Composable fun EmptyEvents()
{
    val typography=MaterialTheme.typography
    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())){
        Spacer(modifier = Modifier.height(40.dp))
        androidx.compose.material.Text(
                modifier = Modifier.fillMaxSize(),
                text = "No events found !!",
                style = typography.titleMedium.copy(textAlign = TextAlign.Center)
        )

    }

}

@Composable fun PaginatedEventList(
    lazyPagingItems: LazyPagingItems<Event>,
    onEventClick: (Event) -> Unit,
)
{

    Log.d(
            "Paging",
            "PaginatedList:COMPOSE"
    )
    LazyColumn {
        items(
                count = lazyPagingItems.itemCount
        ) { index ->
            Log.d(
                    "Paging",
                    "PaginatedList: ${index}"
            )
            val event = lazyPagingItems[index]
            if (event != null)
            {
                Spacer(modifier = Modifier.height(5.dp))
                SmallEvent(
                        event,
                        onEventClick
                )
            }
        }

        if (lazyPagingItems.loadState.append == LoadState.Loading)
        {
            item {
                Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}



@Composable fun SmallEvent(event: Event, onEventClick: (Event) -> Unit)
{
    val typography = MaterialTheme.typography
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(5.dp)
        .clip(RoundedCornerShape(5.dp))
        .clickable {
            onEventClick(event)
        }) {
        SubcomposeAsyncImage(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f),
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
                    Box{
                        CircularProgressIndicator(modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center))
                    }
                    //                    CircularProgressIndicator()
                },
                contentDescription = "user image"
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                    text = event.name,
                    style = typography.titleMedium.copy(
                            color = MyColors.current.primary_color,
                            fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )
            Text(
                    text = "By ${event.organizer}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.titleSmall.copy(color = MyColors.current._000000)
            )
            Text(
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = "${
                        DateTimeFormmater.formateDate(
                                event.date,
                                "dd MMMM"
                        )
                    }  | ${DateTimeFormmater.formateTime(event.startTime)} - ${DateTimeFormmater.formateTime(event.endTime)} | ${
                        DateTimeFormmater.formateDate(
                                event.date,
                                "EEE"
                        )
                    } | ${event.address}",
                    style = typography.titleSmall.copy(color = MyColors.current._9A9A9A)
            )


        }
    }
}




@Preview(showBackground = true)
@Composable
fun SmallEventShimmerPrev()
{
    EventListShimmer()
}

@Composable
fun EventListShimmer()
{


    Column{

        repeat(5){
            Spacer(modifier = Modifier.height(5.dp))
            SmallEventShimmer()
        }
    }
}
@Composable fun SmallEventShimmer()
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp)
        .clip(RoundedCornerShape(5.dp))
    ) {
        Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .background(Color.Gray)
                    .shimmer(),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Color.Gray)
                        .shimmer()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Color.Gray)
                        .shimmer()
            )

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Gray)
                        .shimmer()
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Gray)
                        .shimmer()
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Gray)
                        .shimmer()
            )



        }
    }
}

//@Preview(showBackground = true) @Composable fun SmallEventPrev()
//{
//    RealisTixTheme {
//        SmallEvent(
//                Event().copy(
//                        name = "Shahzaib Ali",
//                        organizer = "Omni tech"
//                )
//        ) {
//
//        }
//    }
//}

@Preview(showBackground = true) @Composable fun HomeScreenPrev()
{

    RealisTixTheme {
        HomeScreen(
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