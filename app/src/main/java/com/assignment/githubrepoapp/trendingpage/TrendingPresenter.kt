package com.assignment.githubrepoapp.trendingpage

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.assignment.githubrepoapp.cache.DatabaseOpenHelper
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.navigator.Navigator
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class TrendingPresenter (
    private var view : TrendingContract.View?,
    private val context : Context,
    private val navigator : Navigator,
    private val dbHelper: DatabaseOpenHelper,
    private val sharedPreferences: SharedPreferences
) : TrendingContract.Presenter {

    private val url: String = "https://private-anon-0e99465514-githubtrendingapi.apiary-mock.com/repositories"
    private var repoListModelArrayList = ArrayList<RepoListModel>()

    override fun getData() {
        if (checkDuration()) {
            repoListModelArrayList.addAll(dbHelper.getReposFromDb())
            view?.initAdapter(repoListModelArrayList)
        } else {
            view?.startShimmer()
            retriveData()
        }
    }

    override fun retriveData() {
        repoListModelArrayList = ArrayList()
        view?.initAdapter(repoListModelArrayList)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                try {
                    dbHelper.deleteDb()
                    view?.stopShimmer()
                    view?.showRecyclerView()
                    for (i in 0 until response.length()) {
                        val jsonObject : JSONObject = response.getJSONObject(i)
                        val repo = RepoListModel(
                            jsonObject.getString("name"),
                            jsonObject.getString("author"),
                            jsonObject.getString("avatar"),
                            jsonObject.getString("description"),
                            jsonObject.getString("language"),
                            jsonObject.getString("languageColor"),
                            jsonObject.getString("stars").toInt(),
                            jsonObject.getString("forks").toInt()
                        )
                        Log.d(
                            "Puja: ",
                            "Notifying Adapter notifyItemInserted ( $i ) $ in presenter "
                        )
                        repoListModelArrayList.add(repo)
                        view?.notifyAdapterItemInserted(i)
                        dbHelper.saveReposToDb(repo)
                    }
                    sharedPreferences.edit().putLong("cacheTime", Date().time)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                error.printStackTrace()
                navigator.launchErrorPage()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    private fun checkDuration() : Boolean {
        val cacheTime = sharedPreferences.getLong("cacheTime", 0)
        return (cacheTime != 0L && TimeUnit.HOURS.convert (Date().time - cacheTime, TimeUnit.MILLISECONDS) < 2)
    }

    override fun rearrangeListByName() {
        repoListModelArrayList.sortBy{it.name}
        view?.initAdapter(repoListModelArrayList)
    }

    override fun rearrangeListByStars() {
        repoListModelArrayList.sortByDescending{it.stars}
        view?.initAdapter(repoListModelArrayList)
    }

    override fun onDestroy() {
        view = null
        view?.clearAdapterData()
    }

    companion object {
        fun createAndAttach(
            view : TrendingContract.View,
            context : Context,
            navigator: Navigator,
            dbHelper: DatabaseOpenHelper,
            sharedPreferences: SharedPreferences
        ): TrendingContract.Presenter {
            val trendingPresenter = TrendingPresenter(
                view,
                context,
                navigator,
                dbHelper,
                sharedPreferences
            )
            view.setPresenter(trendingPresenter)
            return trendingPresenter
        }
    }
}
