package id.infiniteuny.apps.ui.home.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import id.infiniteuny.apps.R
import id.infiniteuny.apps.data.db.entities.Announcement
import id.infiniteuny.apps.ui.home.announcement.AnnouncementContentActivity
import id.infiniteuny.apps.ui.home.announcement.AnnouncementItem
import id.infiniteuny.apps.ui.home.announcement.MoreAnnouncementViewModel
import id.infiniteuny.apps.util.ApiException
import id.infiniteuny.apps.util.Coroutines
import id.infiniteuny.apps.util.NoInternetException
import id.infiniteuny.apps.util.snackBar
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.more_announcement_fragment.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val mViewModel: MoreAnnouncementViewModel by viewModel()
    private val mAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       showSoftKeyboard(fm_home_search)
        fm_home_search.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                bindUI(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun bindUI(title :String) = Coroutines.main {
        try {
            mViewModel.announcementList.await().observe(viewLifecycleOwner, Observer {

                var test = mutableListOf<Announcement>()
                for (data in it) {
                    var news = data.title?.toLowerCase(Locale.ROOT)
                    if (news?.contains(title)!!) {
                        test.add(data)

                    }
                }
                initAnnouncementRecyclerView(test.toAnnouncementItem())

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

        searchRv.apply {
            layoutManager = LinearLayoutManager(context)
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
private fun List<Announcement>.toAnnouncementItem(): List<AnnouncementItem> {
    return this.map {
        AnnouncementItem(it)
    }
}