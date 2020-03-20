package com.assignments.dummyapp.datasource

import android.os.AsyncTask
import com.assignments.dummyapp.utils.Constants.ARTICLES
import com.assignments.dummyapp.utils.Constants.ERROR
import com.assignments.dummyapp.utils.Utility
import com.assignments.dummyapp.view.DataFetchCallback
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkHelper(var callback: DataFetchCallback): AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {

        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null

        try {

            val url = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            val lengthOfFile = urlConnection.contentLength
            val inputStream = urlConnection.inputStream ?: return ERROR
            val buffer = StringBuffer()
            reader = BufferedReader(InputStreamReader(inputStream))

            var content: String?
            do {
                content = reader.readLine()
                if (content != null) buffer.append(content + "\n")
            }
            while (content != null)

            if (buffer.isBlank()) return ERROR

            return buffer.toString()
        }
        catch (e: IOException){
            e.printStackTrace()
        }
        return ERROR
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        if (result == null || result == ERROR) callback.onError(result)
        else {

            val jsonArray = JSONObject(result).getJSONArray(ARTICLES)
            callback.onSuccess(
                Utility.convertJsonArrToArticleList(
                    jsonArray
                )
            )
        }
    }
}