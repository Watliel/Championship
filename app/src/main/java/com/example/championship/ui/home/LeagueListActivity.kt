package com.example.championship.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.championship.R
import com.example.championship.databinding.LeagueListActivityBinding
import com.example.championship.models.League
import com.example.championship.models.Team
import com.example.championship.ui.teamDetail.DetailTeam

class LeagueListActivity : AppCompatActivity() {
    private lateinit var leagueListViewModel: LeagueListViewModel
    private lateinit var leagueListActivityBinding: LeagueListActivityBinding
    private lateinit var leagueAdapter: LeagueListAdapter
    private lateinit var teamAdapter: TeamListAdapter
    private lateinit var toolbarListAct: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        leagueListViewModel = ViewModelProvider(this)[LeagueListViewModel::class.java]
        leagueListActivityBinding = LeagueListActivityBinding.inflate(layoutInflater)
        setContentView(leagueListActivityBinding.root)
        toolbarListAct = findViewById(R.id.league_list_toolbar)
        toolbarListAct.title = ""
        setSupportActionBar(toolbarListAct)

        initRecyclerView()
        initRecyclerViewTeam()
        leagueListViewModel.getLeagues()

        toolbarListAct.setNavigationOnClickListener { onBackPressed() }

        leagueAdapter.setItemClickListener(object : LeagueListAdapter.ItemClickListener {
            override fun onClick(league: League?) {
                val leagueNameSelected = league?.name.toString().replace(" ", "_")
                leagueListViewModel.geTeamsLeaguesByNames(leagueNameSelected)
            }
        })

        teamAdapter.setItemClickListener(object : TeamListAdapter.ItemClickListener {
            override fun onClick(team: Team?) {
                val teamNameSelected = team?.name.toString().replace(" ", "_")
                team?.let { leagueListViewModel.geTeamDetail(teamNameSelected) }
            }
        })

        observeViewModel()
    }

    private fun observeViewModel() {
        leagueListViewModel.allLeagues.observe(this) { leagues ->
            leagues?.let {
                leagueAdapter.updateLeagueList(it)
                leagueListViewModel.leagueFiltered.addAll(leagues)
            }
        }
        leagueListViewModel.loading.observe(this) { isLoading ->
            isLoading?.let {
                leagueListActivityBinding.leagueListLoading.visibility =
                    if (it) View.VISIBLE else View.GONE
            }
        }
        leagueListViewModel.leagueLoadError.observe(this) { isError ->
            isError?.let {
                leagueListActivityBinding.leagueListOnError.visibility =
                    if (it) View.VISIBLE else View.GONE
            }
        }
        leagueListViewModel.teamsInLeague.observe(this) { teams ->
            teams?.let {
                invalidateOptionsMenu()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                leagueListActivityBinding.leagueListRecyclerView.adapter = teamAdapter
                teamAdapter.updateTeamList(it)
            }
        }
        leagueListViewModel.teamDetailLoaded.observe(this) {
            val intent = Intent(this@LeagueListActivity, DetailTeam::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        leagueListActivityBinding.leagueListRecyclerView.layoutManager =
            GridLayoutManager(this@LeagueListActivity, 2)
        leagueAdapter = LeagueListAdapter()
        leagueListActivityBinding.leagueListRecyclerView.adapter = leagueAdapter
    }

    private fun initRecyclerViewTeam() {
        leagueListActivityBinding.leagueListRecyclerView.layoutManager =
            GridLayoutManager(this@LeagueListActivity, 2)
        teamAdapter = TeamListAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu!!.findItem(R.id.toolbar_menu_search)
        searchItem.isVisible =
            leagueListActivityBinding.leagueListRecyclerView.adapter is LeagueListAdapter
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length == 0 || newText == null) {
                    leagueAdapter.leaguesFiltered.clear()
                    leagueAdapter.leaguesFiltered.addAll(leagueListViewModel.leagueFiltered)
                    leagueAdapter.notifyDataSetChanged()
                }
                if (newText!!.trim().isNotEmpty()) {
                    leagueAdapter.leaguesFiltered.clear()
                    val textSearched = newText.trim().lowercase()
                    val leagueFiltered = leagueListViewModel.leagueFiltered.filter {
                        it?.name?.lowercase()
                            ?.contains(textSearched)!!
                    } as MutableList
                    leagueAdapter.leaguesFiltered.addAll(leagueFiltered)
                    leagueAdapter.notifyDataSetChanged()
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_search -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        invalidateOptionsMenu()
        leagueListActivityBinding.leagueListRecyclerView.adapter = leagueAdapter
    }

}