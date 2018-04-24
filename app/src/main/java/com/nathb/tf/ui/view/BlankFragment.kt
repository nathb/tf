package com.nathb.tf.ui.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nathb.tf.R
import kotlinx.android.synthetic.main.fragment_blank.view.*

class BlankFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        view.fragment_title.text = "Blank"
        return view
    }

    companion object {
        fun newInstance() = BlankFragment()
    }
}
