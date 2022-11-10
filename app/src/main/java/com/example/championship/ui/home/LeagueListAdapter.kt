package com.example.championship.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.championship.R
import com.example.championship.models.League
import java.util.ArrayList

class LeagueListAdapter: RecyclerView.Adapter<LeagueListAdapter.LeagueViewHolder>(), Filterable {
    private var allLeagues: MutableList<League?> = ArrayList<League?>()
    private var leaguesFiltered: MutableList<League?> = ArrayList<League?>()

    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueListAdapter.LeagueViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.league_row_recycler_view, parent, false)
        return LeagueViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeagueListAdapter.LeagueViewHolder, position: Int) {
        val logoToDisplay = if (allLeagues[position]?.logo != null ) {
            allLeagues[position]?.logo.toString()
        } else {
            allLeagues[position]?.badge.toString()
        }
             Glide.with(holder.leagueLogo)
                 .load(logoToDisplay)
                 .error(R.drawable.ic_launcher_background)
                 .into(holder.leagueLogo)

        holder.itemView.setOnClickListener {
            if(position != RecyclerView.NO_POSITION) {
                val league = leaguesFiltered[position]
                itemClickListener.onClick(league)
            }
        }

    }

    override fun getItemCount(): Int {
        return allLeagues.size
    }


    inner class LeagueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var leagueLogo: ImageView

        init {
            this.itemView.findViewById<View>(itemView.id)
            leagueLogo = itemView.findViewById(R.id.row_league_logo)
        }


    }

    fun updateLeagueList(leagueList: List<League?>) {
        this.allLeagues = leagueList as MutableList<League?>
        this.leaguesFiltered = leagueList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                leaguesFiltered = if (charString.isEmpty()) allLeagues else {
                    val filteredList = ArrayList<League?>()
                    allLeagues
                        .filter {
                            (it?.name?.contains(constraint!!)!!)
                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = leaguesFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                leaguesFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as MutableList<League?>
                notifyDataSetChanged()
            }
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onClick(league: League?)
    }

}