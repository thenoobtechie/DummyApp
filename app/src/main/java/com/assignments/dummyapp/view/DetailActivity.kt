package com.assignments.dummyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.assignments.dummyapp.R
import com.assignments.dummyapp.utils.Constants.URL
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        web_view.loadUrl(intent.getStringExtra(URL))
        web_view.settings.allowContentAccess = true
        web_view.settings.domStorageEnabled = true
        web_view.settings.setSupportZoom(true)
        web_view.settings.javaScriptEnabled = true
        web_view.settings.javaScriptCanOpenWindowsAutomatically = true

        web_view.isClickable = true
        web_view.webViewClient = WebViewClient()
        web_view.webChromeClient = WebChromeClient()
    }
}
