package com.assignments.dummyapp.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import com.assignments.dummyapp.view.ArticleModel
import org.json.JSONArray
import org.json.JSONObject

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

    fun openUrlInBrowser(context: Context, url: String) {
        context.startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
    }
}