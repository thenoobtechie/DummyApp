package com.assignments.dummyapp.adapter

import android.graphics.Bitmap
import android.os.AsyncTask.THREAD_POOL_EXECUTOR
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignments.dummyapp.view.ArticleModel
import com.assignments.dummyapp.utils.ImageLoader
import com.assignments.dummyapp.R
import kotlinx.android.synthetic.main.article_item_layout.view.*

class ArticleListAdapter(
    var list: List<ArticleModel> = ArrayList(),
    var callback: ListItemClickCallback,
    var fromDB: Boolean = false
) :
    RecyclerView.Adapter<ArticleListAdapter.DummyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DummyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.article_item_layout, parent, false)

        //replace this icon with delete
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

    inner class DummyViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ImageLoadingCallback {

        fun bind(article: ArticleModel) {

            itemView.title.text = article.title
            itemView.author.text = article.author
            itemView.article_image.setImageResource(0)
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
                }
                else
                    callback.onSave(article)
            }
            itemView.setOnClickListener {
                callback.onClick(article)
            }
        }

        override fun onSuccess(position: Int, bitmap: Bitmap) {

            if (list.size > position) {
                list[position].imageBitmap = bitmap
                if (adapterPosition == position) itemView.article_image.setImageBitmap(bitmap)
            }
        }

        override fun onFailure(string: String) {

        }
    }
}

interface ListItemClickCallback {

    fun onSave(article: ArticleModel)

    fun onDelete(article: ArticleModel)

    fun onClick(article: ArticleModel)
}

interface ImageLoadingCallback {

    fun onSuccess(position: Int, bitmap: Bitmap)

    fun onFailure(string: String)
}