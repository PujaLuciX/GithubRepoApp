package com.assignment.githubrepoapp.trendingpage

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.githubrepoapp.R
import com.assignment.githubrepoapp.adapter.RepoListAdapter
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.navigator.Navigator
import kotlinx.android.synthetic.main.activity_trending.*

class TrendingActivity : AppCompatActivity(), TrendingContract.View {
    private lateinit var presenter: TrendingContract.Presenter
    private lateinit var adapter: RepoListAdapter
    private var repoList: MutableList<RepoListModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending)
        TrendingPresenter.createAndAttach(this, applicationContext, navigator = Navigator(this))
        startShimmer()
        presenter.getData()
        recycler_view_repo_list.layoutManager = LinearLayoutManager(this)
        adapter = RepoListAdapter(repoList, context = applicationContext)
        recycler_view_repo_list.adapter = adapter
        pullToRefresh()
    }

    override fun stopShimmer() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
    }

    override fun startShimmer() {
        shimmerLayout.startShimmer()
    }

    override fun showRecyclerView() {
        recycler_view_repo_list.visibility = View.VISIBLE
    }

    override fun notifyAdapterItemInserted(position: Int, repoModel: RepoListModel) {
        Log.d("Puja: ", "Notifying Adapter notifyItemInserted ($position) $ in activity")
        repoList.add(repoModel)
        adapter.notifyItemInserted(position)
    }

    override fun notifyAdapterDataSetChanged() {
        Log.d("Puja: ", "Notifying Adapter notifyDataSetChanged() in activity")
        adapter.notifyDataSetChanged()
    }

    override fun clearAdapterData() {
        recycler_view_repo_list.setAdapter(null)
    }

    override fun setPresenter(
        presenter: TrendingContract.Presenter
    ) {
        this.presenter = presenter
    }

    private fun pullToRefresh() {
        Log.d("Puja: ", "Pull to refresh !!")
        swipe_refresh_list.setOnRefreshListener {
            swipe_refresh_list.visibility = View.VISIBLE
            Handler().postDelayed({
                swipe_refresh_list.isRefreshing = false
                repoList.clear()
                startShimmer()
                presenter.getData()
            }, 4000)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(outState: Bundle) {
        super.onRestoreInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
