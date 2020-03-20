package com.assignments.dummyapp.view

import android.app.Activity
import com.assignments.dummyapp.datasource.DBHelper
import com.assignments.dummyapp.datasource.NetworkHelper
import com.assignments.dummyapp.view.ArticleModel
import com.assignments.dummyapp.view.DataFetchCallback

class ViewDataBridge(
    var dbHelper: DBHelper,
    var networkHelper: NetworkHelper? = null,
    var isSavedItemFetch: Boolean = false
) {

    init {
        if (!isSavedItemFetch) fetch()
    }

    fun save(articleModel: ArticleModel) {
        dbHelper.insertArticle(articleModel)
    }

    private fun fetch() {
        networkHelper?.execute()
    }

    fun fetchSavedNews(activity: Activity?, callback: DataFetchCallback) {
        dbHelper.getArticles(activity, callback)
    }

    fun deleteSavedNews(id: Int) {
        dbHelper.removeArticle(id)
    }
}