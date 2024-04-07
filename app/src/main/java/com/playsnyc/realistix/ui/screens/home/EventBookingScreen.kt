package com.playsnyc.realistix.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.enums.BottomNavItems
import com.playsnyc.realistix.data.model.Error
import com.playsnyc.realistix.data.model.Event
import com.playsnyc.realistix.data.model.HeaderMessage
import com.playsnyc.realistix.data.model.Success
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.model.successMessage
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.DateTimeFormmater
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class) @Composable fun EventBookingScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
)
{

    val event: Event = viewModel.currentEvent ?: Event()
    val typography = MaterialTheme.typography
    val uiState by viewModel.uiState.collectAsState()
    val isMyEvent = event.uid == FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(uiState.state) {
        if (uiState.isLoading.not()) HeaderText.messageHeader.emit(if (uiState.isError) HeaderMessage.Error(message = uiState.errorMessage) else HeaderMessage.Success(uiState.successMessage))
    }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
    ) {
        Text(
                modifier = Modifier.fillMaxWidth(),
                text = event.name,
                style = typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                )
        )
        SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(200.dp),
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
        Spacer(modifier = Modifier.height(10.dp))
        EventDetailDateTimeVenue(event)
        Spacer(modifier = Modifier.height(10.dp))

        if (uiState.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        else ElevatedButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._DDDDDD),
                onClick = {
                    if (!isMyEvent)
                        viewModel.confirmBooking(onSuccess = {
                        navController.navigate(Screen.ConnectionsScreen.route) {
                            popUpTo(Screen.EventScreenDetail.route) {
                                inclusive = true
                            }
                            launchSingleTop
                        }
                    })
                }) {

            Text(
                    text = if (isMyEvent) "Edit Event" else stringResource(R.string.confirm_your_availability),
                    style = typography.titleMedium.copy(
                            color = MyColors.current._000000,
                            fontWeight = FontWeight.Bold
                    )
            )
        }
    }
}

@Composable fun EventDetailDateTimeVenue(event: Event)
{
    val typography = MaterialTheme.typography
    Column(
            modifier = Modifier
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



        Spacer(modifier = Modifier.height(10.dp))

        Text(
                text = event.type,
                style = typography.titleLarge.copy(
                        color = MyColors.current._000000,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                )
        )

        Text(
                text = event.address,
                style = typography.titleMedium
        )



        Spacer(modifier = Modifier.height(10.dp))

        Text(
                text = "Price",
                style = typography.titleLarge.copy(
                        color = MyColors.current._000000,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                )
        )

        Text(
                text = if (event.fee == 0.0) "Free" else "\$${event.fee}",
                style = typography.titleMedium
        )


    }

}


@Preview(showBackground = true) @Composable fun EventBookingScreenPrev()
{

    RealisTixTheme {
        EventBookingScreen(
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