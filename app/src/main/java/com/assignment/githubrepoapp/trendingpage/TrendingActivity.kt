package com.assignment.githubrepoapp.trendingpage

import android.annotation.SuppressLint
import android.content.Context
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
import com.assignment.githubrepoapp.cache.DatabaseOpenHelper
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.navigator.Navigator
import kotlinx.android.synthetic.main.activity_trending.*

class TrendingActivity : AppCompatActivity(), TrendingContract.View {
    private lateinit var presenter: TrendingContract.Presenter
    private lateinit var adapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending)
        TrendingPresenter.createAndAttach(this, applicationContext, navigator = Navigator(this),
            dbHelper = DatabaseOpenHelper(this), sharedPreferences = getSharedPreferences("gitAppSharedPref", Context.MODE_PRIVATE)
        )
        presenter.getData()
        pullToRefresh()
        threeDotClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun threeDotClick() {
        header.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
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
        val popupMenu = PopupMenu(this, v, Gravity.END)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.sort_name -> presenter.rearrangeListByName()
                R.id.sort_stars -> presenter.rearrangeListByStars()
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

    override fun notifyAdapterItemInserted(position: Int) {
        adapter.notifyItemInserted(position)
    }

    override fun initAdapter(repoListModelArrayList : MutableList<RepoListModel>) {
        recycler_view_repo_list.layoutManager = LinearLayoutManager(this)
        adapter = RepoListAdapter(repoListModelArrayList, context = applicationContext)
        recycler_view_repo_list.adapter = adapter
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
                presenter.retriveData()
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
