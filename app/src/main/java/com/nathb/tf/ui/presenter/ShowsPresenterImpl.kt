package com.nathb.tf.ui.presenter

import com.nathb.tf.ui.BasePresenterImpl
import com.nathb.tf.ui.ShowsPresenter
import com.nathb.tf.ui.ShowsView
import com.nathb.tf.ui.view.AddShowDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ShowsPresenterImpl : BasePresenterImpl<ShowsView>(), ShowsPresenter {

    override fun loadShows() {
        disposable.add(
                db
                .showWithEpisodesDao()
                .findAllAsFlowable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { v.onShowsLoaded(it) },
                        { v.onLoadError(it) } ))
    }

    override fun onAddShowClicked() {
        val dialog = AddShowDialogFragment()
        dialog.show(v.getFragmentManager(), "AddShowDialogFragment")
    }
}