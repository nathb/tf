package com.nathb.tf.ui.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.nathb.tf.R
import com.nathb.tf.data.Episode
import com.nathb.tf.data.EpisodeWithShow
import com.nathb.tf.data.Show
import com.nathb.tf.data.ShowWithEpisodes

private const val TYPE_HEADER = 0
private const val TYPE_ROW = 1
private const val EMPTY_EPISODE_ID = -1;

class ShowsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private open class RowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val episodeTitle : TextView = itemView.findViewById(R.id.episode_title)
    }

    private class HeaderViewHolder(itemView: View): RowViewHolder(itemView) {
        val showTitle: TextView = itemView.findViewById(R.id.show_title)
        val arrow : ImageView = itemView.findViewById(R.id.arrow)
        val episodeSection: View = itemView.findViewById(R.id.episode_section)
    }
    private val emptyEpisode = Episode(EMPTY_EPISODE_ID, EMPTY_EPISODE_ID, EMPTY_EPISODE_ID, "", false)
    private var data: MutableList<EpisodeWithShow> = mutableListOf()
    private var showToEpisodes: Map<Show, List<Episode>> = mapOf()
    private var expandedShowIds: MutableSet<Int> = mutableSetOf()

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 ||
                data[position-1].show.showTitle != data[position].show.showTitle) {
            TYPE_HEADER
        } else {
            TYPE_ROW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(layoutInflater.inflate(R.layout.row_show_header, parent, false))
        } else {
            RowViewHolder(layoutInflater.inflate(R.layout.row_episode, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val show = data[position].show
        if (getItemViewType(position) == TYPE_HEADER) {
            val hasEpisodes = data[position].episode != emptyEpisode
            bindShowToHeader(holder as HeaderViewHolder, show, hasEpisodes)
        }

        if (expandedShowIds.contains(show.id)) {
            bindEpisodeToRow(holder as RowViewHolder, data[position].episode)
        }
    }

    fun setData(data: List<ShowWithEpisodes>) {
        // Create Map of Show -> List<Episode> and sort episodes
        showToEpisodes = data
                .associateBy({it.show}, {it.episodes})
                .toSortedMap(compareBy { it.showTitle })
        showToEpisodes.forEach { _, episodes ->
            episodes.sortedWith(
                    compareBy(
                            { it.seasonNumber },
                            { it.episodeNumber }))
        }
        updateData()
    }

    private fun updateData() {
        data.clear()

        // If the show is expanded, add all of its episodes
        // Else only add the first episode to display as a collapsed header
        showToEpisodes.forEach { show, episodes ->
            if (expandedShowIds.contains(show.id)) {
                episodes.forEach {
                    data.add(EpisodeWithShow(it, show))
                }
            } else {
                val episode = if (episodes.isEmpty()) emptyEpisode else episodes[0]
                data.add(EpisodeWithShow(episode, show))
            }
        }

        notifyDataSetChanged()
    }

    private fun bindShowToHeader(holder: HeaderViewHolder, show: Show, hasEpisodes: Boolean) {
        holder.showTitle.text = show.showTitle
        if (expandedShowIds.contains(show.id)) {
            holder.arrow.setImageResource(R.drawable.ic_arrow_up_white_24dp)
            holder.arrow.isVisible = true
            holder.episodeSection.isVisible = true
            holder.arrow.setOnClickListener {
                expandedShowIds.remove(show.id)
                updateData()
            }
        } else {
            holder.arrow.isVisible = hasEpisodes
            holder.arrow.setImageResource(R.drawable.ic_arrow_down_white_24dp)
            holder.episodeSection.isVisible = false
            holder.arrow.setOnClickListener {
                expandedShowIds.add(show.id)
                updateData()
            }
        }
    }

    private fun bindEpisodeToRow(holder: RowViewHolder, episode: Episode) {
        holder.episodeTitle.text = episode.episodeTitle
    }
}