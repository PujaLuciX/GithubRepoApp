package com.assignment.githubrepoapp.trendingpage

import com.assignment.githubrepoapp.BasePresenter
import com.assignment.githubrepoapp.BaseView
import com.assignment.githubrepoapp.data.model.RepoListModel

interface TrendingContract {
    interface Presenter : BasePresenter {
        fun getData()
    }

    interface View : BaseView<Presenter> {
        fun stopShimmer()

        fun startShimmer()

        fun showRecyclerView()

        fun notifyAdapterItemInserted(position: Int, repoModel: RepoListModel)

        fun notifyAdapterDataSetChanged()

        fun clearAdapterData()
    }
}
