package id.infiniteuny.apps.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import id.infiniteuny.apps.R
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.ui.home.announcement.AnnouncementContentActivity
import id.infiniteuny.apps.ui.home.announcement.AnnouncementItem
import id.infiniteuny.apps.ui.home.news.NewsContentActivity
import id.infiniteuny.apps.ui.home.news.NewsItem
import id.infiniteuny.apps.ui.home.viewpager.FacultyAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindRecyclerUI()
        bindViewPagerUI()
        (activity as AppCompatActivity).apply {
            supportActionBar?.show()
        }
        fm_home_moreBerita.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_moreNewsFragment)
        }
        fm_home_morePengumuman.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_moreAnnouncementFragment)
        }
    }

    private fun bindViewPagerUI() {
        val horizontalInfiniteCycleViewPager =
            requireView().findViewById<View>(R.id.fm_home_viewPager) as HorizontalInfiniteCycleViewPager
        horizontalInfiniteCycleViewPager.adapter = context?.let { FacultyAdapter(it) }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindRecyclerUI() {
        try {
            Coroutines.main {
                mViewModel.lastNewsList.await().observe(this, Observer {
                    shimmer_berita.apply {
                        stopShimmer()
                        visibility = View.INVISIBLE
                    }
                    initNewsRecyclerView(it.toNewsItem())
                })
            }

            Coroutines.main {
                mViewModel.lastAnnouncementList.await().observe(this, Observer {
                    shimmer_pengumuman.apply {
                        stopShimmer()
                        visibility = View.INVISIBLE
                    }
                    initAnnouncementRecyclerView(it.toAnnouncementItem())
                })
            }

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

        mAdapter.setOnItemClickListener { item, view ->
            val uit = item as AnnouncementItem
            Intent(view.context, AnnouncementContentActivity::class.java).also {
                it.putExtra("link", uit.announcement.link)
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

private fun List<Announcement>.toAnnouncementItem(): List<AnnouncementItem> {
    return this.map {
        AnnouncementItem(it)
    }


}
