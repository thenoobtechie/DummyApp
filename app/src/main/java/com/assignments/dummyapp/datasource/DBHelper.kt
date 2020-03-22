package com.assignments.dummyapp.datasource

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.util.Log
import com.assignments.dummyapp.utils.Constants
import com.assignments.dummyapp.view.ArticleModel
import com.assignments.dummyapp.utils.Constants.AUTHOR
import com.assignments.dummyapp.utils.Constants.CONTENT
import com.assignments.dummyapp.utils.Constants.DB_NAME
import com.assignments.dummyapp.utils.Constants.DB_VERSION
import com.assignments.dummyapp.utils.Constants.DESCRIPTION
import com.assignments.dummyapp.utils.Constants.ID
import com.assignments.dummyapp.utils.Constants.IMAGE_uRL
import com.assignments.dummyapp.utils.Constants.PUBLISHED_aT
import com.assignments.dummyapp.utils.Constants.TABLE_ARTICLES
import com.assignments.dummyapp.utils.Constants.TITLE
import com.assignments.dummyapp.utils.Constants.URL
import com.assignments.dummyapp.view.DataFetchCallback
import java.lang.Exception

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_ARTICLES (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, url TEXT,  image_url TEXT, content TEXT, description TEXT, published_at TEXT, author TEXT, tempId TEXT UNIQUE)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO(NOT IMPLEMENTED)
    }

    fun insertArticle(articleModel: ArticleModel, callback: InsertArticleCallback) {
        InsertArticle(writableDatabase, articleModel, callback).execute()
    }

    fun removeArticle(id: Int, callback: DeleteArticleCallback) {
        DeleteArticle(writableDatabase, id, callback).execute()
    }

    fun getArticles(callback: DataFetchCallback) {

        FetchLocalRecords(writableDatabase, callback).execute()
    }

    companion object {
        private var instance: DBHelper? = null

        @Synchronized
        fun getDBInstance(context: Context): DBHelper {
            return instance
                ?: {
                    instance =
                        DBHelper(context)
                    instance!!
                }()
        }
    }

}

class FetchLocalRecords(var db: SQLiteDatabase, var callback: DataFetchCallback) :
    AsyncTask<Void, Void, ArrayList<ArticleModel>>() {

    override fun doInBackground(vararg params: Void?): ArrayList<ArticleModel> {
        val listOfArticles = ArrayList<ArticleModel>()
        try {
            val cursor =
                db.query(
                    TABLE_ARTICLES,
                    arrayOf(ID, TITLE, URL, IMAGE_uRL, PUBLISHED_aT, DESCRIPTION, CONTENT, AUTHOR, Constants.TEMP_ID),
                    null, null, null, null, "title ASC", null
                )
            if (cursor.moveToFirst()) {
                do {
                    listOfArticles.add(ArticleModel.fromTableData(cursor))
                } while (cursor.moveToNext())
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return listOfArticles
    }

    override fun onPostExecute(listOfArticles: ArrayList<ArticleModel>) {
        super.onPostExecute(listOfArticles)

        callback.onSuccess(listOfArticles)
    }
}

class InsertArticle(var db: SQLiteDatabase, var articleModel: ArticleModel, var callback: InsertArticleCallback? = null) :
    AsyncTask<Void, Void, Long>() {

    override fun doInBackground(vararg params: Void?): Long {

        var row = -1L
        try {
            row = db.insertWithOnConflict(TABLE_ARTICLES, null, articleModel.toContentValues(), SQLiteDatabase.CONFLICT_IGNORE)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Log.d("DBOperation", "Insert at row: $row")
            db.close()
        }
        return row
    }

    override fun onPostExecute(row: Long) {
        super.onPostExecute(row)

        callback?.onInsertSuccess(row)
    }
}

class DeleteArticle(var db: SQLiteDatabase, var id: Int, var callback: DeleteArticleCallback? = null) :
    AsyncTask<Void, Void, Int>() {

    override fun doInBackground(vararg params: Void?): Int {
        var rows = 0
        try {
            rows = db.delete(TABLE_ARTICLES, "id = ?", arrayOf(id.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Log.d("DBOperation", "Deleted rows: $rows")
            db.close()
        }
        return rows
    }

    override fun onPostExecute(rows: Int) {
        super.onPostExecute(rows)

        callback?.onDeleteSuccess(rows)
    }
}

interface DeleteArticleCallback {
    fun onDeleteSuccess(rows: Int)
}

interface InsertArticleCallback {
    fun onInsertSuccess(row: Long)
}