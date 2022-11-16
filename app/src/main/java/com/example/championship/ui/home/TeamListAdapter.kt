package com.example.championship.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.championship.R
import com.example.championship.models.Team


class TeamListAdapter : RecyclerView.Adapter<TeamListAdapter.TeamViewHolder>() {
    private var allTeams: MutableList<Team?> = ArrayList()

    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeamListAdapter.TeamViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.league_row_recycler_view, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamListAdapter.TeamViewHolder, position: Int) {
        val logoToDisplay = if (allTeams[position]?.logo != null) {
            allTeams[position]?.logo.toString()
        } else {
            allTeams[position]?.badge.toString()
        }
        Glide.with(holder.teamLogo)
            .load(logoToDisplay)
            .error(R.drawable.ic_no_image_provided)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<Drawable?>?, isFirstResource: Boolean
                ): Boolean {
                    holder.imageLoader.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageLoader.visibility = View.GONE
                    return false
                }
            })
            .into(holder.teamLogo)

        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                val team = allTeams[position]
                itemClickListener.onClick(team)
            }
        }
    }

    override fun getItemCount(): Int {
        return allTeams.size
    }


    inner class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var teamLogo: ImageView
        var imageLoader: ProgressBar

        init {
            this.itemView.findViewById<View>(itemView.id)
            teamLogo = itemView.findViewById(R.id.row_league_logo)
            imageLoader = itemView.findViewById(R.id.row_image_loader)
        }

    }

    fun updateTeamList(TeamList: List<Team?>) {
        this.allTeams = TeamList as MutableList<Team?>
        notifyDataSetChanged()
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onClick(team: Team?)
    }

}