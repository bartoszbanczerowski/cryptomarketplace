package eu.mobilebear.cryptomarketplace.domain

import eu.mobilebear.cryptomarketplace.domain.model.CryptoAsset

sealed class GetCryptoAssetsAction {

    class Success(val cryptoAssets: List<CryptoAsset>): GetCryptoAssetsAction()
    object NetworkException: GetCryptoAssetsAction()
    object GeneralError: GetCryptoAssetsAction()
}