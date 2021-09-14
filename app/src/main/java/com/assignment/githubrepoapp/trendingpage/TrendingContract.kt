package com.assignment.githubrepoapp.trendingpage

import com.assignment.githubrepoapp.BasePresenter
import com.assignment.githubrepoapp.BaseView
import com.assignment.githubrepoapp.data.model.RepoListModel

interface TrendingContract {
    interface Presenter : BasePresenter {
        fun getData()

        fun retriveData()

        fun rearrangeListByName()

        fun rearrangeListByStars()
    }

    interface View : BaseView<Presenter> {
        fun stopShimmer()

        fun startShimmer()

        fun showRecyclerView()

        fun notifyAdapterItemInserted(position: Int)

        fun initAdapter(repoListModelArrayList : MutableList<RepoListModel>)

        fun clearAdapterData()
    }
}
