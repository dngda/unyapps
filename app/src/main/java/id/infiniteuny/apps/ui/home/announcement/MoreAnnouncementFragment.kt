package id.infiniteuny.apps.ui.home.announcement

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
import id.infiniteuny.apps.util.*
import kotlinx.android.synthetic.main.more_announcement_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreAnnouncementFragment : Fragment() {
    private val mViewModel: MoreAnnouncementViewModel by viewModel()
    private val mAdapter = GroupAdapter<ViewHolder>()
    private var rvPosition: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.more_announcement_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        initLoadMoreUI()
    }

    private fun initLoadMoreUI() {
        btn_ann_load_more.setOnClickListener {
            rvPosition =
                (fm_more_RvPengumuman.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            logD(rvPosition.toString())
            btn_ann_load_more.apply {
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
            mViewModel.announcementList.await().observe(viewLifecycleOwner, Observer {
                shimmer_more_pengumuman.apply {
                    stopShimmer()
                    visibility = View.INVISIBLE
                }
                initAnnouncementRecyclerView(it.toAnnouncementItem())
            })

        } catch (e: ApiException) {
            Log.d("Api", e.message!!)
        } catch (e: NoInternetException) {
            more_root.snackBar(e.message!!)
        }
    }

    private fun updateUI(page: Int) = Coroutines.main {
        try {
            mViewModel.getAnnouncementListByPageAsync(page).await()
                .observe(viewLifecycleOwner, Observer {
                    initAnnouncementRecyclerView(it.toAnnouncementItem())

                    btn_ann_load_more.apply {
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

    private fun initAnnouncementRecyclerView(newsItem: List<AnnouncementItem>) {
        mAdapter.apply {
            addAll(newsItem)
            update(newsItem)
        }

        fm_more_RvPengumuman.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            scrollToPosition(rvPosition)
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

private fun List<Announcement>.toAnnouncementItem(): List<AnnouncementItem> {
    return this.map {
        AnnouncementItem(it)
    }
}
