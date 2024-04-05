@file:OptIn(ExperimentalMaterial3Api::class)

package com.hamza.aiarticraft.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hamza.aiarticraft.R
import com.hamza.aiarticraft.ui.api.RequestBody
import com.hamza.aiarticraft.ui.main.components.GenerateButton
import com.hamza.aiarticraft.ui.main.components.ResultScreen
import com.hamza.aiarticraft.ui.main.components.SettingsFragment
import com.hamza.aiarticraft.ui.theme.AIArticraftTheme
import com.hamza.aiarticraft.ui.utils.Common.aiPromptStyles
import com.hamza.aiarticraft.ui.utils.Common.getApiResponse
import com.hamza.aiarticraft.ui.utils.PromptEntryColumn
import com.hamza.aiarticraft.ui.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: PromptViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIArticraftTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController, startDestination = "mainScreen"
                    ) {
                        composable("mainScreen") {
                            MainUI(viewModel, navController)
                        }
                        composable("settingsScreen") {
                            SettingsFragment(navController)
                        }

                        composable("resultScreen") { backStackEntry ->
//                            val base64Image = backStackEntry.arguments?.getString("base64Image")
                            ResultScreen(viewModel, navController)
                        }
                        // Add other composable screens with unique names
                    }
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUI(viewModel: PromptViewModel, navController: NavHostController) {

    var promptText = remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)
    var isDialogVisible by remember { mutableStateOf(false) }
    var isProgressBarVisible by remember { mutableStateOf(false) }
    var isBottomSheetOpen by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var aiPromptStyle by remember { mutableStateOf<AIPromptStyles?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var imge = remember { mutableStateOf("") }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (matches != null && matches.isNotEmpty()) {

                // Update your promptText or perform other actions with the speech result
                promptText.value = matches[0]
            }
        }
    }
    //UPDATING PROMPT TEXT IN VIEW MODEL CLASS
    viewModel.setPrompt(promptText.value)

    Column(
        modifier = Modifier
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Articraft AI",
                style = TextStyle(
                    fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Start
                ),
            )

            Spacer(modifier = Modifier.width(20.dp))

            Icon(painter = painterResource(R.drawable.ic_settings),
                contentDescription = "",
                modifier = Modifier.clickable {
                    navController.navigate("settingsScreen")
                })
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(0.92f)
                .verticalScroll(state = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PromptEntryColumn(
                promptText = promptText,
                speechRecognizerLauncher = speechRecognizerLauncher,
                context = context,
                sharedPreferencesManager = sharedPreferencesManager
            )

            @Composable
            fun allStyles(allStylesList: List<AIPromptStyles>) {
                val chunkedList = allStylesList.chunked(2)

                LazyHorizontalGrid(
                    rows = GridCells.Fixed(chunkedList.size),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(870.dp),

                    ) {
                    items(chunkedList) { pair ->

                        Row {
                            ItemRow(pair[0], onPromptChange = {
                                aiPromptStyle = it
                                isBottomSheetOpen = true
                            })
                            if (pair.size > 1) {
                                ItemRow(pair[1], onPromptChange = {
                                    aiPromptStyle = it
                                    isBottomSheetOpen = true
                                })
                            }
                        }
                    }
                }
            }
            allStyles(allStylesList = aiPromptStyles)

        }

        Spacer(modifier = Modifier.fillMaxHeight(0.05f))

        GenerateButton(onClick = {
            if (promptText.value.isNotBlank()) {
                isProgressBarVisible = true
                getApiResponse(body = RequestBody(
                    prompt = promptText.value,
                    steps = 21,
                    /*aspect_ratio = sharedPreferencesManager.getString(
                        context.getString(R.string.aspect_ratio), "1:1"
                    )*/
                ), apiResponse = {
                    isDialogVisible = true
                    viewModel.setBase64Code(it.image)
                    isProgressBarVisible = false
                    navController.navigate("resultScreen")
                }, onFailure = {
                    isProgressBarVisible = false
                })


            } else {
                isProgressBarVisible = false
                Toast.makeText(context, "Enter Prompt", Toast.LENGTH_SHORT).show()
            }
        }, modifier = Modifier.fillMaxWidth(0.92f))
    }


    if (isProgressBarVisible) {
        progressing()
    }


    if (isBottomSheetOpen) {
        aiPromptStyle.let {
            tryPromptSheet(aiPromptStyles = it!!,
                onDismissRequest = { isBottomSheetOpen = false },
                onTryNowClick = {
                    coroutineScope.launch {
                        scrollState.scrollTo(0)
                    }
                    promptText.value = it.pos_prompt
                    isBottomSheetOpen = false
                })
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemRow(
    aiPromptStyles: AIPromptStyles, onPromptChange: (aiPromptStyles: AIPromptStyles) -> Unit
) {
    val context = LocalContext.current

    Card(modifier = Modifier
        .padding(horizontal = 10.dp, vertical = 10.dp)
        .size(160.dp, 210.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = {
            onPromptChange(aiPromptStyles)
        }

    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(id = R.color.dark_white)),
                verticalArrangement = Arrangement.SpaceBetween // Adjust the vertical spacing as needed
            ) {
                Image(
                    painter = painterResource(id = aiPromptStyles.img),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    modifier = Modifier
                        .padding(
                            top = 2.dp,
                            start = 4.dp,
                            end = 4.dp,
                            bottom = 4.dp
                        ), // Adjust the weight as needed
                    text = aiPromptStyles.pos_prompt,
                    fontSize = 12.sp,
                    maxLines = 2,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "TRY",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 4.dp, end = 8.dp)
                    .align(Alignment.TopEnd)
                    .background(color = Color.White, shape = RoundedCornerShape(6.dp))
                    .size(30.dp, 20.dp)
                    .padding(top = 1.dp),
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun tryPromptSheet(
    aiPromptStyles: AIPromptStyles,
    onDismissRequest: () -> Unit,
    onTryNowClick: (aiPromptStyles: AIPromptStyles) -> Unit
) {

    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest.invoke() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
        ) {

            Image(
                painter = painterResource(id = aiPromptStyles.img),
                contentDescription = aiPromptStyles.pos_prompt,
                modifier = Modifier
                    .heightIn(0.dp, 260.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
            Text(text = aiPromptStyles.pos_prompt, style = TextStyle(fontSize = 16.sp))

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Box(modifier = Modifier
                .clickable {
                    onTryNowClick.invoke(aiPromptStyles)
                }) {
                Text(
                    text = "TRY NOW", style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ), modifier = Modifier
                        .fillMaxWidth(0.90f)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xffebb079), Color(0xffff1d7c), Color(0xff53a1ec)
                                )
                            ), shape = RoundedCornerShape(12.dp) // Adjust the radius as needed
                        )
                        .padding(vertical = 13.dp)

                )
            }

            Spacer(modifier = Modifier.height(25.dp))

        }
    }

}

