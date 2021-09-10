package com.assignment.githubrepoapp.trendingpage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.assignment.githubrepoapp.R
import com.assignment.githubrepoapp.adapter.RepoListAdapter
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.errorpage.ErrorNoInternetActivity
import kotlinx.android.synthetic.main.activity_trending.*
import org.json.JSONException
import org.json.JSONObject

class TrendingActivity : AppCompatActivity(), TrendingContract.View {
    private lateinit var presenter: TrendingContract.Presenter
    private lateinit var adapter: RepoListAdapter
    private var repoList: MutableList<RepoListModel> = ArrayList()
    private val url: String = "https://private-anon-0e99465514-githubtrendingapi.apiary-mock.com/repositories"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trending)
        shimmerLayout.startShimmer()
        getData()
        recycler_view_repo_list.layoutManager = LinearLayoutManager(this)
        adapter = RepoListAdapter(repoList, context = applicationContext)
        recycler_view_repo_list.adapter = adapter
        pullToRefresh()
    }

    private fun getData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                response ->
                try {
                    stopShimmerShowView()
                    for (i in 0 until response.length()) {
                        val jsonObject : JSONObject = response.getJSONObject(i)
                        repoList.add(
                            RepoListModel(
                                jsonObject.getString("name"),
                                jsonObject.getString("author"),
                                jsonObject.getString("avatar"),
                                jsonObject.getString("description"),
                                jsonObject.getString("language"),
                                jsonObject.getString("languageColor"),
                                jsonObject.getString("stars").toInt(),
                                jsonObject.getString("forks").toInt()
                            )
                        )
                        //Log.d("Puja: ", "Notifying Adapter notifyItemInserted("+i+") $ " + (repoList.size-1))
                        adapter.notifyItemInserted(i)
                    }
                    //Log.d("Puja: ", "Notifying Adapter notifyDataSetChanged()")
                    adapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener {
                error -> error.printStackTrace()
                //Log.d("Puja: ", "No Internet Connection $ ")
                val intent = Intent(this, ErrorNoInternetActivity::class.java)
                startActivity(intent)
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun stopShimmerShowView() {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        recycler_view_repo_list.visibility = View.VISIBLE
    }

    private fun pullToRefresh() {
        swipe_refresh_list.setOnRefreshListener {
            swipe_refresh_list.visibility = View.VISIBLE
            Handler().postDelayed(Runnable {
                swipe_refresh_list.isRefreshing = false
                getData()
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

    override fun setPresenter(presenter: TrendingContract.Presenter) {
        this.presenter = presenter
    }
}
