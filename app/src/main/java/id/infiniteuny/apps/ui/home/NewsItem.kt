package id.infiniteuny.apps.ui.home

import com.xwray.groupie.databinding.BindableItem
import id.infiniteuny.apps.R
import id.infiniteuny.apps.data.db.entities.News
import id.infiniteuny.apps.databinding.ItemNewsBinding

class NewsItem(
    val news: News
) : BindableItem<ItemNewsBinding>() {
    override fun getLayout(): Int = R.layout.item_news

    override fun bind(viewBinding: ItemNewsBinding, position: Int) {
        viewBinding.news = news
    }

}