package id.infiniteuny.apps.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import id.infiniteuny.apps.R
import kotlinx.android.synthetic.main.activity_announcement_content.*
import kotlinx.android.synthetic.main.activity_faculty_content.*
import kotlinx.android.synthetic.main.activity_faculty_content.fabShare
import kotlinx.android.synthetic.main.activity_faculty_content.webView
import kotlinx.android.synthetic.main.loading_layout.*

class FacultyContentActivity : AppCompatActivity() {


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_content)


        supportActionBar?.hide()

        Glide.with(applicationContext).asGif().load(R.drawable.loading).into(loading)

        val url = intent.getStringExtra("link")
        val userAgent = System.getProperty("http.agent")

        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                loadingContainer.visibility = View.GONE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipe.isRefreshing = false
            }
        }
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.userAgentString = userAgent
        webView.loadUrl(url)

        swipe.setOnRefreshListener {
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