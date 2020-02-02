package id.infiniteuny.apps.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import id.infiniteuny.apps.R
import id.infiniteuny.apps.databinding.ActivityNewsContentBinding
import kotlinx.android.synthetic.main.activity_news_content.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsContentActivity : AppCompatActivity() {
    private val mViewModel: NewsContentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityNewsContentBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_news_content)
        binding.viewModel = mViewModel

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val newsLink = intent.getStringExtra("newsLink")

        mViewModel.getNewsLink(newsLink!!)

        mViewModel.newsContent.observe(this, Observer {
            if (it != null) {
                shimmer_content.stopShimmer()
                shimmer_content.visibility = View.GONE
            }
            Log.d("respon hasil", it.toString())
            content_date.text = it.created_date
            content_title.text = it.title
            content_isi.text = it.content
            Glide.with(this)
                .load(it.featured_image)
                .centerCrop().into(content_headerIV)
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
