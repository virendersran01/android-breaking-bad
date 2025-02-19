package com.harismexis.breakingbad.tests

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.harismexis.breakingbad.R
import com.harismexis.breakingbad.domain.Actor
import com.harismexis.breakingbad.parser.MockActorsParser.Companion.EXPECTED_NUM_ACTORS_WHEN_ALL_IDS_VALID
import com.harismexis.breakingbad.parser.MockActorsParser.Companion.EXPECTED_NUM_ACTORS_WHEN_NO_DATA
import com.harismexis.breakingbad.parser.MockActorsParser.Companion.EXPECTED_NUM_ACTORS_WHEN_SEARCH_BY_NAME_LIKE_SALA
import com.harismexis.breakingbad.parser.MockActorsParser.Companion.EXPECTED_NUM_ACTORS_WHEN_SEARCH_BY_NAME_LIKE_WALTER
import com.harismexis.breakingbad.parser.MockActorsParser.Companion.EXPECTED_NUM_ACTORS_WHEN_SOME_EMPTY
import com.harismexis.breakingbad.parser.MockActorsParser.Companion.EXPECTED_NUM_ACTORS_WHEN_SOME_IDS_INVALID
import com.harismexis.breakingbad.presentation.result.ActorsResult
import com.harismexis.breakingbad.presentation.screens.home.ui.activity.MainActivity
import com.harismexis.breakingbad.setup.base.InstrumentedTestSetup
import com.harismexis.breakingbad.setup.testutil.RecyclerCountAssertion
import com.harismexis.breakingbad.setup.testutil.SearchViewActionExtension
import com.harismexis.breakingbad.setup.testutil.verifyRecyclerItemAt
import com.harismexis.breakingbad.setup.viewmodel.MockHomeVmProvider
import io.mockk.every
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest : InstrumentedTestSetup() {

    @get:Rule
    val testRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(
            MainActivity::class.java,
            false, false
        )

    companion object {
        const val WALTER = "walter"
        const val SALA = "sala"
    }

    private val mockViewModel = MockHomeVmProvider.mockHomeViewModel
    private lateinit var mockActors: List<Actor>
    private lateinit var actorsSuccess: ActorsResult.ActorsSuccess

    @Test
    fun actorsFeedHasAllItemsValid_listHasExpectedItems() {
        // given
        mockInitialResults(actorsParser.getMockActorsWhenJsonHasAllItemsValid())

        // when
        testRule.launchActivity(null)

        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_ALL_IDS_VALID)
    }

    @Test
    fun actorsFeedHasSomeInvalidIds_listHasExpectedItems() {
        // given
        mockInitialResults(actorsParser.getMockActorsWhenJsonHasSomeInvalidIds())

        // when
        testRule.launchActivity(null)

        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_SOME_IDS_INVALID)
    }

    @Test
    fun actorsFeedHasSomeEmptyActorJsonItems_listHasExpectedItems() {
        // given
        mockInitialResults(actorsParser.getMockActorsWhenJsonHasSomeEmptyItems())

        // when
        testRule.launchActivity(null)

        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_SOME_EMPTY)
    }

    @Test
    fun actorsFeedHasAllIdsInvalid_listHasNoItems() {
        // given
        mockInitialResults(actorsParser.getMockActorsWhenJsonHasAllIdsInvalid())

        // when
        testRule.launchActivity(null)

        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_NO_DATA)
    }

    @Test
    fun actorsFeedIsEmptyJson_listHasNoItems() {
        // given
        mockInitialResults(actorsParser.getMockActorsWhenJsonIsEmpty())

        // when
        testRule.launchActivity(null)

        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_NO_DATA)
    }

    @Test
    fun userSearchesActorByName_listHasExpectedItems() {
        // Test Initial Results

        // given
        mockInitialResults(actorsParser.getMockActorsWhenJsonHasAllItemsValid())
        // when
        testRule.launchActivity(null)
        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_ALL_IDS_VALID)

        // Search by actor name "walter" and check results

        // given
        mockSearchResults(WALTER, actorsParser.getMockActorsSearchByNameLikeWalter())
        // when
        onView(withId(R.id.searchView)).perform(SearchViewActionExtension.submitQuery(WALTER))
        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_SEARCH_BY_NAME_LIKE_WALTER)

        // Search by actor name "sala" and check results

        // given
        mockSearchResults(SALA, actorsParser.getMockActorsSearchByNameLikeSala())
        // when
        onView(withId(R.id.searchView)).perform(SearchViewActionExtension.submitQuery(SALA))
        // then
        verifyRecycler(EXPECTED_NUM_ACTORS_WHEN_SEARCH_BY_NAME_LIKE_SALA)
    }

    private fun mockInitialResults(mockData: List<Actor>) {
        mockActors = mockData
        actorsSuccess = ActorsResult.ActorsSuccess(mockActors)
        every { mockViewModel.fetchInitialActors() } answers {
            MockHomeVmProvider.fakeActorsResult.value = actorsSuccess
        }
        every { mockViewModel.actorsResult } returns MockHomeVmProvider.fakeActorsResult
    }

    private fun mockSearchResults(actorName: String, mockData: List<Actor>) {
        mockActors = mockData
        actorsSuccess = ActorsResult.ActorsSuccess(mockActors)
        every { mockViewModel.updateSearchQuery(actorName) } answers {
            MockHomeVmProvider.fakeActorsResult.value = actorsSuccess
        }
        every { mockViewModel.actorsResult } returns MockHomeVmProvider.fakeActorsResult
    }

    private fun verifyRecycler(expectedNumberOfItems: Int) {
        onView(withId(R.id.home_list)).check(matches(isDisplayed()))
        verifyRecyclerCount(expectedNumberOfItems)
        verifyRecyclerData()
    }

    private fun verifyRecyclerCount(expectedNumberOfItems: Int) {
        // Checking if the mock result success has correct number of items
        Assert.assertEquals(actorsSuccess.items.size, expectedNumberOfItems)
        // Checking if recycler has correct number of items
        onView(withId(R.id.home_list)).check(RecyclerCountAssertion(expectedNumberOfItems))
    }

    private fun verifyRecyclerData() {
        mockActors.forEachIndexed { index, actor ->
            onView(withId(R.id.home_list)).perform(scrollToPosition<RecyclerView.ViewHolder>(index))
            verifyRecyclerValue(index, R.id.txt_name, actor.name)
            verifyRecyclerValue(index, R.id.txt_meta, actor.nickname)
        }
    }

    private fun verifyRecyclerValue(
        index: Int,
        @IdRes viewId: Int,
        value: String?
    ) {
        verifyRecyclerItemAt(R.id.home_list, index, viewId, value)
    }

}