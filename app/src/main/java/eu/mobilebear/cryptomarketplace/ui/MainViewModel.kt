package eu.mobilebear.cryptomarketplace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobilebear.cryptomarketplace.data.BitfinexRepository
import eu.mobilebear.cryptomarketplace.data.model.IO
import eu.mobilebear.cryptomarketplace.data.model.Main
import eu.mobilebear.cryptomarketplace.data.remoteconfig.RemoteConfigManager
import eu.mobilebear.cryptomarketplace.domain.GetCryptoAssetsAction
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticEvent
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticScreen
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticsLogger
import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val bitfinexRepository: BitfinexRepository,
    private val remoteConfigManager: RemoteConfigManager,
    private val analyticsLogger: AnalyticsLogger,
    @Main private val mainDispatcher: CoroutineDispatcher,
    @IO private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), MainContract {


    private val _state: MutableStateFlow<MainContract.State> = MutableStateFlow(MainContract.State())
    override val state: StateFlow<MainContract.State> = _state.asStateFlow()

    private val _effect = Channel<MainContract.Effect>()
    override val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            analyticsLogger.logEvent(AnalyticScreen.MAIN_SCREEN)
            getCryptoAssets()
        }
        viewModelScope.launch {
            fetchRemoteConfig()
        }
    }

    private suspend fun fetchRemoteConfig() {
        remoteConfigManager.fetchAndActivate()
        remoteConfigManager.effect.collectLatest {
            _state.update { state ->
                state.copy(isAnalyticsEnabled = remoteConfigManager.isAnalyticsEnabled)
            }
        }
    }

    override fun event(event: MainContract.Event) {
        when (event) {
            MainContract.Event.ClearSearchClicked -> clearSearchClicked()
            is MainContract.Event.SearchQueryChange -> searchQueryChange(event.query)
        }
    }

    private fun clearSearchClicked() {
        analyticsLogger.logEvent(AnalyticEvent.MAIN_SEARCH_CLEAR_CLICK)
        _state.update {
            it.copy(
                filteredCryptoAssets = it.cryptoAssets,
                searchQuery = ""
            )
        }
    }

    private fun searchQueryChange(query: String) {
        analyticsLogger.logEvent(AnalyticEvent.MAIN_SEARCH_QUERY_CHANGE)
        _state.update {
            it.copy(
                searchQuery = query,
                filteredCryptoAssets = filterCryptoAssets(state.value.cryptoAssets, searchQuery = query)
            )
        }
    }

    private fun getCryptoAssets() {
        viewModelScope.launch(ioDispatcher) {
            val action = bitfinexRepository.getCryptoAssetss()
            withContext(mainDispatcher) {
                when (action) {
                    GetCryptoAssetsAction.GeneralError -> _state.update {
                        it.copy(isLoading = false, isGeneralError = true, isNetworkError = false)
                    }

                    GetCryptoAssetsAction.NetworkException -> _state.update {
                        it.copy(isLoading = false, isGeneralError = false, isNetworkError = true)
                    }

                    is GetCryptoAssetsAction.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            isGeneralError = false,
                            isNetworkError = false,
                            searchQuery = it.searchQuery,
                            cryptoAssets = action.cryptoAssets,
                            filteredCryptoAssets = filterCryptoAssets(action.cryptoAssets, it.searchQuery)
                        )
                    }
                }
            }
        }
    }

    private fun filterCryptoAssets(cryptoAssets: List<CryptoAsset>, searchQuery: String) = cryptoAssets
        .filter { cryptoAsset ->
            if (searchQuery.isNotEmpty()) {
                cryptoAsset.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            } else true
        }
}