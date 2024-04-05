package com.hamza.aiarticraft.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.speech.RecognizerIntent
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.hamza.aiarticraft.R
import com.hamza.aiarticraft.ui.api.ApiInstance
import com.hamza.aiarticraft.ui.api.ApiResponse
import com.hamza.aiarticraft.ui.api.RequestBody
import com.hamza.aiarticraft.ui.main.AIPromptStyles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.io.encoding.ExperimentalEncodingApi

object Common {

    val aiPromptStyles by lazy {

        return@lazy listOf(
            AIPromptStyles(
                "Fantasy cartoon style beautiful young lady in a Slavic traditional dress and one baby goat next to her. They are standing on the magical shore of the lake",
                "blurry",
                R.drawable.im_1
            ),
            AIPromptStyles(
                "detailed anime style,beautiful and stunning artwork, shimmering shadows, strong colors, candy cotton pastell colors, a racoonwoman, black hair, blue eyes, cyberpunk background with pink and blue lights",
                "blurry",
                R.drawable.im_2
            ),
            AIPromptStyles(
                "a beautiful woman, Hyperdetailed Eyes, Tee-Shirt Design, Line Art, Black Background, Ultra Detailed Artistic, Detailed Gorgeous Face, Natural Skin, Water Splash, Colour Splash Art, Fire and Ice, Splatter, Black Ink, Liquid Melting, Dreamy, Glowing, Glamour, Glimmer, Shadows, Oil On Canvas, Brush Strokes, Smooth, Ultra High Definition, 8k, Unreal Engine 5, Ultra Sharp Focus, Intricate Artwork Masterpiece, Ominous, Golden Ratio, Highly Detailed, Vibrant, Production Cinematic Character Render, Ultra High Quality Model",
                "blurry",
                R.drawable.im_3
            ),
            AIPromptStyles(
                "4d photographic image of full body image of a cute little chibi boy realistic, vivid colors octane render trending on artstation, artistic photography, photorealistic concept art, soft natural volumetric cinematic perfect light, UHD no background",
                "blurry",
                R.drawable.im_4
            ),
            AIPromptStyles(
                "Spiderman as Thor full-body, roadside, 2D render",
                "blurry",
                R.drawable.im_5
            ),
            AIPromptStyles(
                "8k wallpaper of a beautiful anime adventurer girl wearing gold jewelry in the streets of a city in the Western Sahara, by artgerm, intricate detail, trending on artstation, 8k, fluid motion, stunning shading",
                "blurry",
                R.drawable.im_6
            ),
            AIPromptStyles(
                "2D sci fi cryptography anime style",
                "blurry",
                R.drawable.im_7
            ),
            AIPromptStyles(
                "A boy playing video games at night in his room, illustration by Hergé, perfect coloring, 8k",
                "blurry",
                R.drawable.im_8
            ),

            )
    }


    @OptIn(ExperimentalEncodingApi::class)
    fun base64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes: ByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.decode(base64String, Base64.DEFAULT)
        } else {
            Base64.decode(base64String, Base64.DEFAULT)
        }
        val inputStream: InputStream = ByteArrayInputStream(decodedBytes)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun startSpeechRecognition(
        launcher: ActivityResultLauncher<Intent>,
        context: Context
    ) {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        try {
            launcher.launch(speechRecognizerIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Speech recognition not supported on this device",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun shareAppLink(context: Context) {
        val appPackageName = context.packageName
        val playStoreLink = "https://play.google.com/store/apps/details?id=$appPackageName"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, playStoreLink)
        }

        if (isIntentSafe(context, sendIntent)) {
            context.startActivity(
                Intent.createChooser(
                    sendIntent,
                    "Share Articraft AI with friends"
                )
            )
        }
    }

    fun openPlayStoreForRating(context: Context) {
        val appPackageName = context.packageName
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    fun saveImageToDownloads(bitmap: Bitmap, uri: Uri, context: Context) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                Toast.makeText(context, "Image saved to Downloads", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun onBtnSaveImg(context: Context, bitmap: Bitmap): String? {
        try {
            val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString()
            val myDir = File("$root/saved_images")
            myDir.mkdirs()
            val fname = getCurrentTimeString() + ".jpg"
            val file = File(myDir, fname)

            val out = FileOutputStream(file)
            val bm = bitmap
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
            return file.absolutePath
        } catch (e: Exception) {
            Log.d("onBtnSavePng", e.toString())
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    fun shareImage(context: Context, imagePath: String) {
        val imageFile = File(imagePath)
        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            imageFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Optional: Add a subject and text to the share intent
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Image")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this image!")

        // Start the system's activity for sending the shared content
        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }

    fun getApiResponse(
        body: RequestBody,
        apiResponse: (ApiResponse) -> Unit,
        onFailure: () -> Unit
    ) {

        ApiInstance.sdApi.sendRequest(body).enqueue(object : Callback<ApiResponse> {


            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val sdApiResponses = response.body()
                    Log.e("Dictionary", "onResponse:  ${sdApiResponses?.image}")

                    if (sdApiResponses != null) {
                        apiResponse(sdApiResponses)
                    }
                    // Extract the data that you need from the response
                } else {
                    // Handle error
                    Log.e("Dictionary", "onResponse: not succeed")
                    onFailure.invoke()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Failure", "failure: ${t.message}")
                onFailure.invoke()
            }
        })


    }


    fun getCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return dateFormat.format(Date())
    }


    fun sendFeedbackEmail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:ahadtechdeveloper@gmail.com")

        // Define a custom subject
        val subject = "Articraft Feedback - ${System.currentTimeMillis()}"

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear ...,")

        // Explicitly set the package for Gmail
        emailIntent.setPackage("com.google.android.gm")
        try {
            context.startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    private fun isIntentSafe(context: Context, intent: Intent): Boolean {
        val packageManager: PackageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        return activities.isNotEmpty()
    }

}