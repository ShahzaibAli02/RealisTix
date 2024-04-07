package com.playsnyc.realistix.ui.screens.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import com.playsnyc.realistix.utils.MyUtils
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable fun ContactScreen(
    navController:NavController,
    viewModel: ContactScreenViewModel = koinViewModel(),
)
{

    val typography = MaterialTheme.typography
    val todayDate: String = rememberSaveable {
        SimpleDateFormat(
                "dd MMMM, yy",
                Locale.getDefault()
        ).format(Date())
    }
    val uiState by viewModel.uiState.collectAsState()
    val listConnections by viewModel._connectionsList.collectAsState()
    var numberDialogVisibility by rememberSaveable { mutableStateOf(false) }
    val randomNumber by viewModel._randomNumber.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.generateNumber()
    }
    LaunchedEffect(uiState.state) {
        if (uiState.isLoading.not())
            HeaderText.messageHeader.emit(if (uiState.isError) HeaderMessage.Error(message = uiState.errorMessage) else HeaderMessage.Success(uiState.successMessage))
    }

    if (numberDialogVisibility)
    {
        DialogNumberPicker(
                onDismiss = {
                    numberDialogVisibility = false
                },
                onValue = { number ->
                    numberDialogVisibility = false
                    viewModel.connectWithUser(number)
                })
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

        Spacer(modifier = Modifier.height(10.dp))

        if (uiState.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))

        ElevatedButton(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._DDDDDD),
                onClick = {
                    numberDialogVisibility=true
                }) {

            Text(
                    "Type A Number",
                    style = typography.titleMedium.copy(
                            color = MyColors.current._000000,
                            fontWeight = FontWeight.Bold
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        ElevatedButton(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxWidth(),
                enabled = uiState.isLoading.not(),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._03BF62),
                onClick = {
                    viewModel.generateNumber()
                }) {

            Text(
                    "Generate A Number",
                    style = typography.titleMedium.copy(
                            color = MyColors.current._FFFFFF,
                            fontWeight = FontWeight.Bold
                    )
            )
        }


        Spacer(modifier = Modifier.height(15.dp))
        Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "** Tell your connection Type...",
                style = typography.titleMedium
        )
        Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = randomNumber.toString(),
                style = typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = MyFonts.poppins()
                )
        )
        Text(
                text = "Your new connection today",
                style = typography.titleMedium.copy(fontWeight = FontWeight.Black)
        )
        Text(
                text = todayDate,
                style = typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))
        ConnectionsList(listConnections){
            navController.navigate(Screen.MyActivitiesScreen.args(it.uid,"false"))
        }

    }
}


@Composable
fun DialogNumberPicker(onDismiss: () -> Unit, onValue: (String) -> Unit)
{


    var number by rememberSaveable { mutableStateOf("") }
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
        ) {

            Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
            ) {

                Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.End)
                            .roundClickable {
                                onDismiss()
                            },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                        text = "Type number provided by other event mate to connect with them",
                )
                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = number,
                        onValueChange = {
                            number = it
                        },
                        placeholder = {
                            Text(
                                    modifier = Modifier.padding(5.dp),
                                    text = "Enter Number",
                            )

                        })

                Spacer(modifier = Modifier.height(10.dp))
                ElevatedButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                        enabled = number.isBlank().not(),
                        onClick = {
                            if (number.isBlank().not()) onValue(number)
                        }) {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "Done",
                    )

                }


            }


        }
    }

}

@Composable
fun ConnectionsList(list:List<User>,onItemClick: (User) -> Unit)
{
    list.forEach {
        Spacer(modifier = Modifier.height(10.dp))
        UserContact(user=it, onItemClick = onItemClick)
    }
}

@Composable fun UserContact(modifier: Modifier = Modifier, user: User,onItemClick:(User)->Unit)
{
    val typography = MaterialTheme.typography
    val context= LocalContext.current
    Row(modifier = Modifier.clickable {
        onItemClick(user)
    }){

        SubcomposeAsyncImage(
                modifier = modifier
                    .size(90.dp)
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
        Column {

            Row {

                Text(
                        text = user.name ?: "",
                        style = typography.titleMedium
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                        text = "Connected at 19:15",
                        style = typography.titleSmall.copy(color = MyColors.current._9A9A9A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                user.socialMedia?.forEach { socialMediaItem ->
                    val item=getSocialMediaList().find { it.socialMediaName == socialMediaItem.socialMediaName }
                    Image(
                            modifier = Modifier
                                .size(50.dp)
                                .roundClickable {
                                    MyUtils.openUrl(
                                            context,
                                            item?.url + "" + socialMediaItem.userName.replace(
                                                    "@",
                                                    ""
                                            )
                                    )
                                },
                            painter = painterResource(
                                    id = item?.image
                                        ?: R.drawable.logo
                            ),
                            contentDescription = socialMediaItem.socialMediaName
                    )
                }

            }

        }


    }
}

fun getSocialMediaList(): MutableList<SocialMediaItem> {
    val list = mutableListOf<SocialMediaItem>()

    list.add(SocialMediaItem(
            image = R.drawable.linkedin,
            socialMediaName = "linkedin",
            userName = "",
            url = "https://www.linkedin.com/in/" // LinkedIn
    ))

    list.add(SocialMediaItem(
            image = R.drawable.instagram,
            socialMediaName = "instagram",
            userName = "",
            url = "https://www.instagram.com/" // Instagram
    ))

    list.add(SocialMediaItem(
            image = R.drawable.wechat,
            socialMediaName = "wechat",
            userName = "",
            url = "https://www.wechat.com/" // WeChat
    ))

    list.add(SocialMediaItem(
            image = R.drawable.xiaohongshu,
            socialMediaName = "xiaohongshu",
            userName = "",
            url = "https://www.xiaohongshu.com/" // Xiaohongshu
    ))

    list.add(SocialMediaItem(
            image = R.drawable.facebook,
            socialMediaName = "facebook",
            userName = "",
            url = "https://www.facebook.com/" // Facebook
    ))

    list.add(SocialMediaItem(
            image = R.drawable.github,
            socialMediaName = "github",
            userName = "",
            url = "https://www.github.com/" // GitHub
    ))

    list.add(SocialMediaItem(
            image = R.drawable.twitter,
            socialMediaName = "twitter",
            userName = "",
            url = "https://www.twitter.com/" // Twitter
    ))

    return list
}


@Preview(showBackground = true) @Composable fun ContactScreenPrev()
{
    RealisTixTheme { //        DialogNumberPicker(onDismiss = {
        //
        //        }, onValue = {
        //
        //        })

//        ContactScreen(
//                viewModel = ContactScreenViewModel(
//                        DataRepository(
//                                SharedPref(LocalContext.current),
//                                FireStoreRepository()
//                        )
//                )
//        )
    }

}