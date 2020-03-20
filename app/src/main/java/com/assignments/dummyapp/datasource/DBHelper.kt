package com.assignments.dummyapp.datasource

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
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
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_ARTICLES (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, url TEXT,  image_url TEXT, content TEXT, description TEXT, published_at TEXT, author TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO(NOT IMPLEMENTED)
    }

    fun insertArticle(articleModel: ArticleModel): Long {
        val db = writableDatabase
        var row = -1L
        try {
            row = db.insert(TABLE_ARTICLES, null, articleModel.toContentValues())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Log.d("DBOperation", "Insert at row: $row")
            db.close()
        }
        return row
    }

    fun removeArticle(id: Int): Int {
        val db = writableDatabase
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

    fun getArticles(context: Activity?, callback: DataFetchCallback) {

        Thread {
            val db = writableDatabase
            val listOfArticles = ArrayList<ArticleModel>()
            try {
                val cursor =
                    db.query(
                        TABLE_ARTICLES,
                        arrayOf(
                            ID,
                            TITLE,
                            URL,
                            IMAGE_uRL,
                            PUBLISHED_aT,
                            DESCRIPTION,
                            CONTENT,
                            AUTHOR
                        ),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                if (cursor.moveToFirst()) {
                    do {
                        listOfArticles.add(
                            ArticleModel.fromTableData(
                                cursor
                            )
                        )
                    } while (cursor.moveToNext())
                    cursor.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                db.close()
            }

            context?.runOnUiThread {
                callback.onSuccess(listOfArticles)
            }
        }.start()
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