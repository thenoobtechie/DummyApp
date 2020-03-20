package com.assignments.dummyapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.assignments.dummyapp.utils.Constants.ERROR
import com.assignments.dummyapp.adapter.ImageLoadingCallback
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader(
    var url: String,
    var callback: ImageLoadingCallback,
    var position: Int
): AsyncTask<Void, Void, Bitmap?>() {

    override fun doInBackground(vararg params: Void?): Bitmap? {
        val url = URL(url)
        val urlConnection = url.openConnection() as HttpURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.connect()

        try {
            val inputStream = urlConnection.inputStream
            return BitmapFactory.decodeStream(inputStream)
        }
        catch (e: IOException){
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)

        result?.let { callback.onSuccess(position, it) } ?: callback.onFailure(ERROR)
    }
}