package com.nathb.tf.ui.presenter

import com.nathb.tf.service.episode.TestEpisodeFetcher
import com.nathb.tf.ui.BasePresenterImpl
import com.nathb.tf.ui.SyncPresenter
import com.nathb.tf.ui.SyncView
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class SyncPresenterImpl : BasePresenterImpl<SyncView>(), SyncPresenter {

    override fun sync() {
        val episodeFetcher = TestEpisodeFetcher()
        val episodesDisposable =
                // Find all shows with their episodes
                db.showWithEpisodesDao()
                .findAllAsSingle()
                .flattenAsFlowable { showList -> showList }
                // Subscribe each show on their own thread to run in parallel
                .subscribeOn(Schedulers.io())
                // Fetch all episodes for each show
                .flatMap { showWithEpisodes ->
                    Flowable.just(showWithEpisodes)
                            .flatMapIterable { episodeFetcher.getEpisodes(showWithEpisodes.show) }
                            // Only include unsynced episodes
                            .filter { episode -> !showWithEpisodes.episodes.contains(episode) }
                }
                .observeOn(Schedulers.io())
                .subscribe(
                        { db.episodeDao().insert(it) },
                        { v.onSyncError(it) },
                        { v.onSyncComplete() })

        disposable.addAll(episodesDisposable)
    }

}