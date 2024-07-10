package com.playsnyc.realistix.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.playsnyc.realistix.data.model.SocialMediaItem
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.ui.composables.ErrorText
import com.playsnyc.realistix.ui.composables.RoundProgress
import com.playsnyc.realistix.ui.screens.auth.signup.SignUpScreenViewModel
import com.playsnyc.realistix.ui.screens.auth.signup.SocialMediaRowListItem
import com.playsnyc.realistix.ui.screens.auth.signup.getSocialMediaList
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.utils.MyFonts
import org.koin.androidx.compose.koinViewModel

@Composable fun SocialMediaScreen(
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel(),
)
{
    val uiState by viewModel.uiState.collectAsState()
    var itemList by remember { mutableStateOf<MutableList<SocialMediaItem>>(mutableListOf()) }
    LaunchedEffect(Unit) {
        val list = getSocialMediaList().toMutableStateList()
        list.forEachIndexed { index, socialMediaItem ->
            val item = uiState.data?.socialMedia?.find { it.socialMediaName == socialMediaItem.socialMediaName }
            item?.let {
                socialMediaItem.userName = it.userName //                itemList.set(index,socialMediaItem)
            }
        }
        itemList = list
    }

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
                fontSize = 30.sp,
                lineHeight = 40.sp,
                text = "Sync your account with other profiles!",
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
                    viewModel.updateSocialMediaList(itemList) {
                        navController.navigateUp()
                    }

                }) {

            if (uiState.isLoading) RoundProgress(modifier = Modifier.size(30.dp))
            else Text(
                    color = Color.White,
                    text = "Update!",
                    fontSize = 20.sp,
                    fontFamily = MyFonts.poppins(),
                    fontWeight = FontWeight.Bold
            )

        }
        Spacer(modifier = Modifier.height(30.dp))


    }
}