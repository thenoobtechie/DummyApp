package com.assignments.dummyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.assignments.dummyapp.*
import com.assignments.dummyapp.adapter.*
import com.assignments.dummyapp.datasource.DBHelper
import com.assignments.dummyapp.datasource.InsertArticleCallback
import com.assignments.dummyapp.datasource.NetworkHelper
import com.assignments.dummyapp.utils.Utility
import kotlinx.android.synthetic.main.fragment_news_list.*

/**
 * A simple [Fragment] subclass.
 */
class NewsListFragment : Fragment(),
    DataFetchCallback,
    ListItemClickCallback {

    private val dataListAdapter =
        ArticleListAdapter(callback = this)
    private lateinit var viewDataBridge: ViewDataBridge
    var sortedVal = SORT_BY_TITLE_ASC

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_articleList.adapter = dataListAdapter
        rv_articleList.addItemDecoration(DividerItemDecoration(view.context, RecyclerView.VERTICAL))

        viewDataBridge = ViewDataBridge(
            DBHelper.getDBInstance(view.context),
            NetworkHelper(this)
        )

        if (Utility.isInternetAvailable(view.context)) viewDataBridge.fetch()
        else Toast.makeText(context, getString(R.string.network_error), Toast.LENGTH_SHORT).show()

        progress_layout.visibility = VISIBLE

        sort_by_title.setOnClickListener {

            if (sortedVal == SORT_BY_TITLE_ASC)
                sortedVal = SORT_BY_TITLE_DESC
            else
                sortedVal = SORT_BY_TITLE_ASC

            dataListAdapter.sortList(sortedVal)
        }

        sort_by_publish.setOnClickListener {

            if (sortedVal == SORT_BY_PUBLISH_ASC)
                sortedVal = SORT_BY_PUBLISH_DESC
            else
                sortedVal = SORT_BY_PUBLISH_ASC

            dataListAdapter.sortList(sortedVal)
        }
    }


    override fun onSuccess(articles: List<ArticleModel>) {

        progress_layout.visibility = GONE
        dataListAdapter.notifyAdapter(articles.sortedBy { it.title })
    }

    override fun onError(sting: String?) {
        Toast.makeText(
            context,
            R.string.data_fetch_error_msg, Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSave(article: ArticleModel) {

        viewDataBridge.save(article, object : InsertArticleCallback {
            override fun onInsertSuccess(row: Long) {

                if (row >= 0)
                    Toast.makeText(activity, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDelete(article: ArticleModel) {

    }

    override fun onItemClick(article: ArticleModel) {
        context?.let {
            Utility.openDetailArticle(it, article.url)
        }
    }

}
