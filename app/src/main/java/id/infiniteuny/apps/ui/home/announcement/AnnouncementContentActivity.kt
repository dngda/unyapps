package id.infiniteuny.apps.ui.home.announcement

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import id.infiniteuny.apps.R
import kotlinx.android.synthetic.main.activity_announcement_content.*
import kotlinx.android.synthetic.main.loading_layout.*


class AnnouncementContentActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement_content)

        setSupportActionBar(bottomAppBar)

        Glide.with(applicationContext).asGif().load(R.drawable.loading).into(loading)

        val url = "https://uny.ac.id" + (intent.getStringExtra("link"))
        val userAgent = System.getProperty("http.agent")

        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                loadingContainer.visibility = View.GONE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeContainer.isRefreshing = false
            }
        }
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = userAgent
        webView.loadUrl(url)

        swipeContainer.setOnRefreshListener {
            webView.reload()
        }

        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
            //checking Runtime permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Do this, if permission granted
                    downloadDialog(url, userAgent, contentDisposition, mimeType)

                } else {

                    //Do this, if there is no permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )

                }
            } else {
                //Code for devices below API 23 or Marshmallow
                downloadDialog(url, userAgent, contentDisposition, mimeType)

            }
        }

        fabShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                webView.url + " via " + getString(R.string.app_name)
            )
            val intentChooser = Intent.createChooser(shareIntent, "Share")
            startActivity(intentChooser)
        }
        changeColor(R.color.colorPrimaryDark)


    }

    private fun changeColor(resourseColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(
                applicationContext,
                resourseColor
            )
        }
        val bar: ActionBar? = supportActionBar
        bar?.setBackgroundDrawable(ColorDrawable(resources.getColor(resourseColor)))
    }
    private fun downloadDialog(
        url: String,
        userAgent: String,
        contentDisposition: String,
        mimeType: String
    ) {
        //getting file name from url
        val filename = URLUtil.guessFileName(url, contentDisposition, mimeType)
        //AlertDialog
        val builder = AlertDialog.Builder(this)
        //title for AlertDialog
        builder.setTitle("Download")
        //message of AlertDialog
        builder.setMessage("Do you want to save $filename")
        //if YES button clicks
        builder.setPositiveButton("Yes") { _, _ ->
            //DownloadManager.Request created with url.
            val request = DownloadManager.Request(Uri.parse(url))
            //cookie
            val cookie = CookieManager.getInstance().getCookie(url)
            //Add cookie and User-Agent to request
            request.addRequestHeader("Cookie", cookie)
            request.addRequestHeader("User-Agent", userAgent)
            //file scanned by MediaScanner
            request.allowScanningByMediaScanner()
            //Download is visible and its progress, after completion too.
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //DownloadManager created
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            //Saving file in Download folder
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
            //download enqueued
            downloadManager.enqueue(request)
        }
        //If Cancel button clicks
        builder.setNegativeButton("Cancel")
        { dialog, _ ->
            //cancel the dialog if Cancel clicks
            dialog.cancel()
        }

        val dialog: AlertDialog = builder.create()
        //alertDialog shows
        dialog.show()

    }
}
