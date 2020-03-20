package com.assignments.dummyapp.view

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Parcelable
import com.assignments.dummyapp.utils.Constants.AUTHOR
import com.assignments.dummyapp.utils.Constants.CONTENT
import com.assignments.dummyapp.utils.Constants.DESCRIPTION
import com.assignments.dummyapp.utils.Constants.ID
import com.assignments.dummyapp.utils.Constants.IMAGE_uRL
import com.assignments.dummyapp.utils.Constants.PUBLISHED_AT
import com.assignments.dummyapp.utils.Constants.PUBLISHED_aT
import com.assignments.dummyapp.utils.Constants.TITLE
import com.assignments.dummyapp.utils.Constants.URL
import com.assignments.dummyapp.utils.Constants.URL_TO_IMAGE
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class ArticleModel(
    val id: Int = -1,
    var title: String,
    var url: String,
    var imageUrl: String,
    var description: String,
    var publishedAt: String,
    var author: String?,
    var content: String?,
    var imageBitmap: Bitmap? = null
) : Parcelable {

    fun toContentValues(): ContentValues {
        val  contentValues = ContentValues()
        contentValues.put(TITLE, title)
        contentValues.put(URL, url)
        contentValues.put(IMAGE_uRL, imageUrl)
        contentValues.put(DESCRIPTION, description)
        contentValues.put(PUBLISHED_aT, publishedAt)
        contentValues.put(AUTHOR, author)
        contentValues.put(CONTENT, content)
        return contentValues
    }

    companion object {

        fun fromTableData(cursor: Cursor): ArticleModel {
            return ArticleModel(
                id = cursor.getInt(cursor.getColumnIndex(ID)),
                title = cursor.getString(cursor.getColumnIndex(TITLE)),
                url = cursor.getString(cursor.getColumnIndex(URL)),
                imageUrl = cursor.getString(cursor.getColumnIndex(IMAGE_uRL)),
                description = cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                publishedAt = cursor.getString(cursor.getColumnIndex(PUBLISHED_aT)),
                author = cursor.getString(cursor.getColumnIndex(AUTHOR)),
                content = cursor.getString(cursor.getColumnIndex(CONTENT))
            )
        }

        fun fromJSONData(articleJson: JSONObject): ArticleModel {
            return ArticleModel(
                title = articleJson[TITLE] as String,
                url = articleJson[URL] as String,
                imageUrl = articleJson[URL_TO_IMAGE] as String,
                description = articleJson[DESCRIPTION] as String,
                publishedAt = articleJson[PUBLISHED_AT] as String,
                author = articleJson[AUTHOR] as? String?,
                content = articleJson[CONTENT] as? String?
            )
        }
    }
}