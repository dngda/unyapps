package id.infiniteuny.apps.ui.home

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
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val mViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = Coroutines.main {
        try {
            mViewModel.lastNewsList.await().observe(this, Observer {
                shimmer_berita.apply {
                    stopShimmer()
                    visibility = View.GONE
                }
                initRecyclerView(it.toNewsItem())
            })
        } catch (e: ApiException) {
            Log.d("Api", e.message!!)
        }

    }

    private fun initRecyclerView(newsItem: List<NewsItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(newsItem)
        }

        fm_home_RvBerita.apply {
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
