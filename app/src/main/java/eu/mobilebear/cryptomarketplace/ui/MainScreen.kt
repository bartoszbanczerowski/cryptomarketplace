import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import eu.mobilebear.cryptomarketplace.R
import eu.mobilebear.cryptomarketplace.ui.MainContract
import eu.mobilebear.cryptomarketplace.ui.MainViewModel
import eu.mobilebear.cryptomarketplace.ui.theme.CryptoMarketplaceTheme
import eu.mobilebear.cryptomarketplace.ui.theme.use
import eu.mobilebear.cryptomarketplace.ui.widgets.CryptoAssets
import eu.mobilebear.cryptomarketplace.ui.widgets.LottieScreen
import eu.mobilebear.cryptomarketplace.ui.widgets.SearchBar

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {

    val (state, _, dispatch) = use(viewModel)

    MainScreen(state, dispatch)
}

@Composable
private fun MainScreen(
    state: MainContract.State,
    dispatch: (MainContract.Event) -> Unit
) {
    Column {
        SearchBar(
            searchText = state.searchQuery,
            onTextChange = { dispatch.invoke(MainContract.Event.SearchQueryChange(it)) },
            onSearchClick = { dispatch.invoke(MainContract.Event.ClearSearchClicked) }
        )

        when {
            state.isGeneralError -> LottieScreen(animation = R.raw.general_error)
            state.isNetworkError -> LottieScreen(animation = R.raw.network_error)
            state.isLoading -> LottieScreen(animation = R.raw.loading)
            else -> {
                AnimatedVisibility(visible = state.cryptoAssets.isEmpty() || state.filteredCryptoAssets.isEmpty()) {
                    LottieScreen(animation = R.raw.empty_list)
                }
                AnimatedVisibility(visible = state.cryptoAssets.isNotEmpty() && state.filteredCryptoAssets.isNotEmpty()) {
                    CryptoAssets(assets = state.filteredCryptoAssets)
                }
            }
        }
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainPreview() {
    CryptoMarketplaceTheme {
        MainScreen(state = MainContract.State(), dispatch = {})
    }
}
