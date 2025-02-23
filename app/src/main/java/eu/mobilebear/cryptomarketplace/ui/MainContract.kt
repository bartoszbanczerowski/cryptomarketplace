package eu.mobilebear.cryptomarketplace.ui

import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset
import eu.mobilebear.cryptomarketplace.ui.theme.ViewModelContract

interface MainContract : ViewModelContract<MainContract.Event, MainContract.Effect, MainContract.State> {

    sealed class Event {
        class SearchQueryChange(val query: String) : Event()
        data object ClearSearchClicked : Event()
    }

    sealed class Effect

    data class State(
        val cryptoAssets: List<CryptoAsset> = emptyList(),
        val filteredCryptoAssets: List<CryptoAsset> = emptyList(),
        val searchQuery: String = "",
        val isLoading: Boolean = true,
        val isNetworkError: Boolean = false,
        val isGeneralError: Boolean = false,
        val isAnalyticsEnabled: Boolean = false
    )
}