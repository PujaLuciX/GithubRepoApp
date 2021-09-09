package com.assignment.githubrepoapp.trendingpage

import com.assignment.githubrepoapp.BasePresenter
import com.assignment.githubrepoapp.BaseView

interface TrendingContract {
    interface Presenter : BasePresenter {
        fun loadErrorScreen()
    }

    interface View : BaseView<Presenter> {
        fun displayTrendingRepoName(name : String)
    }
    interface Navigator {
        fun launchErrorScreen()
        fun launchTrendingScreen()
    }
}