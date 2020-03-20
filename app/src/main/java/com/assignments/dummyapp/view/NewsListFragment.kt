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
    }



    override fun onSuccess(articles: List<ArticleModel>) {
        dataListAdapter.notifyAdapter(articles)
    }

    override fun onError(sting: String?) {
        Toast.makeText(context,
            R.string.data_fetch_error_msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSave(article: ArticleModel) {
        viewDataBridge.save(article)
        Toast.makeText(activity, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
    }

    override fun onDelete(article: ArticleModel) {

    }

    override fun onClick(article: ArticleModel) {
        context?.let {
            Utility.openUrlInBrowser(
                it,
                article.url
            )
        }
    }

}
