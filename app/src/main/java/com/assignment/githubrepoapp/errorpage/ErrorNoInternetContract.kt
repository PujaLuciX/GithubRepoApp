package com.assignment.githubrepoapp.errorpage

import com.assignment.githubrepoapp.BasePresenter
import com.assignment.githubrepoapp.BaseView

class ErrorNoInternetContract {
    interface Presenter : BasePresenter {
        fun onRetryButtonClick()
    }

    interface View : BaseView<Presenter> {

    }
}
