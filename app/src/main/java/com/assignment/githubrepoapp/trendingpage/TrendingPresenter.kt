package com.assignment.githubrepoapp.trendingpage

class TrendingPresenter (
    private val view : TrendingContract.View
) : TrendingContract.Presenter {

    init {
        view.setPresenter(this)
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}
