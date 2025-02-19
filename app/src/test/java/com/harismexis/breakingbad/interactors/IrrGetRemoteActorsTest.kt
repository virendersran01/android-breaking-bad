package com.harismexis.breakingbad.interactors

import com.harismexis.breakingbad.data.BreakingBadRemoteRepository
import com.harismexis.breakingbad.domain.Actor
import com.harismexis.breakingbad.interactors.actor.IrrGetRemoteActors
import com.harismexis.breakingbad.setup.UnitTestSetup
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class IrrGetRemoteActorsTest : UnitTestSetup() {

    @Mock
    private lateinit var mockRepository: BreakingBadRemoteRepository

    private lateinit var mockActors: List<Actor>
    private lateinit var subject: IrrGetRemoteActors

    init {
        initialise()
    }

    override fun initialiseClassUnderTest() {
        MockitoAnnotations.initMocks(this)
        setupMocks()
        subject = IrrGetRemoteActors(mockRepository)
    }

    private fun setupMocks() {
        mockActors = actorsParser.getMockActorsWhenJsonHasAllItemsValid()
        runBlocking {
            Mockito.`when`(mockRepository.getActors()).thenReturn(mockActors)
        }
    }

    @Test
    fun interactorInvoked_then_repositoryCallsExpectedMethodWithExpectedArgAndResult() =
        runBlocking {
            // when
            val items = subject()

            // then
            verify(mockRepository, times(1)).getActors()
            Assert.assertEquals(mockActors.size, items.size)
        }

}