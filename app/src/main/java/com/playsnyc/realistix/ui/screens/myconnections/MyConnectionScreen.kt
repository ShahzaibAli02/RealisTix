package com.playsnyc.realistix.ui.screens.myconnections

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.Error
import com.playsnyc.realistix.data.model.HeaderMessage
import com.playsnyc.realistix.data.model.SocialMediaItem
import com.playsnyc.realistix.data.model.Success
import com.playsnyc.realistix.data.model.User
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.model.successMessage
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.OutlinedTextField
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts
import com.playsnyc.realistix.extensions.roundClickable
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.utils.DateTimeFormmater
import com.playsnyc.realistix.utils.MyUtils
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable fun MyConnectionScreen(
    navController: NavController,
    viewModel: MyConnectionScreenViewModel = koinViewModel(),
)
{

    val typography = MaterialTheme.typography
    val uiState by viewModel.uiState.collectAsState()
    val listConnections by viewModel._connectionsList.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadAllConnections()
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

        Text(

                text = "All connections",
                style = typography.titleLarge.copy(fontWeight = FontWeight.Black)
        )
        Spacer(modifier = Modifier.height(10.dp))
        if(listConnections.isEmpty()) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "No connections! ",
                    style = typography.titleSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedButton(modifier = Modifier.align(Alignment.CenterHorizontally),onClick = {
                navController.navigate(Screen.ConnectionsScreen.route)
            }) {
                Text(
                        text = "Add New Connections"
                )

            }
        }

        ConnectionsList(listConnections, onItemClick = {
            navController.navigate(Screen.MyActivitiesScreen.args(it.uid,"false"))
        }){
            viewModel.removeUser(it.uid!!)
        }

    }
}



@Composable
fun ConnectionsList(list:List<User>, onItemClick: (User) -> Unit, onRemove:(User)->Unit)
{
    list.forEach {
        Spacer(modifier = Modifier.height(10.dp))
        UserContact(user=it, onItemClick = onItemClick, onRemove = onRemove)
    }
}

@Composable fun UserContact(modifier: Modifier = Modifier, user: User,onItemClick:(User)->Unit,onRemove:(User)->Unit)
{
    val typography = MaterialTheme.typography
    val context= LocalContext.current
    Row(modifier = Modifier.background(color= MyPerColors._FFFFFF).fillMaxWidth().padding(5.dp).clickable {
        onItemClick(user)
    }){

        SubcomposeAsyncImage(
                modifier = modifier
                    .size(50.dp)
                    .clip(CircleShape)
                   ,
                contentScale = ContentScale.Crop,
                model = user.image,
                error = {
                    Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.profile_grey),
                            contentDescription = ""
                    )
                },
                loading = {
                    CircularProgressIndicator() //                    CircularProgressIndicator()

                },
                contentDescription = "user image"
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(Modifier.align(Alignment.CenterVertically)){
            Text(
                    text = user.name ?: "",
                    style = typography.titleMedium
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                    text = "from "+DateTimeFormmater.formateDate(Date()),
                    style = typography.titleSmall.copy(color = MyColors.current._9A9A9A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Text(
                modifier=Modifier.align(Alignment.CenterVertically).background(MyPerColors._9A9A9A,shape= CircleShape).padding(horizontal = 5.dp)
                    .roundClickable {
                        onRemove(user)
                },
                text = "Remove",
                style = typography.titleSmall
        )



    }
}


@Preview(showBackground = true)
@Composable
fun MyConnectionScreenPrev()
{
    RealisTixTheme {
        MyConnectionScreen(
                navController = NavController(LocalContext.current),
                viewModel = MyConnectionScreenViewModel(
                        DataRepository(
                                SharedPref(LocalContext.current),
                                FireStoreRepository()
                        )
                )
        )
    }

}