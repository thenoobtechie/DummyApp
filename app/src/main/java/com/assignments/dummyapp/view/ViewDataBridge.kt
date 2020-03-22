package com.assignments.dummyapp.view

import com.assignments.dummyapp.datasource.DBHelper
import com.assignments.dummyapp.datasource.DeleteArticleCallback
import com.assignments.dummyapp.datasource.InsertArticleCallback
import com.assignments.dummyapp.datasource.NetworkHelper
import com.assignments.dummyapp.model.ArticleModel

class ViewDataBridge(
    var dbHelper: DBHelper,
    var networkHelper: NetworkHelper? = null
) {

    fun save(articleModel: ArticleModel, callback: InsertArticleCallback) {
        dbHelper.insertArticle(articleModel, callback)
    }

    fun fetch() {
        networkHelper?.execute()
    }

    fun fetchSavedNews(callback: DataFetchCallback) {
        dbHelper.getArticles(callback)
    }

    fun deleteSavedNews(id: Int, callback: DeleteArticleCallback) {
        dbHelper.removeArticle(id, callback)
    }
}