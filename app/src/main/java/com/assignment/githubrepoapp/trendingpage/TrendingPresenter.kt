package com.assignment.githubrepoapp.trendingpage

import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.assignment.githubrepoapp.cache.DatabaseOpenHelper
import com.assignment.githubrepoapp.data.JsonArrayRequestWrapper
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.navigator.Navigator
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class TrendingPresenter constructor(
    private var view : TrendingContract.View?,
    private val navigator : Navigator,
    private val dbHelper: DatabaseOpenHelper,
    private val sharedPreferences: SharedPreferences,
    private val repoListModelArrayList: ArrayList<RepoListModel> = ArrayList()
) : TrendingContract.Presenter {

    init {
        view?.setPresenter(this)
    }

    override fun getData() {
        if (checkCacheNotExpired()) {
            repoListModelArrayList.addAll(dbHelper.getReposFromDb())
            view?.initAdapter(repoListModelArrayList)
        } else {
            view?.startShimmer()
            view?.retrieveData()
        }
    }

    override fun onSuccess(response: JSONArray) {
        repoListModelArrayList.clear()
        view?.initAdapter(repoListModelArrayList)
        try {
            dbHelper.deleteDb()
            view?.stopShimmer()
            view?.showRecyclerView()
            for (i in 0 until response.length()) {
                val jsonObject: JSONObject = response.getJSONObject(i)
                val repo = RepoListModel(
                    jsonObject.getString(NAME),
                    jsonObject.getString(AUTHOR),
                    jsonObject.getString(AVATAR),
                    jsonObject.getString(DESCRIPTION),
                    jsonObject.getString(LANGUAGE),
                    jsonObject.getString(LANGUAGE_COLOR),
                    jsonObject.getString(STARS).toInt(),
                    jsonObject.getString(FORKS).toInt()
                )
                //Log.d("Puja: ", "Notifying Adapter notifyItemInserted ( $i ) in presenter")
                repoListModelArrayList.add(repo)
                view?.notifyAdapterItemInserted(i)
                dbHelper.saveReposToDb(repo)
            }
            sharedPreferences.edit().putLong(CACHE_TIME, Date().time)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onFailure(error: Any) {
        //printStackTrace(error)
        navigator.launchErrorPage()
    }

    private fun checkCacheNotExpired() : Boolean {
        val cacheTime = sharedPreferences.getLong(CACHE_TIME, 0)
        return (cacheTime != 0L && TimeUnit.HOURS.convert (
            Date().time - cacheTime, TimeUnit.MILLISECONDS) < 2)
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
        view?.clearAdapterData()
        view = null
    }

    companion object {
        private const val NAME = "name"
        private const val AUTHOR = "author"
        private const val AVATAR = "avatar"
        private const val DESCRIPTION = "description"
        private const val LANGUAGE = "language"
        private const val LANGUAGE_COLOR = "languageColor"
        private const val STARS = "stars"
        private const val FORKS = "forks"
        private val CACHE_TIME = "cacheTime"
    }
}
