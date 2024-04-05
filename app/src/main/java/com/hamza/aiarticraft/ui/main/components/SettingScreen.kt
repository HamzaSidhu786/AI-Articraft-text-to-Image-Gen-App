package com.hamza.aiarticraft.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hamza.aiarticraft.R
import com.hamza.aiarticraft.ui.theme.AIArticraftTheme
import com.hamza.aiarticraft.ui.utils.Common.openPlayStoreForRating
import com.hamza.aiarticraft.ui.utils.Common.sendFeedbackEmail
import com.hamza.aiarticraft.ui.utils.Common.shareAppLink
import com.hamza.aiarticraft.ui.utils.SharedPreferencesManager

@Composable
fun SettingsFragment(navController: NavHostController) {
    val context = LocalContext.current
    var isImgTypeClicked = remember { mutableStateOf(false) }
    val sharedPreferencesManager = SharedPreferencesManager(context)
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(20.dp)) // Add some spacing at the top

        // Profile section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
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
                    text = "Settings",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

            }

            Column(
                modifier = Modifier
                    .paint(painterResource(id = R.drawable.settings_header), alpha = 0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Articraft AI",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                )

                Text(
                    text = "Developed by 50+ AI Specialists, world-leading image processing tech",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(0.85f)
                )

                Text(
                    text = "TRY NOW",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xffebb079),
                                    Color(0xffff1d7c),
                                    Color(0xff53a1ec)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp) // Adjust the radius as needed
                        )
                        .padding(10.dp)
                        .clickable {

                        }
                )

            }



            Spacer(modifier = Modifier.height(20.dp))

            // Settings options
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "App Settings",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                SettingItem(
                    title = "Image Type",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_image),
                    action = {
                        isImgTypeClicked.value = true
                    }
                )
                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "General Options",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                SettingItem(
                    title = "Share App",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_share),
                    action = {
                        shareAppLink(context)
                    }
                )

                Spacer(modifier = Modifier.height(25.dp))

                SettingItem(
                    title = "Rate Us",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_rate_us),
                    action = { openPlayStoreForRating(context) }
                )

                Spacer(modifier = Modifier.height(25.dp))

                SettingItem(
                    title = "FeedBack",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_feedback),
                    action = { sendFeedbackEmail(context) }
                )

                Spacer(modifier = Modifier.height(25.dp))

                SettingItem(
                    title = "Privacy Policy",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_privacy_policy),
                    action = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp)) // Add some spacing at the bottom
    }

    if (isImgTypeClicked.value) {
        ImageTypeBottomSheet(
            sharedPreferencesManager.getString(
                stringResource(id = R.string.save_img_type),
                "JPG"
            ), onImageTypeSelected = {
                sharedPreferencesManager.saveString(context.getString(R.string.save_img_type), it)
                isImgTypeClicked.value = false
            }, onDismiss = {
                isImgTypeClicked.value = false
            })
    }

}

@Composable
fun SettingItem(title: String, action: () -> Unit, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                action.invoke()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,

                )
            Text(
                text = title,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = title
        )
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageTypeBottomSheet(
    imgType: String,
    onImageTypeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedImageType by remember { mutableStateOf(imgType) }
    val modalBottomSheetState = rememberModalBottomSheetState()


    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Image Type Options
            ImageTypeOption("JPG", selectedImageType == "JPG") {
                selectedImageType = "JPG"
            }
            Spacer(modifier = Modifier.height(16.dp))
            ImageTypeOption("PNG", selectedImageType == "PNG") {
                selectedImageType = "PNG"
            }
            Spacer(modifier = Modifier.height(16.dp))
            ImageTypeOption("WEBP", selectedImageType == "WEBP") {
                selectedImageType = "WEBP"
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Cancel and Done Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancel",
                    style = TextStyle(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .width(130.dp)
                        .padding(16.dp)
                        .border(2.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
                        .background(
                            color = Color.White
                        )
                        .padding(10.dp)
                        .clickable {
                            onDismiss.invoke()
                        },

                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.width(16.dp)) // Add space between the texts

                Text(
                    text = "Done",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier
                        .width(130.dp)
                        .padding(16.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xffebb079),
                                    Color(0xffff1d7c),
                                    Color(0xff53a1ec)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp) // Adjust the radius as needed
                        )
                        .padding(10.dp)
                        .clickable {
                            onImageTypeSelected(selectedImageType)
                        }
                )
            }


            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}


@Composable
fun ImageTypeOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val borderColor = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFED2939), // Red
                Color(0xFF001F3F)  // Blue
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFE8E8E8), // Red
                Color(0xFFE8E8E8)  // Blue
            )
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = Color(android.graphics.Color.parseColor("#E8E8E8")),
                shape = RoundedCornerShape(8.dp)
            )
            .border(2.dp, borderColor!!, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text, fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(
            selected = isSelected,
            onClick = null,
            enabled = false,
            modifier = Modifier
                .padding(end = 10.dp)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    AIArticraftTheme {
        SettingsFragment(navController = rememberNavController())
    }
}