package id.infiniteuny.apps.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.infiniteuny.apps.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreNewsFragment : Fragment() {
    private val mViewModel: NewsContentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.more_news_fragment, container, false)
    }

}
