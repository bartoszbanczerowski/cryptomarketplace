package eu.mobilebear.cryptomarketplace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.mobilebear.cryptomarketplace.data.BitfinexRepository
import eu.mobilebear.cryptomarketplace.data.model.IO
import eu.mobilebear.cryptomarketplace.data.model.Main
import eu.mobilebear.cryptomarketplace.domain.GetCryptoAssetsAction
import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val bitfinexRepository: BitfinexRepository,
    @Main private val mainDispatcher: CoroutineDispatcher,
    @IO private val ioDispatcher: CoroutineDispatcher
) : ViewModel(), MainContract {


    private val _state: MutableStateFlow<MainContract.State> = MutableStateFlow(MainContract.State())
    override val state: StateFlow<MainContract.State> = _state.asStateFlow()

    private val _effect = Channel<MainContract.Effect>()
    override val effect = _effect.receiveAsFlow()

    override fun event(event: MainContract.Event) {
        when (event) {
            MainContract.Event.ClearSearchClicked -> _state.update {
                it.copy(
                    filteredCryptoAssets = it.cryptoAssets,
                    searchQuery = ""
                )
            }

            is MainContract.Event.SearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query,
                        filteredCryptoAssets = filterCryptoAssets(state.value.cryptoAssets, searchQuery = event.query)
                    )
                }
            }
        }
    }

    init {
        getCryptoAssets()
    }

    private fun getCryptoAssets() {
        viewModelScope.launch(ioDispatcher) {
            while (isActive) {
                if (_state.value.cryptoAssets.isEmpty()) {
                    _state.update { it.copy(isLoading = true) }
                }
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

                delay(5000)
            }

        }
    }

    private fun filterCryptoAssets(cryptoAssets: List<CryptoAsset>, searchQuery: String) = cryptoAssets
        .filter { cryptoAsset ->
            if (state.value.searchQuery.isNotEmpty()) {
                cryptoAsset.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            } else true
        }
}