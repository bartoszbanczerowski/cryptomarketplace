package eu.mobilebear.cryptomarketplace.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ticker(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "bid") val bid: Double,
    @Json(name = "bidSize") val bidSize: Double,
    @Json(name = "ask") val ask: Double,
    @Json(name = "askSize") val askSize: Double,
    @Json(name = "dailyChange") val dailyChange: Double,
    @Json(name = "dailyChangeRelative") val dailyChangeRelative: Double,
    @Json(name = "lastPrice") val lastPrice: Double,
    @Json(name = "volume") val volume: Double,
    @Json(name = "high") val high: Double,
    @Json(name = "low") val low: Double
)