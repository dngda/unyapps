package id.infiniteuny.apps.ui.home.news

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import id.infiniteuny.apps.R
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.util.ApiException
import id.infiniteuny.apps.util.Coroutines
import id.infiniteuny.apps.util.NoInternetException
import id.infiniteuny.apps.util.snackBar
import kotlinx.android.synthetic.main.more_news_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreNewsFragment : Fragment() {
    private val mViewModel: MoreNewsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.more_news_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = Coroutines.main {
        try {
            mViewModel.newsList.await().observe(this, Observer {
                shimmer_more_berita.apply {
                    stopShimmer()
                    visibility = View.INVISIBLE
                }
                initNewsRecyclerView(it.toNewsItem())
            })

        } catch (e: ApiException) {
            Log.d("Api", e.message!!)
        } catch (e: NoInternetException) {
            more_root.snackBar(e.message!!)
        }

    }

    private fun initNewsRecyclerView(newsItem: List<NewsItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(newsItem)
        }

        fm_more_RvBerita.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

        mAdapter.setOnItemClickListener { item, view ->
            val uit = item as NewsItem
            Intent(view.context, NewsContentActivity::class.java).also {
                it.putExtra("newsLink", uit.news.link)
                startActivity(it)
            }
        }

    }
}

private fun List<News>.toNewsItem(): List<NewsItem> {
    return this.map {
        NewsItem(it)
    }
}
