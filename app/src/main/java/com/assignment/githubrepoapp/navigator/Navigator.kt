package com.assignment.githubrepoapp.navigator

import android.content.Context
import android.content.Intent
import com.assignment.githubrepoapp.errorpage.ErrorNoInternetActivity
import com.assignment.githubrepoapp.trendingpage.TrendingActivity

class Navigator(private val context: Context) {

    fun launchTrendingPage() {
        context.startActivity(
            Intent(context, TrendingActivity::class.java)
        )
    }

    fun launchErrorPage() {
        context.startActivity(
            Intent(context, ErrorNoInternetActivity::class.java)
        )
    }

}