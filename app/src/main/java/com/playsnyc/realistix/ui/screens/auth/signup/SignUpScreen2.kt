package com.playsnyc.realistix.ui.screens.auth.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.playsnyc.realistix.R
import com.playsnyc.realistix.enums.UiScreens
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.repositories.AuthRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.RoundProgress
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts
import com.playsnyc.realistix.extensions.roundClickable

@Composable fun SignUpScreen2(viewModel: SignUpScreenViewModel)
{
    val userState by viewModel.userState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.uploadImage(it)
        }
    }

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        Text(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp),
                fontSize = 38.sp,
                lineHeight = 40.sp,
                text = stringResource(R.string.tell_us_about_you),
                fontFamily = MyFonts.poppins(),
                fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MyPerColors._004AAD),
                label = {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            fontSize = 15.sp,
                            text = stringResource(R.string.name),
                            letterSpacing = 1.5.sp,
                            fontFamily = MyFonts.poppins()
                    )
                },
                value = userState.name!!,
                onValueChange = {
                    viewModel.updateUser { name = it }
                })
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MyPerColors._004AAD),
                label = {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            fontSize = 15.sp,
                            text = stringResource(R.string.occupation),
                            letterSpacing = 1.5.sp,
                            fontFamily = MyFonts.poppins()
                    )
                },
                value = userState.occupation!!,
                onValueChange = {
                    viewModel.updateUser { occupation = it }
                })

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MyPerColors._004AAD),
                label = {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            fontSize = 15.sp,
                            text = stringResource(R.string.organization),
                            letterSpacing = 1.5.sp,
                            fontFamily = MyFonts.poppins()
                    )
                },
                value = userState.organization!!,
                onValueChange = {
                    viewModel.updateUser { organization = it }
                })


        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MyPerColors._004AAD),
                label = {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            fontSize = 15.sp,
                            text = stringResource(R.string.nationality),
                            letterSpacing = 1.5.sp,
                            fontFamily = MyFonts.poppins()
                    )
                },
                value = userState.nationality!!,
                onValueChange = {
                    viewModel.updateUser { nationality = it }
                })


        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MyPerColors._004AAD),
                label = {
                    Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            fontSize = 15.sp,
                            text = stringResource(R.string.age),
                            letterSpacing = 1.5.sp,
                            fontFamily = MyFonts.poppins()
                    )
                },
                value = if (userState.age == 0) "" else userState.age.toString(),
                onValueChange = {
                    if (it.length < 3)
                    {
                        viewModel.updateUser {
                            age = if (it.isBlank()) 0 else it.trim().toInt()
                        }
                    }

                })

        Spacer(modifier = Modifier.height(30.dp))

        Row(verticalAlignment = Alignment.CenterVertically) { // Image

            if (userState.image.isNullOrBlank().not())
            {
                SubcomposeAsyncImage(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .roundClickable {
                                launcher.launch("image/*")
                            },
                        model = userState.image,
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentDescription = "user image"
                )
            } else
            {
                Image(
                        painter = painterResource(id = R.drawable.profile_grey),
                        contentDescription = "",
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .roundClickable {
                                launcher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop,
                )
            }

            Spacer(modifier = Modifier.width(30.dp))
            Text(
                    text = "Upload your\nprofile picture",
                    fontFamily = MyFonts.poppins(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
            )

        }


        Spacer(modifier = Modifier.height(50.dp))
        Spacer(modifier = Modifier.weight(1f))

        if (uiState.isLoading) RoundProgress(modifier = Modifier.size(30.dp))
        else Icon(
                modifier = Modifier
                    .size(50.dp)
                    .roundClickable {


                        viewModel.changeScreen(UiScreens.SIGNUP_SCREEN3)
                    },
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Camera"
        )
        Spacer(modifier = Modifier.height(20.dp))


    }
}

@Preview(showBackground = true) @Composable fun SignUpScreen2Prev()
{
    RealisTixTheme {
        val sharedPref= SharedPref(LocalContext.current)
        SignUpScreen2(
                viewModel = SignUpScreenViewModel(
                        FireStoreRepository(),
                        AuthRepository(sharedPref)
                )
        )
    }

}