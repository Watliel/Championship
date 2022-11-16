package com.example.championship.ui.home

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.championship.R
import com.example.championship.databinding.LeagueListActivityBinding
import com.example.championship.models.League
import com.example.championship.models.Team
import com.example.championship.ui.teamDetail.DetailTeam
import com.google.android.material.snackbar.Snackbar

class LeagueListActivity : AppCompatActivity() {
    private lateinit var leagueListViewModel: LeagueListViewModel
    private lateinit var leagueListActivityBinding: LeagueListActivityBinding
    private lateinit var leagueAdapter: LeagueListAdapter
    private lateinit var teamAdapter: TeamListAdapter
    private lateinit var toolbarListAct: Toolbar
    private lateinit var searchView: SearchView

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
                leagueListViewModel.geTeamsByLeagueNames(leagueNameSelected)
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
            leagues?.let { it ->
                leagueAdapter.updateLeagueList(it)
                leagueListViewModel.leagueFiltered.addAll(leagues)
                leagueListViewModel.suggestionLeagueList = it.map { it.name } as MutableList<String>
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
                val snack = Snackbar.make(leagueListActivityBinding.leagueListView, "An error occurred", Snackbar.LENGTH_SHORT)
                snack.show()
                /*leagueListActivityBinding.leagueListOnError.visibility =
                    if (it) View.VISIBLE else View.GONE*/
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
        searchView = searchItem.actionView as SearchView
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.search_item_id)
        val cursorAdapter = SimpleCursorAdapter(this, R.layout.suggestion_item_search_view, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        searchView.suggestionsAdapter = cursorAdapter

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
                val cursor =
                    MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                newText.let {
                    leagueListViewModel.suggestionLeagueList.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(newText, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }

        })
        handleClickSearchViewSuggestion()
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

    private fun handleClickSearchViewSuggestion() {
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                var selection = ""
                if (cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1) >= 0) {
                    selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                }
                searchView.setQuery(selection, false)
                return true
            }

            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }
        })
    }

}