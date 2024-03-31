package com.playsnyc.realistix.ui.screens.auth.signup

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.SocialMediaItem
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.repositories.AuthRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.ErrorText
import com.playsnyc.realistix.ui.composables.RoundProgress
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts

@Composable fun SignUpScreen4(viewModel: SignUpScreenViewModel)
{
    val userState by viewModel.userState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val itemList = remember { getSocialMediaList().toMutableStateList() }

    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
    ) {

        Text(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(0.dp),
                fontSize = 38.sp,
                lineHeight = 40.sp,
                text = stringResource(R.string.select_ur_fvrt_events),
                fontFamily = MyFonts.poppins(),
                fontWeight = FontWeight.Bold
        )
        if (uiState.isError)
        {
            ErrorText(text = uiState.errorMessage)
        }
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(itemList) { index, item ->
                SocialMediaRowListItem(item = item,
                        onNameChanged = { newName ->
                            itemList[index] = item.copy(userName = newName)
                        })
            }
        }



        ElevatedButton(shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._03BF62),
                modifier = Modifier.fillMaxWidth(fraction = 0.6f),
                onClick = {

                    if (uiState.isLoading) return@ElevatedButton
                    viewModel.updateUser {
                        socialMedia = itemList.filter { it.userName.isBlank().not() }
                    }
                    viewModel.signUp()

                }) {

            if (uiState.isLoading) RoundProgress(modifier = Modifier.size(30.dp))
            else Text(
                    color = Color.White,
                    text = "DONE!",
                    fontSize = 20.sp,
                    fontFamily = MyFonts.poppins(),
                    fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(30.dp))


    }
}

@Composable fun SocialMediaRowListItem(
    item: SocialMediaItem,
    onNameChanged: (String) -> Unit,
)
{
    Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
    ) {

        Image(
                modifier = Modifier.size(
                        70.dp
                ),
                painter = painterResource(id = item.image),
                contentDescription = item.socialMediaName
        )
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth(),
                shape = RoundedCornerShape(size = 20.dp),
                value = item.userName,
                onValueChange = {
                    onNameChanged(it)
                })
    }
}


fun getSocialMediaList(): MutableList<SocialMediaItem>
{
    val list = mutableListOf<SocialMediaItem>()
    list.add(
            SocialMediaItem(
                    image = R.drawable.linkedin,
                    socialMediaName = "linkedin",
                    userName = ""
            )
    )
    list.add(
            SocialMediaItem(
                    image = R.drawable.instagram,
                    socialMediaName = "instagram",
                    userName = ""
            )
    )
    list.add(
            SocialMediaItem(
                    image = R.drawable.wechat,
                    socialMediaName = "wechat",
                    userName = ""
            )
    )
    list.add(
            SocialMediaItem(
                    image = R.drawable.xiaohongshu,
                    socialMediaName = "xiaohongshu",
                    userName = ""
            )
    )
    list.add(
            SocialMediaItem(
                    image = R.drawable.facebook,
                    socialMediaName = "facebook",
                    userName = ""
            )
    )
    list.add(
            SocialMediaItem(
                    image = R.drawable.github,
                    socialMediaName = "github",
                    userName = ""
            )
    )
    list.add(
            SocialMediaItem(
                    image = R.drawable.twitter,
                    socialMediaName = "twitter",
                    userName = ""
            )
    )
    return list
}

@Preview(showBackground = true) @Composable fun SignUpScreen4Prev()
{
    val sharedPref= SharedPref(LocalContext.current)
    RealisTixTheme {
        SignUpScreen4(
                viewModel = SignUpScreenViewModel(
                        FireStoreRepository(),
                        AuthRepository(sharedPref)
                )
        )
    }

}