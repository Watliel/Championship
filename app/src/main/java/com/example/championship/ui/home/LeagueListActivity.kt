package com.example.championship.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.championship.R
import com.example.championship.databinding.LeagueListActivityBinding
import com.example.championship.models.League

class LeagueListActivity : AppCompatActivity() {
    private lateinit var leagueListViewModel: LeagueListViewModel
    private lateinit var leagueListActivityBinding: LeagueListActivityBinding
    private lateinit var leagueAdapter: LeagueListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.league_list_activity)
        leagueListViewModel = ViewModelProvider(this)[LeagueListViewModel::class.java]
        leagueListActivityBinding = LeagueListActivityBinding.inflate(layoutInflater)
        setContentView(leagueListActivityBinding.root)
        initRecyclerView()
        leagueListViewModel.getLeagues()

        leagueAdapter.setItemClickListener(object: LeagueListAdapter.ItemClickListener{
            override fun onClick(league: League?) {
                Log.d("CLICK", league?.name.toString())
                //TODO redirect
            }
        })
        observeViewModel()

    }

    private fun observeViewModel() {
        leagueListViewModel.allLeagues.observe(this, Observer { leagues ->
            leagues?.let { leagueAdapter.updateLeagueList(it) }
        })
    }

    private fun initRecyclerView() {
        leagueListActivityBinding.leagueListRecyclerView.layoutManager = GridLayoutManager(this@LeagueListActivity, 2)
        leagueAdapter = LeagueListAdapter()
        leagueListActivityBinding.leagueListRecyclerView.adapter = leagueAdapter;
    }
/*
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            // on below line we are checking
            // if query exist or not.
            if (programmingLanguagesList.contains(query)) {
                // if query exist within list we
                // are filtering our list adapter.
                listAdapter.filter.filter(query)
            } else {
                // if query is not present we are displaying
                // a toast message as no  data found..
                Toast.makeText(this@HomeActivity, "No Language found..", Toast.LENGTH_LONG)
                    .show()
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            // if query text is change in that case we
            // are filtering our adapter with
            // new text on below line.
            listAdapter.filter.filter(newText)
            return false
        }
    })*/
}