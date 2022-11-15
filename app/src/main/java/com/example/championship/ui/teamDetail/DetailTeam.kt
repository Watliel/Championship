package com.example.championship.ui.teamDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.championship.R
import com.example.championship.databinding.ActivityDetailTeamBinding
import com.example.championship.models.OneTeam

class DetailTeam : AppCompatActivity() {

    private lateinit var detailTeamViewModel: DetailTeamViewModel
    private lateinit var detailTeamBinding: ActivityDetailTeamBinding
    private lateinit var toolbarDetailTeam: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailTeamViewModel = ViewModelProvider(this)[DetailTeamViewModel::class.java]
        detailTeamBinding = ActivityDetailTeamBinding.inflate(layoutInflater)

        setContentView(detailTeamBinding.root)
        toolbarDetailTeam = findViewById(R.id.detail_team_toolbar)
        toolbarDetailTeam.title = OneTeam.team.name
        toolbarDetailTeam.title

        setSupportActionBar(toolbarDetailTeam)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setTeamDetail()
    }

    private fun setTeamDetail() {
        detailTeamBinding.detailTeamLeague.text = OneTeam.team.league
        detailTeamBinding.detailTeamCountry.text = OneTeam.team.country
        detailTeamBinding.detailTeamDescription.text = OneTeam.team.description

        val banner = if(OneTeam.team.banner != null) OneTeam.team.banner else OneTeam.team.badge
        Glide.with(detailTeamBinding.detailTeamBanner)
            .load(banner)
            .error(R.drawable.ic_launcher_background)
            .into(detailTeamBinding.detailTeamBanner)
    }

}