package com.assignment.githubrepoapp

import android.content.SharedPreferences
import com.assignment.githubrepoapp.cache.DatabaseOpenHelper
import com.assignment.githubrepoapp.data.model.RepoListModel
import com.assignment.githubrepoapp.navigator.Navigator
import com.assignment.githubrepoapp.trendingpage.TrendingContract
import com.assignment.githubrepoapp.trendingpage.TrendingPresenter
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.Assert.assertEquals
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class TrendingPresenterTest {

    private lateinit var presenter: TrendingContract.Presenter

    @MockK
    private lateinit var view: TrendingContract.View

    @MockK
    private lateinit var navigator: Navigator

    @MockK
    private lateinit var dbHelper: DatabaseOpenHelper

    @MockK
    private lateinit var sharedPreferences: SharedPreferences

    @MockK
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @MockK
    private lateinit var repoListModelArrayList: ArrayList<RepoListModel>

    @MockK
    private lateinit var repoListModel: RepoListModel

    private val repoListModelCaptor = CapturingSlot<RepoListModel>()

    private val repoListModelCaptorForDb = CapturingSlot<RepoListModel>()

    @MockK
    private lateinit var jsonArray: JSONArray

    @MockK
    private lateinit var jsonObject: JSONObject

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockWebServer.start(8080)
        presenter = TrendingPresenter(view, navigator, dbHelper, sharedPreferences, repoListModelArrayList)
    }

    @Test
    fun testGetDataIfCacheNotExpired() {
        val listFromDb = ArrayList<RepoListModel>()
        listFromDb.add(repoListModel)
        every { sharedPreferences.getLong(CACHE_TIME, 0) }.returns(Date().time)
        every { dbHelper.getReposFromDb() } returns listFromDb
        every { repoListModelArrayList.addAll(listFromDb) } returns true

        presenter.getData()

        verify {
            repoListModelArrayList.addAll(listFromDb)
            view.initAdapter(repoListModelArrayList)
        }
    }

    @Test
    fun testGetDataIfCacheExpired() {
        every { sharedPreferences.getLong(CACHE_TIME, 0) } returns 0L
        every { repoListModelArrayList.clear() } returns Unit
        every { repoListModelArrayList.size } returns 1

        presenter.getData()

        verify {
            view.startShimmer()
            view.retrieveData()
        }
    }

        @Test
    fun testOnSuccess() {
        every { sharedPreferences.getLong(CACHE_TIME, 0) } returns 0L
        every { repoListModelArrayList.clear() } returns Unit
        every { repoListModelArrayList.size } returns 1
        every { sharedPreferences.edit() } returns sharedPreferencesEditor
        every { sharedPreferencesEditor.putLong(CACHE_TIME, any()) } returns sharedPreferencesEditor
        every { jsonArray.length() } returns 1
        every { jsonArray.getJSONObject(0) } returns jsonObject
        every { jsonObject.getString(NAME) } returns NAME
        every { jsonObject.getString(AUTHOR) } returns AUTHOR
        every { jsonObject.getString(AVATAR) } returns AVATAR
        every { jsonObject.getString(DESCRIPTION) } returns DESCRIPTION
        every { jsonObject.getString(LANGUAGE) } returns LANGUAGE
        every { jsonObject.getString(LANGUAGE_COLOR) } returns LANGUAGE_COLOR
        every { jsonObject.getString(STARS) } returns "0"
        every { jsonObject.getString(FORKS) } returns "0"
        every { repoListModelArrayList.add(any()) } returns true

        presenter.onSuccess(jsonArray)

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(JSON_OBJECT))
        verify {
            repoListModelArrayList.clear()
            view.initAdapter(repoListModelArrayList)
        }
        verify {
            dbHelper.deleteDb()
            view.stopShimmer()
            view.showRecyclerView()
            repoListModelArrayList.add(capture(repoListModelCaptor))
            view.notifyAdapterItemInserted(0)
            dbHelper.saveReposToDb(capture(repoListModelCaptorForDb))
            sharedPreferencesEditor.putLong(CACHE_TIME, any())
        }
            assertEquals(repoListModelCaptor.captured, repoListModelCaptorForDb.captured)
            assertEquals(repoListModelCaptor.captured.author, AUTHOR)
    }

    @Test
    fun testOnFailure() {
        presenter.onFailure(ERROR)
        verify {
            navigator.launchErrorPage()
        }
    }

    @Test
    fun testRearrangeListByName() {
        every { repoListModelArrayList.size }. returns(1)
        presenter.rearrangeListByName()
        verify {
            repoListModelArrayList.sortBy { it.name }
            view.initAdapter(repoListModelArrayList)
        }
    }

    @Test
    fun testRearrangeListByStars() {
        every { repoListModelArrayList.size }. returns(1)
        presenter.rearrangeListByStars()
        verify {
            repoListModelArrayList.sortByDescending { it.stars }
            view.initAdapter(repoListModelArrayList)
        }
    }

    @Test
    fun testOnDestroy() {
        presenter.onDestroy()
        verify {
            view.clearAdapterData()
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
        private val ERROR = "Error"
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
