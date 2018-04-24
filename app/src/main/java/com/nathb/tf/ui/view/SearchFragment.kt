package com.nathb.tf.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nathb.tf.R
import com.nathb.tf.ui.*
import com.nathb.tf.ui.presenter.SearchPresenterImpl
import com.nathb.tf.ui.presenter.SyncPresenterImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*


class SearchFragment : Fragment(), SearchView, SyncView {

    private val syncPresenter = SyncPresenterImpl()
    private val searchPresenter = SearchPresenterImpl()
    private lateinit var dynamicFab : DynamicFloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        view.fragment_title.text = "Search"
        dynamicFab = activity!!.dynamic_fab // activity is not null onCreateView
        dynamicFab.setOnClickListenerForPage(PageType.SEARCH, { syncPresenter.sync() })
        syncPresenter.onViewCreated(this)
        searchPresenter.onViewCreated(this)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        syncPresenter.onViewDestroyed()
        searchPresenter.onViewDestroyed()
    }

    override fun addSearchResult(results: String) {
        fragment_title.text = results
    }

    override fun onSearchError(error: Throwable) {
        fragment_title.text = "Search Error $error"
        dynamicFab.onSearchComplete()
    }

    override fun onSearchComplete() {
        dynamicFab.onSearchComplete()
    }

    override fun onSyncError(error: Throwable) {
        fragment_title.text = "Sync Error $error"
        dynamicFab.onSearchComplete()
    }

    override fun onSyncComplete() {
        searchPresenter.search()
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}