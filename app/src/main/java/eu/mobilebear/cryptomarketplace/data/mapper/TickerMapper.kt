package eu.mobilebear.cryptomarketplace.data.mapper

import eu.mobilebear.cryptomarketplace.data.model.Ticker
import javax.inject.Inject


class TickerMapper @Inject constructor() {
    fun parseTickerData(data: List<Any>): Ticker {
        val symbol = data[0] as String
        val bid = data[1] as Double
        val bidSize = data[2] as Double
        val ask = data[3] as Double
        val askSize = data[4] as Double
        val dailyChange = data[5] as Double
        val dailyChangeRelative = data[6] as Double
        val lastPrice = data[7] as Double
        val volume = data[8] as Double
        val high = data[9] as Double
        val low = data[10] as Double
        return Ticker(
            symbol,
            bid,
            bidSize,
            ask,
            askSize,
            dailyChange,
            dailyChangeRelative,
            lastPrice,
            volume,
            high,
            low
        )
    }
}