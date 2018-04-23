package com.nathb.tf.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import com.nathb.tf.R
import com.nathb.tf.data.ShowWithEpisodes
import com.nathb.tf.ui.PageType
import com.nathb.tf.ui.ShowsView
import com.nathb.tf.ui.presenter.ShowsPresenterImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_shows.view.*

class ShowsFragment : Fragment(), ShowsView {

    private val showsPresenter = ShowsPresenterImpl()
    private lateinit var adapter: ShowsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shows, container, false)
        adapter = ShowsAdapter()
        view.recyclerview.adapter = adapter
        view.recyclerview.layoutManager = LinearLayoutManager(activity)
        view.recyclerview.itemAnimator

        activity!!.dynamic_fab.setOnClickListenerForPage(
                PageType.SHOWS,
                { showsPresenter.onAddShowClicked() })

        showsPresenter.onViewCreated(this)
        showsPresenter.loadShows()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showsPresenter.onViewDestroyed()
    }

    override fun onShowsLoaded(list: List<ShowWithEpisodes>) {
        adapter.setData(list)
    }

    override fun onLoadError(error: Throwable) {
        context?.toast("Error loading shows: $error")
    }

    companion object {
        fun newInstance(): ShowsFragment = ShowsFragment()
    }
}