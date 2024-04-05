package com.hamza.aiarticraft.ui.main.components

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hamza.aiarticraft.R
import com.hamza.aiarticraft.ui.api.RequestBody
import com.hamza.aiarticraft.ui.main.PromptViewModel
import com.hamza.aiarticraft.ui.main.progressing
import com.hamza.aiarticraft.ui.theme.AIArticraftTheme
import com.hamza.aiarticraft.ui.utils.Common.base64ToBitmap
import com.hamza.aiarticraft.ui.utils.Common.getApiResponse
import com.hamza.aiarticraft.ui.utils.Common.onBtnSaveImg
import com.hamza.aiarticraft.ui.utils.Common.shareImage
import com.hamza.aiarticraft.ui.utils.PromptEntryColumn
import com.hamza.aiarticraft.ui.utils.SharedPreferencesManager

@Composable
fun ResultScreen(viewModel: PromptViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()
    var isProgressBarVisible by remember { mutableStateOf(false) }
    var isEditPromptVisible by remember { mutableStateOf(false) }
    var isFavImg by remember { mutableStateOf(false) }
    var imagePath by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val sharedPreferencesManager = SharedPreferencesManager(context)


    val downloadLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
            uri?.let {
                if (bitmap != null) {
//                saveImageToDownloads(bitmap, uri, context)
                }
            }
        }

    /*val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
        uri?.let {
            shareImageUri = it
            shareImage(uri)
        }
    }*/
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically


        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = "Result",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

        }

        base64ToBitmap(uiState.base64Code)?.let {
            bitmap = it
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxHeight(0.40f)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp))

            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = colorResource(id = R.color.dark_white),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(2.dp, color = Color.LightGray, shape = RoundedCornerShape(16.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isEditPromptVisible = true
                    }
            ) {
                Text(
                    text = "Prompt",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp, top = 10.dp)
                )
            }

            Text(
                text = uiState.prompt,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray,
                ),
                modifier = Modifier
                    .padding(start = 10.dp, end = 30.dp, bottom = 10.dp)
                    .height(40.dp)
            )

        }

        GenerateButton(txt = "Generate More", onClick = {
            isProgressBarVisible = true
            getApiResponse(body = RequestBody(
                prompt = uiState.prompt,
                steps = 21,
                /*aspect_ratio = sharedPreferencesManager.getString(
                    context.getString(R.string.aspect_ratio), "1:1"
                )*/
            ), apiResponse = {
                viewModel.setBase64Code(it.image)
                isProgressBarVisible = false
            }, onFailure = {
                isProgressBarVisible = false
            })
        }, modifier = Modifier.padding(top = 15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if (!isFavImg) R.drawable.ic_favorite else R.drawable.ic_favorite_filled),
                    contentDescription = "",
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.dark_white),
                            shape = RoundedCornerShape(19.dp)
                        )
                        .padding(all = 6.dp)
                        .clickable {
                            isFavImg = !isFavImg
                        },
                    tint = Color.Unspecified
                )
                Text(
                    text = "Like",
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_download),
                    contentDescription = "",
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.dark_white),
                            shape = RoundedCornerShape(19.dp)
                        )
                        .padding(all = 6.dp)
                        .clickable {
                            bitmap?.let { imagePath = onBtnSaveImg(context, bitmap = it)!! }
                        }
                )
                Text(
                    text = "Save",
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_share_2),
                    contentDescription = "",
                    modifier = Modifier
                        .background(
                            color = colorResource(id = R.color.dark_white),
                            shape = RoundedCornerShape(19.dp)
                        )
                        .padding(all = 6.dp)
                        .clickable {
                            if (imagePath.isEmpty()) {
                                imagePath = bitmap?.let { onBtnSaveImg(context, it) }!!
                                shareImage(context, imagePath)
                            } else {
                                shareImage(context, imagePath)
                            }
                        }
                )
                Text(
                    text = "Share",
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

    }

    if (isProgressBarVisible) {
        progressing()
    }

    if (isEditPromptVisible) {
        EditPrompt(onCancel = {
            isEditPromptVisible = false
        }, onGenAgain = {
            isEditPromptVisible = false
            isProgressBarVisible = true
            getApiResponse(body = RequestBody(
                prompt = uiState.prompt,
                steps = 21,
                /*aspect_ratio = sharedPreferencesManager.getString(
                    context.getString(R.string.aspect_ratio), "1:1"
                )*/
            ), apiResponse = {
                viewModel.setBase64Code(it.image)
                isProgressBarVisible = false
            }, onFailure = {
                isProgressBarVisible = false
            })
        }, viewModel = viewModel, prompt = uiState.prompt,sharedPreferencesManager)
    }
}

@Composable
fun GenerateButton(
    txt: String = "Generate",
    onClick: () -> Unit,
    modifier: Modifier = Modifier

) {
    Box(
        modifier = modifier.clickable {
            onClick.invoke()
        },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = txt,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xffebb079),
                            Color(0xffff1d7c),
                            Color(0xff53a1ec)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp) // Adjust the radius as needed
                )
                .padding(10.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPrompt(
    onCancel: () -> Unit,
    onGenAgain: () -> Unit,
    viewModel: PromptViewModel,
    prompt: String = "",
    sharedPreferencesManager: SharedPreferencesManager
) {

    val modalBottomSheetState = rememberModalBottomSheetState()
    var promptText = remember { mutableStateOf(prompt) }
    val context = LocalContext.current

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

    viewModel.setPrompt(promptText.value)

    ModalBottomSheet(
        onDismissRequest = { onCancel.invoke() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            PromptEntryColumn(
                promptText = promptText,
                speechRecognizerLauncher = speechRecognizerLauncher,
                context = context,
                sharedPreferencesManager = sharedPreferencesManager
            )

            GenerateButton(onClick = {
                onGenAgain.invoke()
            }, modifier = Modifier.padding(top = 15.dp))

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .border(2.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp) // Adjust the radius as needed
                    )
                    .clickable {
                        onCancel.invoke()
                    }
            ) {
                Text(
                    text = "Cancel",
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    AIArticraftTheme {
        ResultScreen(viewModel = viewModel(), navController = rememberNavController())
    }
}