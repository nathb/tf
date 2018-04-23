package com.nathb.tf.ui.view

import android.animation.ObjectAnimator
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.addListener
import androidx.core.util.arrayMapOf
import com.nathb.tf.R
import com.nathb.tf.ui.PageType

class DynamicFloatingActionButton : FrameLayout {

    private val pageTransitions = arrayMapOf(

            PageType.SEARCH to {
                fab.show()
                fab.setImageResource(when {
                    isSearching -> R.drawable.ic_loop_white_24dp
                    didSearch -> R.drawable.ic_research_white_24dp
                    else -> R.drawable.ic_search_white_24dp
                })

                if (isSearching) {
                    searchAnimation.start()
                }
            },

            PageType.SHOWS to {
                fab.show()
                searchAnimation.cancel()
                fab.setImageResource(R.drawable.ic_add_white_24dp)
            },

            PageType.SETTINGS to {
                fab.hide()
                searchAnimation.cancel()
            }
    )

    private var type: PageType = PageType.SEARCH
    private var isSearching = false
    private var didSearch = false

    private val fab : FloatingActionButton
    private val searchAnimation: ObjectAnimator
    private var clickListeners: MutableMap<PageType, () -> Unit> = mutableMapOf()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        fab = FloatingActionButton(context, attrs)
        fab.rippleColor = ContextCompat.getColor(context, R.color.primary)
        fab.setImageResource(R.drawable.ic_search_white_24dp)
        addView(fab)

        searchAnimation = ObjectAnimator.ofFloat(fab, View.ROTATION, 0f, 360f).setDuration(1000L)
        searchAnimation.repeatCount = ObjectAnimator.INFINITE
        searchAnimation.repeatMode = ObjectAnimator.REVERSE
        searchAnimation.addListener(
                onStart = {
                    fab.isEnabled = false
                    isSearching = true
                },
                onCancel = {
                    fab.rotation = 0f
                    fab.isEnabled = true
                })

        fab.setOnClickListener {
            clickListeners[type]?.invoke()
            if (type == PageType.SEARCH) {
                fab.setImageResource(R.drawable.ic_loop_white_24dp)
                searchAnimation.start()
            }
        }
    }

    fun setOnClickListenerForPage(page: PageType, l: () -> Unit) {
        clickListeners[page] = l
    }

    fun setPage(type: PageType) {
        this.type = type
        pageTransitions[this.type]?.invoke()
    }

    fun onSearchComplete() {
        searchAnimation.cancel()
        isSearching = false
        didSearch = true
        if (type == PageType.SEARCH) {
            fab.setImageResource(R.drawable.ic_research_white_24dp)
        }
    }

}