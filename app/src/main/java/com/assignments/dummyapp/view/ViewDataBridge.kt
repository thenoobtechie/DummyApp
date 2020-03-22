package com.assignments.dummyapp.view

import android.app.Activity
import com.assignments.dummyapp.datasource.DBHelper
import com.assignments.dummyapp.datasource.NetworkHelper

class ViewDataBridge(
    var dbHelper: DBHelper,
    var networkHelper: NetworkHelper? = null
) {

    fun save(articleModel: ArticleModel) {
        dbHelper.insertArticle(articleModel)
    }

    fun fetch() {
        networkHelper?.execute()
    }

    fun fetchSavedNews(activity: Activity?, callback: DataFetchCallback) {
        dbHelper.getArticles(activity, callback)
    }

    fun deleteSavedNews(id: Int) {
        dbHelper.removeArticle(id)
    }
}