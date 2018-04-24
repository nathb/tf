package com.nathb.tf.ui.presenter

import com.nathb.tf.service.torrent.TestTorrentFetcher
import com.nathb.tf.ui.BasePresenterImpl
import com.nathb.tf.ui.SearchPresenter
import com.nathb.tf.ui.SearchView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchPresenterImpl : BasePresenterImpl<SearchView>(), SearchPresenter {

    override fun search() {
        val torrentFetcher = TestTorrentFetcher()

        val torrentDisposable = db.episodeWithShowDao()
            .findAllNonDownloadedEpisodes()
            .flattenAsFlowable { list -> list }
            .subscribeOn(Schedulers.io())
            .flatMap {
                Flowable.just(it)
                    // Subscribe each episode on its own thread to run in parallel
                    .subscribeOn(Schedulers.io())
                    // Fetch all torrents for each episode
                    .map { torrentFetcher.getTorrents(it.show, it.episode) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { v.addSearchResult(it.toString()) },
                    { v.onSearchError(it) },
                    { v.onSearchComplete() })
        disposable.add(torrentDisposable)
    }

}
