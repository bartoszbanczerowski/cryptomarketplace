package eu.mobilebear.cryptomarketplace.data

import eu.mobilebear.cryptomarketplace.data.mapper.TickerMapper
import eu.mobilebear.cryptomarketplace.domain.GetCryptoAssetsAction
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticsLogger
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticsParam
import eu.mobilebear.cryptomarketplace.domain.mapper.CryptoAssetMapper
import okio.IOException

class BitfinexRepository(
    private val bitfinexService: BitfinexService,
    private val tickerMapper: TickerMapper,
    private val cryptoAssetMapper: CryptoAssetMapper,
    private val analyticsLogger: AnalyticsLogger
) {
    private val symbols = "tBTCUSD,tETHUSD,tCHSB:USD,tLTCUSD,tXRPUSD,tDSHUSD,tRRTUSD,tEOSUSD,tSANUSD,tDATUSD," +
            "tDOGE:USD,tLUNA:USD,tMATIC:USD,tNEXO:USD,tOCEAN:USD,tBEST:USD,tAAVE:USD,tPLUUSD,tFILUSD"

    suspend fun getCryptoAssetss(): GetCryptoAssetsAction =
        try {
            val response = bitfinexService.getTickers(symbols)
            when {
                response.isSuccessful -> {
                    val data = response.body() ?: emptyList()
                    val cryptoAssets = data
                        .map { tickerData -> tickerMapper.parseTickerData(tickerData) }
                        .map { cryptoAssetMapper.convertToCryptoAsset(it) }
                    GetCryptoAssetsAction.Success(cryptoAssets)
                }

                else -> GetCryptoAssetsAction.GeneralError
            }
        } catch (throwable: Throwable) {
            analyticsLogger.logException(throwable, mapOf(AnalyticsParam.ERROR to "CryptoAssets tickers fetch failed"))
            when (throwable) {
                is IOException -> GetCryptoAssetsAction.NetworkException
                else -> GetCryptoAssetsAction.GeneralError
            }
        }
}