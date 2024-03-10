package com.playsnyc.realistix.ui.screens.howtouse

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.Pager
import com.playsnyc.realistix.R
import com.playsnyc.realistix.navigation.Screen
import com.playsnyc.realistix.ui.composables.BackPressHandler
import com.playsnyc.realistix.ui.theme.MyColors
import com.playsnyc.realistix.utils.MyFonts
import com.screen.mirroring.extensions.toDp
import com.screen.mirroring.extensions.toSp
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class) @Composable
fun HowToUseScreen(navHostController: NavHostController)
{

    BackPressHandler { // DO NOTHING JUST SO THAT USER DONT GO BACK
    }
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
    ) {


        var currentIndex by remember { mutableIntStateOf(0) }
        val listTexts = remember {
            listOf(
                    "1st: \n" + "Sign up an event that you are interested in",
                    "2nd:\n" + "Create your profile to know other attendees before the event",
                    "3rd:\n" + " Build connections with the unique \n" + "code at the event",
                    "4th: \n" + "Check out your \n" + "new connections \n" + "in one-go",
                    "5th:\n" + "Continue to \n" + "sign up for events "
            )
        }
        val images = remember {
            listOf(
                    R.drawable.how_to_use_1,
                    R.drawable.how_to_use_2,
                    R.drawable.how_to_use_3,
                    R.drawable.how_to_use_4,
                    R.drawable.how_to_use_5
            )
        }

        val pagerState = rememberPagerState(pageCount = { images.size + 1 })
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(pagerState) { // Collect from the a snapshotFlow reading the currentPage
            snapshotFlow { pagerState.currentPage }.collect { page ->
                currentIndex = page
            }
        }

        if (currentIndex < images.size)
            Text(
                modifier = Modifier.padding(start = 15.dp),
                fontSize = 38.sp,
                text = stringResource(R.string.how_to_use),
                fontFamily = MyFonts.poppins(),
                fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState
        ) { page ->

            if (page >= images.size)
            {
                HowToUseEnd(modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(MaterialTheme.colorScheme.background),
                        onRevisitAgain = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(0)
                            }
                        },
                        onDashBoard = {
                            navHostController.navigate(Screen.DashBoardScreen.route){
                                popUpTo(navHostController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        })
                return@HorizontalPager
            }
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        painter = painterResource(id = images[page]),
                        contentDescription = "",
                        alignment = Alignment.TopCenter
                )

                Text(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        text = listTexts[page],
                        fontFamily = MyFonts.poppins(),
                        fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        if (currentIndex < images.size) LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Absolute.Center
        ) {
            items(listTexts.size) {
                Image(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 10.dp),
                        painter = painterResource(id = if (currentIndex == it) R.drawable.baseline_circle_24 else R.drawable.outline_circle_24),
                        contentDescription = "",
                )
            }
        }


    }

}

@Composable fun HowToUseEnd(
    modifier: Modifier = Modifier,
    onRevisitAgain: () -> Unit,
    onDashBoard: () -> Unit,
)
{
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
    ) {

        Image(
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .size(
                            width = 300.dp,
                            height = 200.dp
                    )
                    .padding(0.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo RX"
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                fontSize = 40.sp,
                text = "Are you ready to expand your network?",
                fontFamily = MyFonts.poppins(),
                lineHeight = 40.sp,
                style = TextStyle(
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))

        ElevatedButton(shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._03BF62),
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                onClick = {
                    onDashBoard()
                }) {
            Text(
                    color = Color.White,
                    text = "Join event now!",
                    fontSize = 20.sp,
                    fontFamily = MyFonts.poppins(),
                    fontWeight = FontWeight.Bold
            )

        }

        Spacer(modifier = Modifier.height(40.dp))
        ElevatedButton(shape = RoundedCornerShape(size = 10.dp),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MyColors.current._CD0002),
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                onClick = {
                    onRevisitAgain()
                }) {
            Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,

                    text = "Revisit the features!",
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    fontFamily = MyFonts.poppins(),
                    fontWeight = FontWeight.Bold
            )

        }

    }
}

@Preview(showBackground = true) @Composable fun HowToUseScreenPrev()
{
    MaterialTheme { //        HowToUseScreen(NavHostController(LocalContext.current))
        HowToUseEnd(onRevisitAgain = { /*TODO*/ }) {}
    }
}