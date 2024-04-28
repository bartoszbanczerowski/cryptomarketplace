package eu.mobilebear.cryptomarketplace.domain.mapper

import eu.mobilebear.cryptomarketplace.R
import eu.mobilebear.cryptomarketplace.data.model.Ticker
import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset
import javax.inject.Inject


class CryptoAssetMapper @Inject constructor() {
    fun convertToCryptoAsset(ticker: Ticker): CryptoAsset {

        val name = ticker.symbol.removePrefix(TRANSACTION_PREFIX)
            .replace(DELIMETER, EMPTY)
            .removeSuffix(TRANSACTION_SUFFIX)

        return CryptoAsset(
            name = name,
            image = getIcon(name),
            lastPrice = ticker.lastPrice,
            dailyChangeRelative = ticker.dailyChangeRelative * 100
        )
    }


    private fun getIcon(cryptoAssetName: String): Int =
        when (cryptoAssetName) {
            AAVE -> R.drawable.ic_aave_logo
            BTC -> R.drawable.ic_btc_logo
            BITPANDA_ECONOMY -> R.drawable.ic_bitpanda_logo
            ETH -> R.drawable.ic_eth_logo
            EOS -> R.drawable.ic_eos_logo
            FIL -> R.drawable.ic_fil_logo
            DASH -> R.drawable.ic_dash_logo
            DOGE -> R.drawable.ic_doge_logo
            LTC -> R.drawable.ic_ltc_logo
            MATIC -> R.drawable.ic_matic_logo
            NEXO -> R.drawable.ic_nexo_logo
            OCEAN -> R.drawable.ic_ocean_logo
            PLU -> R.drawable.ic_plu_logo
            SWISSBORG -> R.drawable.ic_swissborg_logo
            XRP -> R.drawable.ic_xrp_logo
            else -> R.drawable.ic_doge_logo
        }

    companion object {
        private const val AAVE = "AAVE"
        private const val BTC = "BTC"
        private const val BITPANDA_ECONOMY = "BEST"
        private const val ETH = "ETH"
        private const val EOS = "EOS"
        private const val FIL = "FIL"
        private const val DASH = "DSH"
        private const val DOGE = "DOGE"
        private const val LTC = "LTC"
        private const val MATIC = "MATIC"
        private const val NEXO = "NEXO"
        private const val OCEAN = "OCEAN"
        private const val PLU = "PLU"
        private const val SWISSBORG = "CHSB"
        private const val XRP = "XRP"
        private const val TRANSACTION_PREFIX = "t"
        private const val TRANSACTION_SUFFIX = "USD"
        private const val DELIMETER = ":"
        private const val EMPTY = ""
    }
}