package com.assignment.githubrepoapp.errorpage

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assignment.githubrepoapp.R
import com.assignment.githubrepoapp.trendingpage.TrendingActivity
import kotlinx.android.synthetic.main.activity_error_page.*


class ErrorNoInternetActivity : AppCompatActivity(), ErrorNoInternetContract.View {
    private lateinit var presenter: ErrorNoInternetContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_page)

        retry_button.setOnClickListener {
             val intent = Intent(this, TrendingActivity::class.java)
             startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun setPresenter(presenter: ErrorNoInternetContract.Presenter) {
        this.presenter = presenter
    }
}