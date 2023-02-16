package com.iamkdblue.videotrimmer.viewmodel

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.collection.LongSparseArray
import androidx.lifecycle.ViewModel
import kotlin.math.ceil

class VideoTrimmerViewModel : ViewModel() {

    fun videoThumbnails(
        mediaMetadataRetriever: MediaMetadataRetriever, viewWidth: Long, thumbWidth: Int, thumbHeight: Int
    ): LongSparseArray<Bitmap> {
        val thumbnailList: LongSparseArray<Bitmap> = LongSparseArray()

        //video length in ms
        val videoLengthInMs = (mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
            .toInt() * 1000).toLong()

        val numberOfThumbnails = ceil(viewWidth.toFloat() / thumbWidth).toInt()

        val interval = videoLengthInMs / numberOfThumbnails


        for (i in 1..numberOfThumbnails) {
            var bitmap = mediaMetadataRetriever.getFrameAtTime(i * interval, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            try {
                bitmap = Bitmap.createScaledBitmap(bitmap!!, thumbWidth, thumbHeight, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            thumbnailList.put(i.toLong(), bitmap)
        }
        mediaMetadataRetriever.release();
        return thumbnailList
    }

}