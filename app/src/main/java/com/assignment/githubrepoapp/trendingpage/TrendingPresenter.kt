package com.assignment.githubrepoapp.trendingpage

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.navigator.Navigator
import org.json.JSONException
import org.json.JSONObject

class TrendingPresenter (
    private var view : TrendingContract.View?,
    private val context : Context,
    private val navigator : Navigator
) : TrendingContract.Presenter {

    private val url: String = "https://private-anon-0e99465514-githubtrendingapi.apiary-mock.com/repositories"

    override fun getData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                    response ->
                try {
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
                        Log.d("Puja: ", "Notifying Adapter notifyItemInserted ( $i ) $ in presenter ")
                        view?.notifyAdapterItemInserted(i, repo)
                    }
                    Log.d("Puja: ", "Notifying Adapter notifyDataSetChanged() in presenter")
                    view?.notifyAdapterDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener {
                error -> error.printStackTrace()
                navigator.launchErrorPage()
            }
        )
        requestQueue.add(jsonArrayRequest)
    }

    override fun onDestroy() {
        view = null
    }

    companion object {
        fun createAndAttach(
            view : TrendingContract.View,
            context : Context,
            navigator: Navigator
        ): TrendingContract.Presenter {
            val trendingPresenter = TrendingPresenter(
                view,
                context,
                navigator
            )
            view.setPresenter(trendingPresenter)
            return trendingPresenter
        }
    }
}
