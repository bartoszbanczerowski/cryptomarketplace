package eu.mobilebear.cryptomarketplace.domain.model

import androidx.annotation.DrawableRes

data class CryptoAsset(
    val name: String,
    @DrawableRes val image: Int,
    val lastPrice: Double,
    val dailyChangeRelative: Double
)