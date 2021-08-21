package com.assignment.githubrepoapp

import com.assignment.githubrepoapp.trendingpage.TrendingContract
import io.mockk.impl.annotations.MockK

class TrendingPresenterTest {
    private lateinit var presenter: TrendingContract.Presenter

    @MockK
    private lateinit var view: TrendingContract.View

}