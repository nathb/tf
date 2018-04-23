package com.nathb.tf.ui

import android.support.v4.app.FragmentManager
import com.nathb.tf.data.AppDatabase
import com.nathb.tf.data.Show
import com.nathb.tf.data.ShowWithEpisodes
import io.reactivex.disposables.CompositeDisposable

/**
 * Base
 */
interface BasePresenter<in T : BaseView> {
    fun onViewCreated(view: T)
    fun onViewDestroyed()
}

interface BaseView

abstract class BasePresenterImpl<T : BaseView> : BasePresenter<T> {

    protected val db = AppDatabase.get()
    protected lateinit var disposable: CompositeDisposable
    protected lateinit var v: T

    override fun onViewCreated(view: T) {
        v = view
        disposable = CompositeDisposable()
    }

    override fun onViewDestroyed() {
        disposable.clear()
    }
}

/**
 * Search
 */
interface SearchPresenter : BasePresenter<SearchView> {
    fun search()
}

interface SearchView : BaseView {
    fun addSearchResult(results: String)
    fun onSearchError(error: Throwable)
    fun onSearchComplete()
}

/**
 * Sync
 */
interface SyncPresenter : BasePresenter<SyncView> {
    fun sync()
}

interface SyncView : BaseView {
    fun onSyncError(error: Throwable)
    fun onSyncComplete()
}

/**
 * Shows
 */
interface ShowsPresenter {
    fun loadShows()
    fun onAddShowClicked()
}

interface ShowsView : BaseView {
    fun onShowsLoaded(list: List<ShowWithEpisodes>)
    fun onLoadError(error: Throwable)
    fun getFragmentManager(): FragmentManager?
}

/**
 * Add Show
 */
interface AddShowPresenter : BasePresenter<AddShowView> {
    fun save(show: Show)
}

interface AddShowView : BaseView {
    enum class Field { SHOW_TITLE, TORRENT_SEARCH_TERM, EPISODE_SEARCH_TERM }
    fun onInvalidFields(fields: Set<Field>)
    fun onSaveComplete()
}
