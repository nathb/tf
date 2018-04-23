package com.nathb.tf.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nathb.tf.R
import kotlinx.android.synthetic.main.fragment_blank.view.*

class BlankFragment : Fragment() {

    private lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { title = it.getString(TITLE) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        view.fragment_title.text = title
        return view
    }

    companion object {
        private val TITLE = "title"

        fun newInstance(title: String): BlankFragment {
            val fragment = BlankFragment()
            val args = Bundle()
            args.putString(TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
}
