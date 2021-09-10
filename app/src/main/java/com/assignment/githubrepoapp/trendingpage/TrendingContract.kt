package com.assignment.githubrepoapp.trendingpage

import com.assignment.githubrepoapp.BasePresenter
import com.assignment.githubrepoapp.BaseView

interface TrendingContract {
    interface Presenter : BasePresenter {

    }

    interface View : BaseView<Presenter> {

    }
}