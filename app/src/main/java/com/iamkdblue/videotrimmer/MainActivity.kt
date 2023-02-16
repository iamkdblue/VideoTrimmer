package com.iamkdblue.videotrimmer

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iamkdblue.videotrimmer.ui.theme.VideoTrimmerTheme
import com.iamkdblue.videotrimmer.viewmodel.VideoTrimmerViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoTrimmerTheme {
                val videoTrimmerViewModel = VideoTrimmerViewModel()

                val configuration = LocalConfiguration.current

                val screenHeight = configuration.screenHeightDp.dp.dpToPx()
                val screenWidth = configuration.screenWidthDp.dp.dpToPx()


                val str = "Width : " + screenWidth + "\n" + "Height : " + screenHeight
                Log.d("displayMetrics", "$str")

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    showHorizontalThumbnail(videoTrimmerViewModel, screenWidth.toLong(), 124, 124)
                }
            }
        }
    }
}

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
private fun showHorizontalThumbnail(
    videoTrimmerViewModel: VideoTrimmerViewModel,
    viewWidth: Long,
    thumbWidth: Int,
    thumbHeight: Int
) {

    val context = LocalContext.current

    val descriptor = context.assets.openFd("naruto.mp4")

    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)

    val videoThumbnails =
        videoTrimmerViewModel.videoThumbnails(mediaMetadataRetriever, viewWidth, thumbWidth, thumbHeight)


    LazyRow {
        // Add 5 items
        items(videoThumbnails.size()) { index ->
            videoThumbnails[index.toLong()]?.let {
                BitmapImage(it)
            }
        }
    }
}

@Composable
fun BitmapImage(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "some useful description",
    )
}
