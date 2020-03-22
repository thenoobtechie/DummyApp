package com.assignments.dummyapp.adapter

import android.graphics.Bitmap
import android.os.AsyncTask.THREAD_POOL_EXECUTOR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignments.dummyapp.model.ArticleModel
import com.assignments.dummyapp.utils.ImageLoader
import com.assignments.dummyapp.R
import com.assignments.dummyapp.utils.Constants.DISPLAY_DATE_FORMAT
import com.assignments.dummyapp.utils.Constants.PUBLISH_DATE_FORMAT
import com.assignments.dummyapp.utils.Utility
import kotlinx.android.synthetic.main.article_item_layout.view.*
import kotlin.collections.ArrayList

const val SORT_BY_TITLE_ASC = 0
const val SORT_BY_TITLE_DESC = 1
const val SORT_BY_PUBLISH_ASC = 2
const val SORT_BY_PUBLISH_DESC = 3

class ArticleListAdapter(
    var list: List<ArticleModel> = ArrayList(),
    var callback: ListItemClickCallback,
    var fromDB: Boolean = false//Common adapter for 2 lists (From network/DB)
) :
    RecyclerView.Adapter<ArticleListAdapter.DummyViewHolder>() {

    //Current applied sort
    var sortedVal = SORT_BY_TITLE_ASC

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.article_item_layout, parent, false)

        //replace this icon with delete if list is from DB
        if (fromDB) view.save_article.setImageResource(R.drawable.delete)
        return DummyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DummyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyAdapter(list: List<ArticleModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun sortList(param: Int) {

        when (param) {

            SORT_BY_TITLE_ASC -> list = list.sortedBy { it.title }
            SORT_BY_TITLE_DESC -> list = list.sortedByDescending { it.title }
            SORT_BY_PUBLISH_ASC -> {
                list =
                    list.sortedBy { Utility.getDateFromString(it.publishedAt, PUBLISH_DATE_FORMAT) }
            }
            SORT_BY_PUBLISH_DESC -> list = list.sortedByDescending {
                Utility.getDateFromString(it.publishedAt, PUBLISH_DATE_FORMAT)
            }
        }
        sortedVal = param
        notifyDataSetChanged()
    }

    inner class DummyViewHolder(view: View) : RecyclerView.ViewHolder(view), ImageLoadingCallback {

        fun bind(article: ArticleModel) {

            itemView.title.text = article.title
            itemView.author.text = article.author ?: "NA"
            itemView.published_at.text = Utility.getFormattedDateTime(
                article.publishedAt,
                PUBLISH_DATE_FORMAT,
                DISPLAY_DATE_FORMAT
            )

            article.imageBitmap?.let { itemView.article_image.setImageBitmap(it) }
                ?: ImageLoader(
                    article.imageUrl,
                    this,
                    adapterPosition
                ).executeOnExecutor(
                    THREAD_POOL_EXECUTOR
                )

            itemView.save_article.setOnClickListener {
                if (fromDB) {
                    callback.onDelete(article)
                } else
                    callback.onSave(article)
            }
            itemView.setOnClickListener {
                callback.onItemClick(article)
            }
        }

        override fun onSuccess(position: Int, bitmap: Bitmap) {

            if (list.size > position) {

                //Loaded bitmap cached in memory
                list[position].imageBitmap = bitmap

                //Load only if view holder is at correct position
                if (adapterPosition == position) itemView.article_image.setImageBitmap(bitmap)
            }
        }

        override fun onFailure(string: String) {
            //TODO(NOT IMPLEMENTED)
        }
    }
}

interface ListItemClickCallback {

    //Save articles
    fun onSave(article: ArticleModel)

    fun onDelete(article: ArticleModel)

    fun onItemClick(article: ArticleModel)
}

interface ImageLoadingCallback {

    fun onSuccess(position: Int, bitmap: Bitmap)

    fun onFailure(string: String)
}