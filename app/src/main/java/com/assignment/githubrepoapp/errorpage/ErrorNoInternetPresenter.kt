package com.assignment.githubrepoapp.errorpage

import com.assignment.githubrepoapp.trendingpage.TrendingContract

class ErrorNoInternetPresenter (
    private val view : ErrorNoInternetContract.View
) : ErrorNoInternetContract.Presenter {
    init {
        view.setPresenter(this)
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}