package com.assignments.dummyapp.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.assignments.dummyapp.R
import com.assignments.dummyapp.model.ArticleModel
import com.assignments.dummyapp.view.DetailActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Utility {

    fun convertJsonArrToArticleList(jsonArray: JSONArray): ArrayList<ArticleModel> {
        val listOfArticles = ArrayList<ArticleModel>()
        for (i in 0 until jsonArray.length()) {

            val articleJson = jsonArray[i] as JSONObject
            listOfArticles.add(
                ArticleModel.fromJSONData(
                    articleJson = articleJson
                )
            )
        }
        return listOfArticles
    }

    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    fun openDetailArticle(context: Context, url: String) {

        if (isInternetAvailable(context)) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Constants.URL, url)
            context.startActivity(intent)
        }
        else
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    @Throws(ParseException::class)
    fun getDateFromString(dateStr: String, pattern: String): Date? {
        val dateFormat = SimpleDateFormat(pattern, Locale.US)
        return dateFormat.parse(dateStr)
    }

    fun getStringFromDate(date: Date, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.US)
        return dateFormat.format(date)
    }

    fun getFormattedDateTime(dateStr: String, oldPattern: String, newPattern: String): String {
        val date = getDateFromString(dateStr, oldPattern)
        return date?.let { getStringFromDate(it, newPattern) } ?: ""
    }
}