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
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.ui.home.announcement.AnnouncementItem
import id.infiniteuny.apps.ui.home.news.NewsContentActivity
import id.infiniteuny.apps.ui.home.news.NewsItem
import id.infiniteuny.apps.util.ApiException
import id.infiniteuny.apps.util.Coroutines
import id.infiniteuny.apps.util.NoInternetException
import id.infiniteuny.apps.util.snackBar
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
        toolbar_home.title = "Home"
        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = Coroutines.main {
        try {
            mViewModel.lastNewsList.await().observe(this, Observer {
                shimmer_berita.apply {
                    stopShimmer()
                    visibility = View.INVISIBLE
                }
                initNewsRecyclerView(it.toNewsItem())
            })

            mViewModel.lastAnnouncementList.await().observe(this, Observer {
                shimmer_pengumuman.apply {
                    stopShimmer()
                    visibility = View.INVISIBLE
                }
                initAnnouncementRecyclerView(it.toAnnouncementItem())
            })
        } catch (e: ApiException) {
            Log.d("Api", e.message!!)
        } catch (e: NoInternetException) {
            hm_root_layout.snackBar(e.message!!)
        }

    }

    private fun initNewsRecyclerView(newsItem: List<NewsItem>) {
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

    private fun initAnnouncementRecyclerView(announcementItem: List<AnnouncementItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(announcementItem)
        }

        fm_home_RvPengumuman.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

//        mAdapter.setOnItemClickListener { item, view ->
//            val uit = item as NewsItem
//            Intent(view.context, NewsContentActivity::class.java).also {
//                it.putExtra("newsLink", uit.news.link)
//                startActivity(it)
//            }
//        }

    }

}

private fun List<News>.toNewsItem(): List<NewsItem> {
    return this.map {
        NewsItem(it)
    }
}

private fun List<Announcement>.toAnnouncementItem(): List<AnnouncementItem> {
    return this.map {
        AnnouncementItem(it)
    }
}
