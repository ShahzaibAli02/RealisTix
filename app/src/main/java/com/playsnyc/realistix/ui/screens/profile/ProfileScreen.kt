package com.playsnyc.realistix.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.ProfileOption
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.ErrorText
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyUtils
import com.playsnyc.realistix.extensions.roundClickable
import com.valentinilk.shimmer.shimmer
import org.koin.androidx.compose.koinViewModel


@Composable fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = koinViewModel(),
    onLogOut: () -> Unit,
)
{

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadUser()
        HeaderText.headerText.emit("Profile")
    }
    Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
                .background(MyColors.current._FFFFFF),
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiState.isLoading)
        {
            ProfileShimmer()
        }
        else if(uiState.isError)
        {
            ErrorText(textAlign = TextAlign.Center,text = uiState.errorMessage)
        }
        else
            ProfileContent(
                navController,
                viewModel,onLogOut)


    }

}

@Composable private fun ProfileContent(
    navController: NavHostController,
    viewModel: ProfileViewModel,
    onLogOut:()->Unit,
)
{

    val context= LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
//                    viewModel.uploadImage(it)
        }
    }
    val typography = MaterialTheme.typography
    SubcomposeAsyncImage(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .roundClickable {
                    launcher.launch("image/*")
                },
            contentScale= ContentScale.Crop,
            model = uiState.data?.image,
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
    Text(
            text = uiState.data?.name ?: "N/A",
            style = typography.titleSmall
    )
    Text(
            text = uiState.data?.email ?: "N/A",
            style = typography.titleSmall.copy(fontWeight = FontWeight.Normal)
    )
    Spacer(modifier = Modifier.height(10.dp))
    viewModel._options.forEach {
        ProfileOptions(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                it
        ) {
            if (it.id == R.id.createEvents)
            {
                navController.navigate(Screen.CreateEventScreen.route)
            }
            if (it.id == R.id.your_activities)
            {
                navController.navigate(Screen.MyActivitiesScreen.args(FirebaseAuth.getInstance().currentUser?.uid,"false"))
            }
            if (it.id == R.id.savedEvents)
            {
                navController.navigate(Screen.MyActivitiesScreen.args(FirebaseAuth.getInstance().currentUser?.uid,"true"))
            }
            if (it.id == R.id.privacy_policy)
            {
              MyUtils.openUrl(context,context.getString(R.string.privacy_policy))
            }
            if (it.id == R.id.termsConditions)
            {
                MyUtils.openUrl(context,context.getString(R.string.terms_condition))
            }
            if (it.id == R.id.log_out)
            {
                onLogOut()
            }
        }
    }
}

@Composable fun ProfileShimmer()
{
    Spacer(modifier = Modifier.height(30.dp))
    Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .shimmer(),
            contentAlignment = Alignment.Center
    ) {}
    Spacer(modifier = Modifier.height(5.dp))
    Box(
            modifier = Modifier
                .width(50.dp)
                .height(10.dp)
                .background(Color.Gray)
                .shimmer()
    )
    Spacer(modifier = Modifier.height(5.dp))
    Box(
            modifier = Modifier
                .width(100.dp)
                .height(10.dp)
                .background(Color.Gray)
                .shimmer()
    )
    Spacer(modifier = Modifier.height(10.dp))
    repeat(10) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(Color.Gray)
                    .shimmer()
        )

    }
}

@Composable fun ProfileOptions(
    modifier: Modifier = Modifier,
    profileOption: ProfileOption,
    onClicked: (ProfileOption) -> Unit,
)
{
    val typoGraphy = MaterialTheme.typography

    Row(
            modifier = modifier
                .clip(CircleShape)
                .clickable {
                    onClicked(profileOption)
                },
            verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = profileOption.image),
                contentDescription = ""
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
                modifier = Modifier.weight(1f),
                text = profileOption.title,
                style = typoGraphy.titleMedium.copy(fontWeight = FontWeight.Normal),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = ""
        )
    }
}

@Preview(
        showBackground = true,
        widthDp = 400,
        heightDp = 700
) @Composable fun LoginScreenPrev()
{

    RealisTixTheme {
        ProfileScreen(
                navController = NavHostController(LocalContext.current),
                viewModel = ProfileViewModel(
                        DataRepository(
                                SharedPref(LocalContext.current)
                        ,
                                FireStoreRepository()
                        )
        ),
                onLogOut = {

                }
        )
    }

}
