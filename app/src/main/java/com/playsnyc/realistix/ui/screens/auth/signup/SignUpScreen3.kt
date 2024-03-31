package com.playsnyc.realistix.ui.screens.auth.signup

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playsnyc.realistix.R
import com.playsnyc.realistix.enums.UiScreens
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.repositories.AuthRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.RoundProgress
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.utils.MyFonts
import com.screen.mirroring.extensions.roundClickable

@Composable fun SignUpScreen3(viewModel: SignUpScreenViewModel)
{
    val uiState by viewModel.uiState.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val itemList = remember { generateItemList().toMutableStateList() }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
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
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(modifier = Modifier.weight(1f)){
            itemsIndexed(itemList) { index, item ->
                ListItem(item = item, onItemCheckedChange = { checked ->
                    itemList[index] = item.copy(checked = checked)
                })
            }
        }


        if (uiState.isLoading) RoundProgress(modifier = Modifier.size(30.dp))
        else Icon(
                modifier = Modifier
                    .size(50.dp)
                    .roundClickable {
                        viewModel.updateUser {
                            fvrtTypes= itemList.filter {it.checked}.map { it.name }
                        }
                        viewModel.changeScreen(UiScreens.SIGNUP_SCREEN4)
                    },
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next"
        )
        Spacer(modifier = Modifier.width(10.dp))


    }
}
data class Item(val name: String, val checked: Boolean)
@Composable
fun ListItem(item: Item, onItemCheckedChange: (Boolean) -> Unit) {

    Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
    ) {
        Checkbox(
                checked = item.checked,
                onCheckedChange = { checked ->
                    onItemCheckedChange(checked)
                }
        )
        Text(
                text =item.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
        )
    }
}
fun generateItemList(): MutableList<Item> {
    val list = mutableListOf<Item>()
    list.add(Item("Industry-specific networking mixers", checked = false))
    list.add(Item("Business breakfasts or luncheons", checked = false))
    list.add(Item("After-work networking happy hours", checked = false))
    list.add(Item("Professional association meetups", checked = false))
    list.add(Item("Panel discussions and speaker series", checked = false))
    list.add(Item("Mentorship or career development workshops", checked = false))
    list.add(Item("Alumni gatherings", checked = false))
    list.add(Item("Trade shows and expos", checked = false))
    list.add(Item("Pitch or demo nights", checked = false))
    return list
}

@Preview(showBackground = true)
@Composable
fun SignUpScreen3Prev()
{
    RealisTixTheme {

        val sharedPref= SharedPref(LocalContext.current)
        SignUpScreen3(
                viewModel = SignUpScreenViewModel(
                        FireStoreRepository(),
                        AuthRepository(sharedPref)
                )
        )
    }

}