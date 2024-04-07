package com.playsnyc.realistix.ui.screens.createevent

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.playsnyc.realistix.R
import com.playsnyc.realistix.data.model.Error
import com.playsnyc.realistix.data.model.HeaderMessage
import com.playsnyc.realistix.data.model.ScreenState
import com.playsnyc.realistix.data.model.errorMessage
import com.playsnyc.realistix.data.model.isError
import com.playsnyc.realistix.data.model.isLoading
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.data.repositories.DataRepository
import com.playsnyc.realistix.data.repositories.FireStoreRepository
import com.playsnyc.realistix.data.repositories.SharedPref
import com.playsnyc.realistix.ui.composables.EventLocations
import com.playsnyc.realistix.ui.composables.OutlinedTextField
import com.playsnyc.realistix.ui.composables.TwoColumnGridCell
import com.playsnyc.realistix.ui.screens.dashboard.HeaderText
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.ui.theme.RealisTixTheme
import com.playsnyc.realistix.extensions.roundClickable
import com.playsnyc.realistix.ui.theme.MyPerColors
import org.koin.androidx.compose.koinViewModel


@Composable fun CreateEventScreen(
    navController: NavHostController,
    viewModel: CreateEventScreenViewModel = koinViewModel(),
    bundle:Bundle?=null
)
{

    val typography = MaterialTheme.typography
    val mColors = MyColors.current
    val uiState by viewModel.uiState.collectAsState()
    val eventId=bundle?.getString("eventDocId")
    LaunchedEffect(Unit){
        if(eventId!=null && eventId.isBlank().not())
            viewModel.loadEventDetails(eventId)
        else
            viewModel.clearDataState()

    }
    LaunchedEffect(uiState.state) {
        if (uiState.isError) HeaderText.messageHeader.emit(HeaderMessage.Error(message = uiState.errorMessage))
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

            Text(
                    text = "Event Title",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    value = uiState.data!!.name,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MyColors.current.primary_color,
                            unfocusedBorderColor = MyColors.current._000000
                    ),
                    contentPadding = PaddingValues(5.dp),
                    placeholder = {
                        Text(
                                text = "What’s the name of your event?",
                                style = typography.titleSmall.copy(color = mColors._9A9A9A)
                        )

                    },
                    onValueChange = {
                        viewModel.updateDataState {
                            name = it
                        }
                    })


            Spacer(modifier = Modifier.height(10.dp))
            Text(
                    text = "Organizer",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    value = uiState.data!!.organizer,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MyColors.current.primary_color,
                            unfocusedBorderColor = MyColors.current._000000
                    ),
                    contentPadding = PaddingValues(5.dp),
                    placeholder = {
                        Text(
                                text = "Tell attendees who is organizing this event",
                                style = typography.titleSmall.copy(color = mColors._9A9A9A)
                        )

                    },
                    onValueChange = {
                        viewModel.updateDataState {
                            organizer = it
                        }
                    })

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                    text = "Type",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(5.dp))
            EventTypes(uiState.data?.type,viewModel._eventTypes) {
                viewModel.updateDataState {
                    type=it
                }
//                uiState.data!!.type = it
            }
            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                    text = "Description",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    value = uiState.data!!.description,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MyColors.current.primary_color,
                            unfocusedBorderColor = MyColors.current._000000
                    ),
                    contentPadding = PaddingValues(5.dp),
                    maxLines = 5,
                    placeholder = {
                        Text(
                                text = "Add more details about your event & include what people can expect if they attend",
                                style = typography.titleSmall.copy(color = mColors._9A9A9A)
                        )

                    },
                    onValueChange = {
                        viewModel.updateDataState {
                            description = it
                        }
                    })

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                    text = "Location",
                    style = typography.titleSmall.copy(color = mColors._000000)
            )
            Spacer(modifier = Modifier.height(5.dp))
            EventLocations(defaultLocation = uiState.data?.locationType,onEventClicked = { //                uiState.data!!.locationType = it
                viewModel.updateDataState {
                    locationType = it
                }
            })
            Spacer(modifier = Modifier.height(5.dp))
            if (uiState.data!!.locationType != "To be announced") OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
                    shape = RoundedCornerShape(5.dp),
                    value = uiState.data!!.address,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MyColors.current.primary_color,
                            unfocusedBorderColor = MyColors.current._000000
                    ),
                    contentPadding = PaddingValues(5.dp),
                    placeholder = {

                        val icon = if (uiState.data!!.locationType == "Online") R.drawable.baseline_link_24 else R.drawable.baseline_search_24
                        val searchText = if (uiState.data!!.locationType == "Online") "Meeting Link here.." else "Search for a venue or type address"
                        Row {
                            Image(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = icon),
                                    contentDescription = "Search"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                    text = searchText,
                                    style = typography.titleSmall.copy(color = mColors._9A9A9A)
                            )
                        }


                    },
                    onValueChange = {
                        viewModel.updateDataState {
                            address = it
                        }
                    })

            Spacer(modifier = Modifier.height(20.dp))

            if(eventId!=null && eventId.isBlank().not())
            {
                ElevatedButton(
                        modifier = Modifier.align(Alignment.End),
                        colors= ButtonDefaults.elevatedButtonColors(containerColor = MyPerColors.primary_color),
                        enabled = uiState.isLoading.not()
                        ,onClick = {
                            viewModel.deleteEvent(eventId){
                                navController.navigate(navController.graph.startDestinationRoute!!){
                                    popUpTo(navController.graph.startDestinationRoute!!)
                                    launchSingleTop
                                }
                            }
                }) {

                    if(uiState.isLoading) CircularProgressIndicator()
                    else
                    Text(
                            text = "Delete Event",
                            style = typography.titleMedium.copy(color= MyPerColors._FFFFFF)
                    )

                }
            }
            Icon(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(50.dp)
                        .roundClickable {
                            if (viewModel
                                    .validateFirstPage()
                                    .not()
                            )
                            {
                                viewModel.updateUiState {
                                    state = ScreenState.Error("Fill all required fields!")
                                }
                                return@roundClickable
                            }
                            navController.navigate(Screen.CreateEventScreen2.route)

                        },
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Camera"
            )


        }
    }


}


@Composable private fun EventTypes(defaultSelection:String?=null,eventTypes: List<String>, onEventSelected: (String) -> Unit)
{
    var selectedEvent= defaultSelection?:""
    repeat(eventTypes.size) { it ->
        if ((it + 1) % 2 != 0) TwoColumnGridCell(
                txt1 = eventTypes[it],
                txt2 = eventTypes.getOrNull(it + 1),
                selectedText = selectedEvent
        ) {
            selectedEvent = it
            onEventSelected(selectedEvent)
        }
    }
}


@Preview(
        showBackground = true,
        heightDp = 600,
        widthDp = 400
) @Composable fun CreateEventScreenPrev()
{
    val context = LocalContext.current
    RealisTixTheme {
        CreateEventScreen(
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