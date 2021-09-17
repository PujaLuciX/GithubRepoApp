package com.assignment.githubrepoapp.trendingpage

import com.assignment.githubrepoapp.BasePresenter
import com.assignment.githubrepoapp.BaseView
import com.assignment.githubrepoapp.data.model.RepoListModel
import org.json.JSONArray

interface TrendingContract {
    interface Presenter : BasePresenter {
        fun getData()

        fun onSuccess(response: JSONArray)

        fun onFailure(error: Any)

        fun rearrangeListByName()

        fun rearrangeListByStars()
    }

    interface View : BaseView<Presenter> {
        fun stopShimmer()

        fun startShimmer()

        fun showRecyclerView()

        fun retrieveData()

        fun notifyAdapterItemInserted(position: Int)

        fun initAdapter(repoListModelArrayList: MutableList<RepoListModel>)

        fun clearAdapterData()
    }
}
