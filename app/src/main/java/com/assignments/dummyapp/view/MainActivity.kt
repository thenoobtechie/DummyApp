package com.assignments.dummyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.assignments.dummyapp.adapter.MainPagerAdapter
import com.assignments.dummyapp.R
import com.assignments.dummyapp.model.ArticleModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter =
            MainPagerAdapter(
                supportFragmentManager
            )
        (tab_layout as TabLayout).setupWithViewPager(view_pager)
    }
}

interface DataFetchCallback {

    fun onSuccess(articles: List<ArticleModel>)

    fun onError(sting: String?)
}
