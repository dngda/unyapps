package id.infiniteuny.apps.ui.home.news

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
import id.infiniteuny.apps.util.NoInternetException
import id.infiniteuny.apps.util.snackBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsContentActivity : AppCompatActivity() {
    private val mViewModel: NewsContentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityNewsContentBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_news_content)
        binding.viewModel = mViewModel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val newsLink = intent.getStringExtra("newsLink")

        try {
            mViewModel.getNewsLink(newsLink!!)
        } catch (e: NoInternetException) {
            binding.root.snackBar(e.message!!)
        }


        mViewModel.newsContent.observe(this, Observer {
            if (it != null) {
                binding.shimmerContent.stopShimmer()
                binding.shimmerContent.visibility = View.GONE
            }
            Log.d("respon hasil", it.toString())
            binding.contentDate.text = it.created_date
            binding.contentTitle.text = it.title
            binding.contentIsi.text = it.content
            Glide.with(this)
                .load(it.featured_image)
                .centerCrop().into(binding.contentHeaderIV)
        })

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
