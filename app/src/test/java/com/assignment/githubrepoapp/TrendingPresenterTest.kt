package com.assignment.githubrepoapp

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.assignment.githubrepoapp.cache.DatabaseOpenHelper
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.navigator.Navigator
import com.assignment.githubrepoapp.trendingpage.TrendingContract
import com.assignment.githubrepoapp.trendingpage.TrendingPresenter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class TrendingPresenterTest {

    private lateinit var presenter: TrendingContract.Presenter

    @MockK
    private var view: TrendingContract.View? = null

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var navigator: Navigator

    @MockK
    private lateinit var dbHelper: DatabaseOpenHelper

    @MockK
    private lateinit var sharedPreferences: SharedPreferences

    @MockK
    private var requestQueue: RequestQueue? = null

    private var repoListModelArrayList = ArrayList<RepoListModel>()

    private lateinit var jsonObject: JSONObject

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        MockKAnnotations.init(this, true)
        requestQueue = Volley.newRequestQueue(context)
        mockWebServer.start(8080)
        presenter = TrendingPresenter(view, context, navigator, dbHelper, sharedPreferences)
    }

    @Test
    fun testGetDataIfCacheNotExpired() {
        every { checkCacheNotExpired() }.returns(true)
        presenter.getData()
        verify {
            repoListModelArrayList.addAll(dbHelper.getReposFromDb())
            view?.initAdapter(repoListModelArrayList)
        }
    }


    @Test
    fun testGetDataIfCacheExpired() {
        every { checkCacheNotExpired() }.returns(false)
        presenter.getData()
        verify {
            view?.startShimmer()
            testRetrieveData()
        }
    }

    @Test
    fun testRetrieveData() {
        presenter.retriveData()
        repoListModelArrayList = ArrayList()
        view?.initAdapter(repoListModelArrayList)
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(JSON_OBJECT))
        //assertThat(mockWebServer.takeRequest().path,URL)

        dbHelper.deleteDb()
        view?.stopShimmer()
        view?.showRecyclerView()
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
        repoListModelArrayList.add(repo)
        view?.notifyAdapterItemInserted(0)
        dbHelper.saveReposToDb(repo)
        sharedPreferences.edit().putLong(CACHE_TIME, Date().time)

    }

    @Test
    fun testRearrangeListByName() {
        presenter.rearrangeListByName()
        verify {
            repoListModelArrayList.sortBy { it.name }
            view?.initAdapter(repoListModelArrayList)
        }
    }

    @Test
    fun testRearrangeListByStars() {
        presenter.rearrangeListByStars()
        verify {
            repoListModelArrayList.sortByDescending { it.stars }
            view?.initAdapter(repoListModelArrayList)
        }
    }

    private fun checkCacheNotExpired(): Boolean {
        val cacheTime = sharedPreferences.getLong(CACHE_TIME, 0)
        return (cacheTime != 0L && TimeUnit.HOURS.convert(
            Date().time - cacheTime,
            TimeUnit.MILLISECONDS
        ) < 2)
    }


    @Test
    fun testOnDestroy() {
        presenter.onDestroy()
        verify {
            view = null
            view?.clearAdapterData()
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    private companion object {
        private const val URL =
            "https://private-anon-0e99465514-githubtrendingapi.apiary-mock.com/repositories"
        private const val NAME = "name"
        private const val AUTHOR = "author"
        private const val AVATAR = "avatar"
        private const val DESCRIPTION = "description"
        private const val LANGUAGE = "language"
        private const val LANGUAGE_COLOR = "languageColor"
        private const val STARS = "stars"
        private const val FORKS = "forks"
        private val CACHE_TIME = "cacheTime"
        private const val JSON_OBJECT = "[\n" +
            " {\n" +
            "    \"author\": \"kusti8\",\n" +
            "    \"name\": \"proton-native\",\n" +
            "    \"avatar\": \"https://github.com/kusti8.png\",\n" +
            "    \"url\": \"https://github.com/kusti8/proton-native\",\n" +
            "    \"description\": \"A React environment for cross platform native desktop apps\",\n" +
            "    \"language\": \"JavaScript\",\n" +
            "    \"languageColor\": \"#3572A5\",\n" +
            "    \"stars\": 4711,\n" +
            "    \"forks\": 124,\n" +
            "    \"currentPeriodStars\": 1186,\n" +
            "    \"builtBy\": [\n" +
            "      {\n" +
            "        \"href\": \"https://github.com/viatsko\",\n" +
            "        \"avatar\": \"https://avatars0.githubusercontent.com/u/376065\",\n" +
            "        \"username\": \"viatsko\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]"
    }
}
