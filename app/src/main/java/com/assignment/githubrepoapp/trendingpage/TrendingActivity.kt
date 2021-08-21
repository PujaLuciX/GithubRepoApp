package com.assignment.githubrepoapp.trendingpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assignment.githubrepoapp.R

class TrendingActivity : AppCompatActivity(), TrendingContract.View {
    private lateinit var presenter: TrendingContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending)
        //setPresenter(TrendingPresenter(this, DependencyInjectorImpl()))
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun setPresenter(presenter: TrendingContract.Presenter) {
        this.presenter = presenter
    }

    override fun displayTrendingRepoName(name: String) {
        TODO("Not yet implemented")
    }
}
