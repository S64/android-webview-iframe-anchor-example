package jp.s64.android.exmaple.webviewiframeanchorexample

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.htmlEncode

class MainActivity : AppCompatActivity() {

    companion object {

        val INNER_HTML = """
            <!DOCTYPE html>
            <html>
                <head>
                    <meta charset="utf-8"/>
                </head>
                <body>
                    <a href="https://example.com" target="_blank">_blank</a><br/>
                    <a href="https://example.com" target="_top">_top</a>
                </body>
            </html>
        """.trimIndent()

    }

    private val webView by lazy { findViewById<WebView>(R.id.webView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.loadData(
            """
                <!DOCTYPE html>
                <html>
                    <head>
                        <meta charset="utf-8"/>
                    </head>
                    <body>
                        Outer
                        <iframe
                            style="margin: 10px;"
                            src="data:text/html,${INNER_HTML.htmlEncode()}"></iframe>
                        Outer
                    </body>
                </html>
            """.trimIndent(),
            "text/html",
            "utf-8"
        )

        webView.webViewClient = listener
    }

    private val listener = object : WebViewClient() {

        private var firstLoadingFinished = false

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            firstLoadingFinished = true
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return shouldOverrideUrlLoading(view, request?.url?.toString())
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?
        ): Boolean {
            if (!firstLoadingFinished) {
                return false
            }

            Toast.makeText(
                this@MainActivity,
                "shouldOverrideUrlLoading(_, ${url})",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }

    }

}
