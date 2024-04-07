package com.playsnyc.realistix.ui.screens.myactivities

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.screens.home.SmallEvent
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MyActivitiesScreen(
    uid:String,
    saved:Boolean,
    viewModel: MyActivitiesScreenViewModel = koinViewModel(),
)
{

    Log.d(
            "TAG",
            "MyActivitiesScreen: saved = ${saved}"
    )
    val typography= MaterialTheme.typography
    val uiState by viewModel.uiState.collectAsState()
    var upcomingActivitiesVisible by rememberSaveable{ mutableStateOf(false) }
    var pastActivitiesVisible by rememberSaveable{ mutableStateOf(false) }

    val upcomingActivitiesList by viewModel.upcomingActivitiesList.collectAsState()
    val pastActivitiesList by viewModel.pastActivitiesList.collectAsState()

    LaunchedEffect(Unit){
        if(saved)
            viewModel.loadSavedActivities(uid)
        else
            viewModel.loadActivities(uid)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
        .background(MyPerColors._FFFFFF)
        ){

        if(uiState.isLoading)
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))

        Row(modifier = Modifier.clickable {
            upcomingActivitiesVisible=!upcomingActivitiesVisible
        }){
            Text(
                    text = "Upcoming activities",
                    style = typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = if(upcomingActivitiesVisible) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = ""
            )
        }
        AnimatedVisibility(visible = upcomingActivitiesVisible) {
            LazyColumn(modifier = Modifier
                .padding(10.dp)
                .weight(1f),content = {

                if(upcomingActivitiesList.isEmpty())
                {
                    item{

                        Text(
                                text = "No Upcoming events",
                                style = typography.titleMedium
                        )

                    }
                    return@LazyColumn
                }
                items(upcomingActivitiesList.size){
                    SmallEvent(
                            event = upcomingActivitiesList[it],
                            onEventClick = {

                            }
                    )
                }
            })
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.clickable {
            pastActivitiesVisible=!pastActivitiesVisible
        }){
            Text(
                    text = "Past activities",
                    style = typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = if(pastActivitiesVisible) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                    contentDescription = ""
            )
        }
        AnimatedVisibility(visible = pastActivitiesVisible) {
            LazyColumn(modifier = Modifier
                .padding(10.dp)
                .weight(1f),content = {

                 if(pastActivitiesList.isEmpty())
                 {
                     item{
                         Text(
                                 text = "No Past events",
                                 style = typography.titleMedium
                         )

                     }
                     return@LazyColumn
                 }
                items(pastActivitiesList.size){
                    SmallEvent(
                            event = pastActivitiesList[it],
                            onEventClick = {

                            }
                    )
                }

            })
        }


    }

}

@Preview(showBackground = true)
@Composable
fun MyActivitiesScreenPrev()
{

    val sharedPref = SharedPref(LocalContext.current)
    RealisTixTheme {

//        MyActivitiesScreen(
//                MyActivitiesScreenViewModel(
//                DataRepository(sharedPref,
//                        FireStoreRepository())
//
//        )
//        )
    }
}