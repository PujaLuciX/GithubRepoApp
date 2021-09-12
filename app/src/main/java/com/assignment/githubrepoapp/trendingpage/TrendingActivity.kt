package com.assignment.githubrepoapp.trendingpage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MenuInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.PopupMenu
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
        threeDotClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun threeDotClick() {
        header.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            //Log.i("Puja: ","Event Action = "+event.action)
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (event.rawX >= header.getPaddingRight() - header.getCompoundDrawables().get(DRAWABLE_RIGHT).getBounds().width()) {
                    displayPopUp(v)
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    private fun displayPopUp(v: View) {
        Log.i("Puja: ", "Displaying pop up !!")
        val popupMenu = PopupMenu(this, v, Gravity.END)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_name -> rearrangeListByName()
                R.id.sort_stars -> rearrangeListByStars()
            }
            true
        }
        popupMenu.show()
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

    private fun rearrangeListByName() {
        repoList.sortBy{it.name}
        notifyAdapterDataSetChanged()
    }

    private fun rearrangeListByStars() {
        repoList.sortByDescending{it.stars}
        notifyAdapterDataSetChanged()
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
