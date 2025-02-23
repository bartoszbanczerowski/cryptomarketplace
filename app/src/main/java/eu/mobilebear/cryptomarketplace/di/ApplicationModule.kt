package eu.mobilebear.cryptomarketplace.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.mobilebear.cryptomarketplace.R
import eu.mobilebear.cryptomarketplace.data.BitfinexRepository
import eu.mobilebear.cryptomarketplace.data.BitfinexService
import eu.mobilebear.cryptomarketplace.data.TimeProvider
import eu.mobilebear.cryptomarketplace.data.analytics.AnalyticsLoggerImpl
import eu.mobilebear.cryptomarketplace.data.interceptors.NetworkInterceptor
import eu.mobilebear.cryptomarketplace.data.mapper.TickerMapper
import eu.mobilebear.cryptomarketplace.data.model.IO
import eu.mobilebear.cryptomarketplace.data.model.Main
import eu.mobilebear.cryptomarketplace.data.remoteconfig.RemoteConfigManager
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticsLogger
import eu.mobilebear.cryptomarketplace.domain.mapper.CryptoAssetMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BITFINEX_URL)
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()

    @Singleton
    @Provides
    fun providesBitfinexService(retrofit: Retrofit): BitfinexService = retrofit.create()

    @Singleton
    @Provides
    fun providesOkhttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(networkInterceptor)
            .build()

    @Provides
    @Singleton
    fun providesCache(@ApplicationContext context: Context) = Cache(context.cacheDir, DEFAULT_CACHE_SIZE)

    @Provides
    @Singleton
    fun providesChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor =
        ChuckerInterceptor.Builder(context).build()

    @Singleton
    @Provides
    fun providesOkHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message -> Timber.i(message) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun providesNetworkInterceptor(): NetworkInterceptor = NetworkInterceptor()

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi.Builder().build()

    @Singleton
    @Provides
    fun providesMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun providesTickerMapper(): TickerMapper = TickerMapper()

    @Singleton
    @Provides
    fun providesBitfinexRepository(
        bitfinexService: BitfinexService,
        tickerMapper: TickerMapper,
        cryptoAssetMapper: CryptoAssetMapper,
        analyticsLogger: AnalyticsLogger
    ): BitfinexRepository = BitfinexRepository(bitfinexService, tickerMapper, cryptoAssetMapper, analyticsLogger)

    @Singleton
    @Provides
    fun providesCryptoAssetMapper(): CryptoAssetMapper = CryptoAssetMapper()

    @Singleton
    @Provides
    @Main
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    @IO
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesFirebaseConfigManager(): FirebaseRemoteConfig {
        val firebaseRemoteConfigSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = 60
            minimumFetchIntervalInSeconds = 0
        }
        return FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            setConfigSettingsAsync(firebaseRemoteConfigSettings)
        }
    }

    @Provides
    @Singleton
    fun providesFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)

    @Provides
    @Singleton
    fun providesFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()


    @Provides
    @Singleton
    fun providesRemoteConfigManager(firebaseRemoteConfig: FirebaseRemoteConfig): RemoteConfigManager =
        RemoteConfigManager(firebaseRemoteConfig)

    @Provides
    @Singleton
    fun providesAnalyticsLogger(
        remoteConfigManager: RemoteConfigManager,
        firebaseAnalytics: FirebaseAnalytics,
        firebaseCrashlytics: FirebaseCrashlytics,
        timeProvider: TimeProvider
    ): AnalyticsLogger =
        AnalyticsLoggerImpl(remoteConfigManager, firebaseAnalytics, firebaseCrashlytics, timeProvider)

    private const val DEFAULT_CACHE_SIZE: Long = 10 * 1024 * 1024
    private const val BITFINEX_URL = "https://api-pub.bitfinex.com/v2/"
}