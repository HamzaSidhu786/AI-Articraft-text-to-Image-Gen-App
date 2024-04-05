package com.hamza.aiarticraft.ui.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hamza.aiarticraft.R
import com.hamza.aiarticraft.ui.main.AspectRatioSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptEntryColumn(
    promptText: MutableState<String>,
    speechRecognizerLauncher: ActivityResultLauncher<Intent>,
    context: Context,
    sharedPreferencesManager: SharedPreferencesManager
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 10.dp)
                .background(color = Color.White)
                .border(
                    width = 2.dp, brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Red, Color.Blue
                        )
                    ), shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(0.89f)
                        .height(130.dp)
                        .padding(horizontal = 10.dp)
                        .background(Color.Black),
                    value = promptText.value,
                    onValueChange = { promptText.value = it.take(500) },
                    placeholder = { Text("Enter Prompt") },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                Icon(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .clickable {
                            promptText.value = ""
                        },
                    imageVector = ImageVector.vectorResource(R.drawable.ic_clear),
                    contentDescription = "Clear Prompt text"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${promptText.value.length}/500")
                Row {
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_tips_and_updates),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .clickable {

                            })
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_mic),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .clickable {
                                Common.startSpeechRecognition(speechRecognizerLauncher, context)
                            })
                }
            }
        }

        Text(
            text = "Aspect Ratio",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 7.dp, top = 10.dp, bottom = 10.dp)
                .align(Alignment.Start),
            style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black)
        )

        AspectRatioSelector(
            onSelect = {
                sharedPreferencesManager.saveString(
                    context.getString(R.string.aspect_ratio), it
                )
            }, savedAsp = sharedPreferencesManager.getString(
                stringResource(id = R.string.aspect_ratio), "1:1"
            )
        )

    }

}