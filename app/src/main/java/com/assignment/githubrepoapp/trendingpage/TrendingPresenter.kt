package com.assignment.githubrepoapp.trendingpage

class TrendingPresenter (
    private val view : TrendingContract.View
) : TrendingContract.Presenter {

    lateinit var navigator: TrendingContract.Navigator
    init {
        view?.setPresenter(this)
    }

    override fun loadErrorScreen() {
        navigator.launchErrorScreen()
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}
