package eu.mobilebear.cryptomarketplace.ui

import eu.mobilebear.cryptomarketplace.CoroutineTestWatcher
import eu.mobilebear.cryptomarketplace.R
import eu.mobilebear.cryptomarketplace.data.BitfinexRepository
import eu.mobilebear.cryptomarketplace.data.remoteconfig.RemoteConfigManager
import eu.mobilebear.cryptomarketplace.domain.GetCryptoAssetsAction
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticScreen
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticsLogger
import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val testWatcher = CoroutineTestWatcher()

    private val bitfinexRepository: BitfinexRepository = mockk(relaxed = true)
    private val remoteConfigManager: RemoteConfigManager = mockk(relaxed = true)
    private val analyticsLogger: AnalyticsLogger = mockk(relaxed = true)

    private val cryptoAssets = listOf(
        CryptoAsset("BTC", R.drawable.ic_btc_logo, 65000.00, 0.15),
        CryptoAsset("ETH", R.drawable.ic_eth_logo, 15000.00, -0.15)
    )

    private lateinit var viewModel: MainViewModel

    private fun createViewModel() = MainViewModel(
        bitfinexRepository = bitfinexRepository,
        remoteConfigManager = remoteConfigManager,
        analyticsLogger = analyticsLogger,
        mainDispatcher = testWatcher.dispatcher,
        ioDispatcher = testWatcher.dispatcher
    )

    @Test
    fun `should get crypto assets and update state`() = runTest(testWatcher.dispatcher) {
        // given
        coEvery { bitfinexRepository.getCryptoAssetss() } coAnswers { GetCryptoAssetsAction.Success(cryptoAssets) }

        // when
        viewModel = createViewModel()

        // then
        coVerify { bitfinexRepository.getCryptoAssetss() }
        assert(viewModel.state.value.cryptoAssets.isNotEmpty())
        assert(viewModel.state.value.cryptoAssets == cryptoAssets)
        coVerify { analyticsLogger.logEvent(AnalyticScreen.MAIN_SCREEN) }
    }

    @Test
    fun `should show general error when GetCryptoAssetsAction is GeneralError`() = runTest {
        // given
        coEvery { bitfinexRepository.getCryptoAssetss() } returns GetCryptoAssetsAction.GeneralError

        // when
        viewModel = createViewModel()

        // then
        assert(viewModel.state.value.isGeneralError)
        assert(!viewModel.state.value.isLoading)
        assert(!viewModel.state.value.isNetworkError)
    }

    @Test
    fun `should show network error when GetCryptoAssetsAction is NetworkException`() =
        runTest {
            // given
            coEvery { bitfinexRepository.getCryptoAssetss() } returns GetCryptoAssetsAction.NetworkException

            // when
            viewModel = createViewModel()

            // then
            assert(!viewModel.state.value.isGeneralError)
            assert(!viewModel.state.value.isLoading)
            assert(viewModel.state.value.isNetworkError)
        }

    @Test
    fun `should filter crypto assets by name`() = runTest(testWatcher.dispatcher) {
        // given
        coEvery { bitfinexRepository.getCryptoAssetss() } coAnswers { GetCryptoAssetsAction.Success(cryptoAssets) }
        val query = "BTC"
        viewModel = createViewModel()

        // when
        viewModel.event(MainContract.Event.SearchQueryChange(query))

        // then
        coVerify(exactly = 1) { bitfinexRepository.getCryptoAssetss() }
        assert(viewModel.state.value.cryptoAssets.isNotEmpty())
        assert(viewModel.state.value.cryptoAssets == cryptoAssets)
        assert(viewModel.state.value.filteredCryptoAssets.size == 1)
        assert(viewModel.state.value.filteredCryptoAssets[0].name == query)
    }

    @Test
    fun `should show all crypto assets when search query is empty`() = runTest(testWatcher.dispatcher) {
        // given
        coEvery { bitfinexRepository.getCryptoAssetss() } coAnswers { GetCryptoAssetsAction.Success(cryptoAssets) }
        val query = ""
        viewModel = createViewModel()

        // when
        viewModel.event(MainContract.Event.SearchQueryChange(query))

        // then
        coVerify { bitfinexRepository.getCryptoAssetss() }
        assert(viewModel.state.value.cryptoAssets.isNotEmpty())
        assert(viewModel.state.value.cryptoAssets == cryptoAssets)
        assert(viewModel.state.value.filteredCryptoAssets.size == cryptoAssets.size)
        assert(viewModel.state.value.filteredCryptoAssets.containsAll(cryptoAssets))
    }
}