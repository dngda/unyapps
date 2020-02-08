package id.infiniteuny.apps.ui.home.announcement

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import id.infiniteuny.apps.R
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.util.ApiException
import id.infiniteuny.apps.util.Coroutines
import id.infiniteuny.apps.util.NoInternetException
import id.infiniteuny.apps.util.snackBar
import kotlinx.android.synthetic.main.more_announcement_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreAnnouncementFragment : Fragment() {
    private val mViewModel: MoreAnnouncementViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.more_announcement_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
        toolbar_morePengumuman.title = "Pengumuman"
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_morePengumuman)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            toolbar_morePengumuman.setNavigationOnClickListener {
                onBackPressed()
            }
        }

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = Coroutines.main {
        try {
            mViewModel.announcementList.await().observe(this, Observer {
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

    private fun initAnnouncementRecyclerView(newsItem: List<AnnouncementItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(newsItem)
        }

        fm_more_RvPengumuman.apply {
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

private fun List<Announcement>.toAnnouncementItem(): List<AnnouncementItem> {
    return this.map {
        AnnouncementItem(it)
    }
}
