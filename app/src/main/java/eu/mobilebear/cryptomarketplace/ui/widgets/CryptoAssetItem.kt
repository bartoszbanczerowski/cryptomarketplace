package eu.mobilebear.cryptomarketplace.ui.widgets

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.mobilebear.cryptomarketplace.R
import eu.mobilebear.cryptomarketplace.ui.theme.CryptoMarketplaceTheme

@Composable
fun CryptoAssetItem(
    name: String,
    @DrawableRes icon: Int,
    lastPrice: Double,
    dailyChangeRelative: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(id = icon),
                            modifier = Modifier.size(40.dp),
                            contentDescription = "$name icon",
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = name, style = MaterialTheme.typography.titleLarge)
                    }
                }

                Text(
                    text = String.format(stringResource(R.string.crypto_asset_last_price), lastPrice),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val color = when {
                    dailyChangeRelative > 0.00 -> MaterialTheme.colorScheme.tertiary
                    dailyChangeRelative < 0.00 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                }
                Text(
                    text = String.format(stringResource(R.string.crypto_asset_daily_change), dailyChangeRelative),
                    style = MaterialTheme.typography.labelLarge,
                    color = color
                )
            }

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CryptoAssetPreview() {
    CryptoMarketplaceTheme {
        CryptoAssetItem("BTC", R.drawable.ic_btc_logo, 65000.00, 0.15)
    }
}