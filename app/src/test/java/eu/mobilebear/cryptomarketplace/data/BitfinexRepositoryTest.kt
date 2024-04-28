package eu.mobilebear.cryptomarketplace.data

import eu.mobilebear.cryptomarketplace.R
import eu.mobilebear.cryptomarketplace.data.mapper.TickerMapper
import eu.mobilebear.cryptomarketplace.data.model.Ticker
import eu.mobilebear.cryptomarketplace.domain.GetCryptoAssetsAction
import eu.mobilebear.cryptomarketplace.domain.mapper.CryptoAssetMapper
import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response


class BitfinexRepositoryTest {

    private val bitfinexService: BitfinexService = mockk()

    private val tickerMapper: TickerMapper = mockk()

    private val cryptoAssetMapper: CryptoAssetMapper = mockk()

    private val response: Response<List<List<Any>>> = mockk(relaxed = true)

    private val ticker: Ticker = Ticker(
        "tBTCUSD",
        63753.00,
        5.52776032,
        63754.00,
        4.51931173,
        788.00,
        0.01251429,
        63756.00,
        732.90254968,
        64470.00,
        62861.00
    )

    private val responseBody: List<List<Any>> = listOf(
        listOf(ticker)
    )

    private val cryptoAsset: CryptoAsset = CryptoAsset(
        name = "BTC",
        image = R.drawable.ic_btc_logo,
        lastPrice = 63756.00,
        dailyChangeRelative = 0.01251429
    )

    lateinit var bitfinexRepository: BitfinexRepository

    @Before
    fun setUp() {
        bitfinexRepository = BitfinexRepository(bitfinexService, tickerMapper, cryptoAssetMapper)
    }

    @Test
    fun `getCryptoAssets - successful response`() {
        //given
        coEvery { bitfinexService.getTickers(any()) } returns response
        coEvery { response.isSuccessful } returns true
        coEvery { response.body() } returns responseBody
        coEvery { tickerMapper.parseTickerData(responseBody.first()) } returns ticker
        coEvery { cryptoAssetMapper.convertToCryptoAsset(ticker) } returns cryptoAsset

        //when
        val result = runBlocking { bitfinexRepository.getCryptoAssetss() }

        //then
        Assert.assertTrue(result is GetCryptoAssetsAction.Success)
        coVerify { bitfinexService.getTickers(any()) }
        coVerify { tickerMapper.parseTickerData(responseBody.first()) }
        coVerify { cryptoAssetMapper.convertToCryptoAsset(ticker) }
        val successResult = result as GetCryptoAssetsAction.Success
        Assert.assertEquals(1, successResult.cryptoAssets.size)
        Assert.assertEquals(cryptoAsset, successResult.cryptoAssets.first())
    }

    @Test
    fun `getCryptoAssets - network exception`() {
        val mockException = IOException("Network error")

        // Mock dependencies
        coEvery { bitfinexService.getTickers(any()) } throws mockException

        // Call the method
        val result = runBlocking { bitfinexRepository.getCryptoAssetss() }

        // Assert network exception
        assertTrue(result is GetCryptoAssetsAction.NetworkException)
    }

    @Test
    fun `getCryptoAssets - general error`() {
        val mockException = RuntimeException("Unexpected error")

        // Mock dependencies
        coEvery { bitfinexService.getTickers(any()) } throws mockException

        // Call the method
        val result = runBlocking { bitfinexRepository.getCryptoAssetss() }

        // Assert general error
        assertTrue(result is GetCryptoAssetsAction.GeneralError)
    }
}