@Composable
fun AspectRatioSelector(onSelect: (String) -> Unit, savedAsp: String) {
    // List of aspect ratios
    val aspectRatios =
        listOf("1:1", "4:3", "3:2", "2:3", "16:9", "9:16", "5:4", "4:5", "3:1", "3:4")

    // State to track the selected aspect ratio
    var selectedAspectRatio by remember { mutableStateOf(savedAsp) }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(aspectRatios) { aspectRatio ->
            Box(modifier = Modifier
                .clickable {
                    // Update the selected aspect ratio
                    selectedAspectRatio = aspectRatio
                    onSelect(selectedAspectRatio)
                }
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (aspectRatio == selectedAspectRatio) {
                            listOf(
                                Color(0xffebb079), Color(0xffff1d7c), Color(0xff53a1ec)
                            )
                        } else {
                            listOf(
                                Color(0xFFE8E8E8), Color(0xFFE8E8E8)
                            )
                        }
                    ), shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp)) {
                Text(
                    text = aspectRatio,
                    style = if (aspectRatio == selectedAspectRatio) {
                        TextStyle(fontWeight = FontWeight.Bold, color = Color.White)
                    } else {
                        TextStyle(fontWeight = FontWeight.Normal, color = Color.Black)
                    },
                    modifier = Modifier
                        .padding(vertical = 7.dp, horizontal = 8.dp)
                        .background(Color.Transparent)
                )
            }

        }
    }
}


@Composable
fun progressing() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .pointerInput(Unit) {}, contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AIArticraftTheme {
        MainUI(viewModel = viewModel(), navController = rememberNavController())
    }
}

data class AIPromptStyles(val pos_prompt: String, val neg_prompt: String, val img: Int)



