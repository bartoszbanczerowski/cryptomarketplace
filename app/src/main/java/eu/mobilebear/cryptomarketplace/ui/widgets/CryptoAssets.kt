package eu.mobilebear.cryptomarketplace.ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.mobilebear.cryptomarketplace.R
import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset
import eu.mobilebear.cryptomarketplace.ui.theme.CryptoMarketplaceTheme

@Composable
fun CryptoAssets(assets: List<CryptoAsset>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(assets.size) { asset ->
            CryptoAssetItem(
                name = assets[asset].name,
                icon = assets[asset].image,
                lastPrice = assets[asset].lastPrice,
                dailyChangeRelative = assets[asset].dailyChangeRelative,
            )
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CryptoAssetsPreview() {
    CryptoMarketplaceTheme {
        CryptoAssets(
            assets = listOf(
                CryptoAsset("BTC", R.drawable.ic_btc_logo, 65000.00, 0.15),
                CryptoAsset("ETH", R.drawable.ic_eth_logo, 15000.00, -0.15),
                CryptoAsset("LTC", R.drawable.ic_ltc_logo, 65000.00, 0.00)
            )
        )
    }
}