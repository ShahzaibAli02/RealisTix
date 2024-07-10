package com.playsnyc.realistix.ui.screens.attandes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.Error
import com.playsnyc.realistix.data.model.HeaderMessage
import com.playsnyc.realistix.data.model.Success
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.model.successMessage
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.extensions.roundClickable
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.utils.DateTimeFormmater
import org.koin.androidx.compose.koinViewModel
import java.util.Date


@Composable fun AttandeListScreen(
    navController: NavController,
    viewModel: AttandeListScreenViewModel = koinViewModel(),
    eventID:String,
)
{

    val typography = MaterialTheme.typography
    val uiState by viewModel.uiState.collectAsState()
    val listConnections by viewModel._connectionsList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadAllAttandee(eventID)
    }
    LaunchedEffect(uiState.state) {
        if (uiState.isLoading.not())
            HeaderText.messageHeader.emit(if (uiState.isError) HeaderMessage.Error(message = uiState.errorMessage) else HeaderMessage.Success(uiState.successMessage))
    }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                        vertical = 10.dp,
                        horizontal = 20.dp
                )
                .verticalScroll(rememberScrollState())
    ) {

        if (uiState.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))


        Spacer(modifier = Modifier.height(10.dp))
        if(listConnections.isEmpty()) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "No attendee found! ",
                    style = typography.titleSmall
            )
            return
        }
        Log.d(
                "TAG",
                "AttandeListScreen: SIZE : "+listConnections.size
        )
        com.playsnyc.realistix.ui.screens.contact.ConnectionsList(listConnections) {
            navController.navigate(
                    Screen.MyActivitiesScreen.args(
                            it.uid,
                            "false"
                    )
            )
        }


    }
}



@Composable
fun ConnectionsList(modifier:Modifier = Modifier,list:List<User>, onItemClick: (User) -> Unit)
{
    Column(modifier = modifier){
        list.forEach {
            Spacer(modifier = Modifier.height(10.dp))
            com.playsnyc.realistix.ui.screens.contact.UserContact(
                    user = it,
                    onItemClick = onItemClick
            )
        }
    }

}

