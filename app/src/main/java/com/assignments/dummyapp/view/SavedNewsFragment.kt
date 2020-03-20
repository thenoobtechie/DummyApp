package com.assignments.dummyapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.assignments.dummyapp.*
import com.assignments.dummyapp.adapter.ArticleListAdapter
import com.assignments.dummyapp.adapter.ListItemClickCallback
import com.assignments.dummyapp.datasource.DBHelper
import com.assignments.dummyapp.utils.Utility
import kotlinx.android.synthetic.main.fragment_news_list.*

/**
 * A simple [Fragment] subclass.
 */
class SavedNewsFragment : Fragment(),
    ListItemClickCallback,
    DataFetchCallback {

    private lateinit var viewDataBridge: ViewDataBridge
    private val dataListAdapter =
        ArticleListAdapter(
            callback = this,
            fromDB = true
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_articleList.adapter = dataListAdapter
        rv_articleList.addItemDecoration(DividerItemDecoration(view.context, RecyclerView.VERTICAL))
        viewDataBridge = ViewDataBridge(
            DBHelper.getDBInstance(view.context),
            null,
            true
        )
    }

    override fun onResume() {
        super.onResume()

        viewDataBridge.fetchSavedNews(activity, this)
    }

    override fun onSave(article: ArticleModel) {

    }

    override fun onDelete(article: ArticleModel) {
        if (article.id > 0) viewDataBridge.deleteSavedNews(article.id)
        Toast.makeText(activity, getString(R.string.deleted_successfully), Toast.LENGTH_SHORT).show()

        viewDataBridge.fetchSavedNews(activity, this)
    }

    override fun onClick(article: ArticleModel) {
        context?.let {
            Utility.openUrlInBrowser(
                it,
                article.url
            )
        }
    }

    override fun onSuccess(articles: List<ArticleModel>) {
        dataListAdapter.notifyAdapter(articles)
    }

    override fun onError(sting: String?) {

    }
}
