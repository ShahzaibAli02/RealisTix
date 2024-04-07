package com.playsnyc.realistix.ui.screens.createevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.Error
import com.playsnyc.realistix.data.model.HeaderMessage
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.data.model.isSuccess
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.DialogLoadingTextView
import com.playsnyc.realistix.ui.composables.ErrorText
import com.playsnyc.realistix.ui.composables.OutlinedTextField
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.MyPerColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable fun CreateEventScreen2(
    navController: NavHostController,
    viewModel: CreateEventScreenViewModel = koinViewModel(),
)
{

    val selectedImages by viewModel.selectedImages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val pickImagesLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(5),
    ) { uris ->
        selectedImages.addAll(uris)
    }
    val typography = MaterialTheme.typography
    val mColors = MyColors.current

    if (uiState.isSuccess)
    {
        LaunchedEffect(Unit) {
            HeaderText.messageHeader.emit(
                    HeaderMessage(
                            message = "Event Posted",
                            backGroundColor = MyPerColors._07910C
                    )
            )
            navController.popBackStack(
                    Screen.CreateEventScreen.route,
                    true
            )
        }
    }


    if (uiState.isLoading)
    {
        val message = uiState.state.message
        val progress = (uiState.state as ScreenState.Loading).progress/100f
        DialogLoadingTextView(
                modifier = Modifier.fillMaxWidth(),
                progress = progress,
                message = message
        ) {
            TextButton(modifier = Modifier.align(Alignment.End),
                    onClick = {
                        viewModel.cancelUpload()
                    }) {
                Text(
                        modifier = Modifier.padding(0.dp),
                        text = "Cancel",
                        style = typography.titleSmall
                )

            }
        }

    }



    Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(mColors._FFFFFF)
    ) {
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
        ) {

            if (uiState.isError)
            {

//                SideEffect{
//                    coroutineScope.launch { HeaderText.messageHeader.emit(HeaderMessage.Error(message =uiState.errorMessage  )) }
//                }
                ErrorText(
                        textAlign = TextAlign.Center,
                        text = uiState.errorMessage
                )
            }

            ImagePicker(
                    selectedImages,
                    onPickImage = {
                        pickImagesLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    onDeleteImage = {
                        selectedImages.removeAt(it)
                    });

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                    text = "Date and Time",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDatePickerDialog(
                            context,
                            Date(uiState.data!!.date)
                    ) {
                        viewModel.updateDataState {
                            date = it.time
                        }
                    }
                }
                .border(
                        BorderStroke(
                                1.5.dp,
                                Color.Black
                        ),
                        shape = RoundedCornerShape(5.dp)
                )
                .padding(8.dp)

            ) {
                Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        contentDescription = "Calendar"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                        modifier = Modifier.weight(1f),
                        style = typography.titleSmall,
                        text = formatDate(Date(uiState.data!!.date)),
                        color = MyColors.current._9A9A9A
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showTimePickerDialog(
                            context,
                            Date(uiState.data!!.startTime)
                    ) {
                        viewModel.updateDataState {
                            startTime = it.time
                        }
                    }
                }
                .border(
                        BorderStroke(
                                1.5.dp,
                                Color.Black
                        ),
                        shape = RoundedCornerShape(5.dp)
                )
                .padding(8.dp)

            ) {
                Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                        contentDescription = "Time"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                        modifier = Modifier.weight(1f),
                        style = typography.titleSmall,
                        text = formatTime(Date(uiState.data!!.startTime)),
                        color = MyColors.current._9A9A9A
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showTimePickerDialog(
                            context,
                            Date(uiState.data!!.endTime)
                    ) {
                        viewModel.updateDataState {
                            endTime = it.time
                        }
                    }
                }
                .border(
                        BorderStroke(
                                1.5.dp,
                                Color.Black
                        ),
                        shape = RoundedCornerShape(5.dp)
                )
                .padding(8.dp)

            ) {
                Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.baseline_access_time_24),
                        contentDescription = "Time"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                        modifier = Modifier.weight(1f),
                        style = typography.titleSmall,
                        text = formatTime(Date(uiState.data!!.endTime)),
                        color = MyColors.current._9A9A9A
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Text(
                    text = "How much do you want to charge for tickets?",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    value = if (uiState.data!!.fee == 0.0) "" else uiState.data!!.fee.toString(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MyColors.current.primary_color,
                            unfocusedBorderColor = MyColors.current._000000
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    contentPadding = PaddingValues(5.dp),
                    label = {
                        Text(
                                text = "\$",
                                style = typography.titleSmall.copy(color = mColors._9A9A9A)
                        )

                    },
                    onValueChange = {
                        viewModel.updateDataState {
                            fee = it.toDoubleOrNull() ?: 0.0
                        }
                    })


            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(colors = SwitchDefaults.colors(checkedThumbColor = MyColors.current.primary_color),
                        checked = uiState.data!!.fee == 0.0,
                        onCheckedChange = { checked ->
                            viewModel.updateDataState {
                                fee = if (checked) 0.0 else 5.0
                            }
                        })
                Text(
                        text = "My Event is free",
                        style = typography.titleSmall
                )

            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                    text = "What’s the capacity for your event?",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    singleLine = true,
                    value = uiState.data!!.capacity.toString(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MyColors.current.primary_color,
                            unfocusedBorderColor = MyColors.current._000000
                    ),
                    contentPadding = PaddingValues(5.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        viewModel.updateDataState {
                            capacity = it.toIntOrNull() ?: 0
                        }
                    })


            Spacer(modifier = Modifier.height(20.dp))

                ElevatedButton(modifier = Modifier.align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._DDDDDD),
                        onClick = {
                            if (uiState.isLoading) return@ElevatedButton
                            if (viewModel.validateSecondPage().not()) return@ElevatedButton

                            viewModel.postEvent()

                        }) {

                    Text(
                            stringResource(R.string.publish_your_event),
                            style = typography.titleMedium.copy(
                                    color = MyColors.current._000000,
                                    fontWeight = FontWeight.Bold
                            )
                    )
                }


        }
    }


}


