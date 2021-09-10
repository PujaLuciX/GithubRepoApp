package com.assignment.githubrepoapp.errorpage

import android.content.Context
import com.assignment.githubrepoapp.navigator.Navigator

class ErrorNoInternetPresenter (
    private var view : ErrorNoInternetContract.View?,
    private val context : Context,
    private val navigator : Navigator
) : ErrorNoInternetContract.Presenter {

    override fun onRetryButtonClick() {
        navigator.launchTrendingPage()
    }

    override fun onDestroy() {
        view = null
    }

    companion object {
        fun createAndAttach(
            view : ErrorNoInternetContract.View,
            context : Context,
            navigator: Navigator
        ): ErrorNoInternetContract.Presenter {
            val errorNoInternetPresenter = ErrorNoInternetPresenter(
                view,
                context,
                navigator
            )
            view.setPresenter(errorNoInternetPresenter)
            return errorNoInternetPresenter
        }
    }
}
