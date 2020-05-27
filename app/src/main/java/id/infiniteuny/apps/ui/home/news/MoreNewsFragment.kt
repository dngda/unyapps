package id.infiniteuny.apps.ui.home.news

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
import id.infiniteuny.apps.util.*
import kotlinx.android.synthetic.main.more_news_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreNewsFragment : Fragment() {
    private val mViewModel: MoreNewsViewModel by viewModel()
    private val mAdapter = GroupAdapter<ViewHolder>()
    private var rvPosition: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.more_news_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        bindLoadMoreUI()
    }

    private fun bindLoadMoreUI() {
        btn_berita_load_more.setOnClickListener {
            rvPosition =
                (fm_more_RvBerita.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            logD(rvPosition.toString())
            btn_berita_load_more.apply {
                text = requireContext().getString(R.string.loading)
                isEnabled = false
                isClickable = false
                setBackgroundColor(requireContext().getColor(R.color.colorOnSecondary))
                setTextColor(requireContext().getColor(R.color.black))
            }
            mViewModel.page++
            updateUI(mViewModel.page)
        }
    }

    private fun bindUI() = Coroutines.main {
        try {
            mViewModel.newsList.await().observe(viewLifecycleOwner, Observer {
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

    private fun updateUI(page: Int) = Coroutines.main {
        try {
            mViewModel.getNewsListByPageAsync(page).await()
                .observe(viewLifecycleOwner, Observer {
                    initNewsRecyclerView(it.toNewsItem())

                    btn_berita_load_more.apply {
                        text = requireContext().getString(R.string.load_more)
                        isEnabled = true
                        isClickable = true
                        setBackgroundColor(requireContext().getColor(R.color.colorPrimary))
                        setTextColor(requireContext().getColor(R.color.white))
                    }
                })
        } catch (e: ApiException) {
            Log.d("Api", e.message!!)
        } catch (e: NoInternetException) {
            more_root.snackBar(e.message!!)
        }
    }

    private fun initNewsRecyclerView(newsItem: List<NewsItem>) {
        mAdapter.apply {
            addAll(newsItem)
            update(newsItem)
        }

        fm_more_RvBerita.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            scrollToPosition(rvPosition)
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
