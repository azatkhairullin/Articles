package com.boxes.articles.presentation.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.boxes.articles.App
import com.boxes.articles.R
import com.boxes.articles.data.entity.ArticleItem
import com.boxes.articles.di.DaggerArticlesComponent
import com.boxes.articles.presentation.ArticlesContract
import com.boxes.articles.presentation.presenter.ArticlesPresenter
import com.boxes.articles.util.forEach
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.layout_empty.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress.*
import javax.inject.Inject


class ArticlesFragment : Fragment(), ArticlesContract.View {


    @Inject
    lateinit internal var articlesPresenter: ArticlesPresenter
    private val articlesAdapter: ArticlesAdapter = ArticlesAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        retainInstance = true
        articlesPresenter.onViewAttach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onStart() {
        super.onStart()
        articlesPresenter.onViewStart()
    }

    override fun onStop() {
        super.onStop()
        articlesPresenter.onViewStop()
    }

    private fun inject() {
        DaggerArticlesComponent.builder()
                .mainComponent((activity.application as App).mainComponent)
                .build().injectArticlesFragment(this)
    }


    override fun updateItems(items: List<ArticleItem>) {
        articlesAdapter.setItems(items)
    }

    private fun showView(v: View) {
        swipeRefreshLayout.isRefreshing = false
        if (v.visibility != View.VISIBLE) {
            hideAllViews()
            v.visibility = View.VISIBLE
        }
    }

    private fun hideAllViews() {
        fragmentRootLayout.forEach {
            it.visibility = View.GONE
        }
    }

    override fun showProgress() {
        showView(progressLayout)
    }

    override fun showContent() {
        showView(articlesRecyclerView)
    }

    override fun showError() {
        showView(errorLayout)
    }

    override fun showLocationError() {
        Toast.makeText(context, R.string.cant_fetch_location, Toast.LENGTH_SHORT).show()
    }

    override fun showUpdateError() {
        Toast.makeText(context, R.string.connection_problems, Toast.LENGTH_SHORT).show()
    }

    override fun showEmpty() {
        showView(emptyLayout)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articlesRecyclerView.adapter = articlesAdapter
        articlesRecyclerView.layoutManager = LinearLayoutManager(context)
        swipeRefreshLayout.setOnRefreshListener { articlesPresenter.retry() }
    }

    inner class ArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

        private var articleItems: List<ArticleItem> = listOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
            return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false))
        }

        override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
            holder.bind(articleItems[position])
        }

        override fun getItemCount(): Int {
            return articleItems.size
        }

        inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var articleItem: ArticleItem
            val imageTitlesLayout: LinearLayout = itemView.imageTitlesLayout
            val articleTextView: TextView = itemView.articleTextView

            fun bind(articleItem: ArticleItem) {
                this.articleItem = articleItem
                articleTextView.text = articleItem.title

                articleItem.images?.let { images ->
                    imageTitlesLayout.visibility = View.VISIBLE

                    while (imageTitlesLayout.childCount < images.size) {
                        imageTitlesLayout.addView(TextView(context),
                                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT))
                    }

                    for (childIndex in 0 until images.size) {
                        imageTitlesLayout.getChildAt(childIndex).visibility = View.VISIBLE
                    }

                    for (childIndex in 0 until images.size) {
                        (imageTitlesLayout.getChildAt(childIndex) as TextView).text = images[childIndex].title
                    }

                    for (childIndex in images.size until itemView.imageTitlesLayout.childCount) {
                        imageTitlesLayout.getChildAt(childIndex).visibility = View.GONE
                    }


                }

                if (articleItem.images == null) {
                    imageTitlesLayout.visibility = View.GONE
                }
            }
        }

        fun setItems(articleItems: List<ArticleItem>) {
            this.articleItems = articleItems
            notifyDataSetChanged()
        }

    }

}