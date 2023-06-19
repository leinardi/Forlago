package com.leinardi.forlago.feature.account.interactor.account

import com.leinardi.forlago.feature.account.api.interactor.account.LogOutInteractor
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.library.network.api.interactor.ClearApolloCacheInteractor
import com.leinardi.forlago.library.preferences.api.repository.DataStoreRepository
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LogOutInteractorImplTest {
    private val clearApolloCacheInteractor: ClearApolloCacheInteractor = mockk()
    private val forlagoNavigator: ForlagoNavigator = mockk()
    private val getFeaturesInteractor: GetFeaturesInteractor = mockk()
    private val userDataStoreRepository: DataStoreRepository = mockk()
    private lateinit var logOutInteractor: LogOutInteractor

    @Before
    fun setUp() {
        logOutInteractor = LogOutInteractorImpl(
            clearApolloCacheInteractor,
            forlagoNavigator,
            getFeaturesInteractor,
            userDataStoreRepository,
        )
        coEvery { userDataStoreRepository.clearPreferencesStorage() } just Runs
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `GIVEN navigateToLogin false WHEN call logOutInteractor THEN clear data & no navigate`(): TestResult = runTest {
        // Given
        every { getFeaturesInteractor() } returns emptyList()
        coEvery { clearApolloCacheInteractor() } returns true
        coEvery { forlagoNavigator.navigate(any()) } returns true

        // When
        logOutInteractor(false)

        // Then
        verify(exactly = 1) { getFeaturesInteractor() }
        coVerify(exactly = 1) { clearApolloCacheInteractor() }
        coVerify(exactly = 0) { forlagoNavigator.navigate(any()) }
    }
}
