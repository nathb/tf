package com.nathb.tf.ui.view

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import com.nathb.tf.R
import com.nathb.tf.data.Show
import com.nathb.tf.ui.AddShowView
import com.nathb.tf.ui.presenter.AddShowPresenterImpl
import kotlinx.android.synthetic.main.dialog_add_show.view.*

class AddShowDialogFragment : DialogFragment(), AddShowView {

    private val addShowPresenter = AddShowPresenterImpl()
    private lateinit var fieldMap : Map<AddShowView.Field, EditText>
    private lateinit var fieldsToUpdate : List<EditText>
    private lateinit var titleInput : EditText
    private lateinit var torrentSearchTermInput : EditText
    private lateinit var episodeSearchTermInput : EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        addShowPresenter.onViewCreated(this)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_show, null)
        view.show_title_input.addTextChangedListener(titleTextWatcher)

        titleInput = view.show_title_input
        torrentSearchTermInput = view.torrent_search_term_input
        episodeSearchTermInput = view.episode_search_term_input

        fieldMap = mapOf(
                AddShowView.Field.SHOW_TITLE to titleInput,
                AddShowView.Field.TORRENT_SEARCH_TERM to torrentSearchTermInput,
                AddShowView.Field.EPISODE_SEARCH_TERM to episodeSearchTermInput)

        fieldsToUpdate = listOf(torrentSearchTermInput, episodeSearchTermInput)

        val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.dialog_add_show_title)
                .setView(view)
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.cancel, null)
                .create()

        // Set button listener here to prevent onClick automatically dismissing
        dialog.setOnShowListener {
            val saveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            saveButton.setOnClickListener {
                val show = Show(titleInput.text.toString(),
                        torrentSearchTermInput.text.toString(),
                        episodeSearchTermInput.text.toString())
                addShowPresenter.save(show)
            }
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addShowPresenter.onViewDestroyed()
    }

    override fun onInvalidFields(fields: Set<AddShowView.Field>) {
        fieldMap.forEach { field, editText ->
            editText.setBackgroundColor(
                    if (fields.contains(field)) resources.getColor(R.color.primary) else Color.TRANSPARENT
            )
        }
    }

    override fun onSaveComplete() {
        dialog.dismiss()
    }

    /**
     * Keep Torrent and Episode edit boxes in sync with Title
     * input if not modified as they will most likely be the same
     */
    private val titleTextWatcher = object : TextWatcher {

        private var previousTitle = ""

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            previousTitle = s?.toString() ?: ""
        }

        override fun afterTextChanged(s: Editable?){}

        override fun onTextChanged(newTitle: CharSequence?, start: Int, before: Int, count: Int) {
            newTitle?.let {
                fieldsToUpdate.forEach {
                    if (it.text.isEmpty() || it.text.toString() == previousTitle) {
                        it.setText(newTitle)
                    }
                }
            }
        }
    }
}