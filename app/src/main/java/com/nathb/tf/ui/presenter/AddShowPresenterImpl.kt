package com.nathb.tf.ui.presenter

import android.text.TextUtils
import com.nathb.tf.data.Show
import com.nathb.tf.ui.AddShowPresenter
import com.nathb.tf.ui.AddShowView
import com.nathb.tf.ui.BasePresenterImpl
import kotlin.concurrent.thread

class AddShowPresenterImpl : BasePresenterImpl<AddShowView>(), AddShowPresenter {

    override fun save(show: Show) {
        val invalidFields = mapOf(
                AddShowView.Field.SHOW_TITLE to show.showTitle,
                AddShowView.Field.TORRENT_SEARCH_TERM to show.torrentSearchTerm,
                AddShowView.Field.TV_MAZE_ID to show.tvMazeId)
                .filterValues { TextUtils.isEmpty(it) }
                .keys

        if (invalidFields.isNotEmpty()) {
            v.onInvalidFields(invalidFields)
        } else {
            thread(start = true) {
                db.showDao().insert(show)
                v.onSaveComplete()
            }
        }
    }
}