fun showTimePickerDialog(
    context: android.content.Context,
    selectedTime: Date,
    onTimeSelected: (Date) -> Unit,
)
{

    val calendar = Calendar.getInstance().apply {
        time = selectedTime
    }

    val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minuteOfDay ->
                val updatedTime = Calendar.getInstance().apply {
                    set(
                            Calendar.HOUR_OF_DAY,
                            hourOfDay
                    )
                    set(
                            Calendar.MINUTE,
                            minuteOfDay
                    )
                }
                onTimeSelected(updatedTime.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // Set to true for 24-hour format
    )

    timePickerDialog.show()
}

fun showDatePickerDialog(
    context: android.content.Context,
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
)
{
    val calendar = Calendar.getInstance()
    selectedDate.let { calendar.time = it }
    val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(
                        year,
                        month,
                        dayOfMonth
                )
                onDateSelected(selectedDate.apply { time = calendar.timeInMillis })
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}

fun formatDate(date: Date): String
{
    val dateFormat = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
    )
    return dateFormat.format(date)
}

fun formatTime(date: Date): String
{
    val dateFormat = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
    )
    return dateFormat.format(date)
}

@Composable private fun ImagePicker(
    selectedImages: List<Uri>,
    onPickImage: () -> Unit,
    onDeleteImage: (Int) -> Unit,
)
{
    if (selectedImages.isEmpty())
    {
        UploadImage(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onPickImage()
            })
    } else
    {
        PickedImages(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
                imageList = selectedImages,
                onDeleteImage = { index ->
                    onDeleteImage(index)
                })
    }
}

@Composable private fun UploadImage(modifier: Modifier = Modifier)
{

    val typography = MaterialTheme.typography
    Card(
            modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Image(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.baseline_file_upload_24),
                contentDescription = ""
        )
        Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Upload photos",
                style = typography.titleSmall
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable fun PickedImages(
    imageList: List<Uri>,
    modifier: Modifier = Modifier,
    onDeleteImage: (Int) -> Unit,
)
{
    Card(modifier = modifier) {
        LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize()
        ) {
            items(imageList.size) { index ->
                val imageUri = imageList[index]
                ImageCard(uri = imageUri,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .height(100.dp),
                        onDeleteImage = {
                            onDeleteImage(index)
                        })
            }
        }
    }

}

@Composable fun ImageCard(uri: Uri, modifier: Modifier, onDeleteImage: () -> Unit)
{
    val painter: Painter = rememberImagePainter(uri)
    Card(
            modifier = modifier
                .padding(8.dp)
                .size(100.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .zIndex(1f)
                        .align(Alignment.TopEnd)
                        .clickable {
                            onDeleteImage()
                        },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
            )
            Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
            )
        }

    }
}


@Preview(
        showBackground = true,
        heightDp = 600,
        widthDp = 400
) @Composable fun CreateEventScreen2Prev()
{
    val context = LocalContext.current
    RealisTixTheme {
        CreateEventScreen2(
                NavHostController(LocalContext.current),
                CreateEventScreenViewModel(
                        DataRepository(
                                SharedPref(context),
                                FireStoreRepository()
                        )
                )
        )
    }
}