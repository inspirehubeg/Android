package alexSchool.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

object NetworkModule {
    private val httpClient = HttpClient {
        // Configure Ktor client here
        install(Logging) {
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    fun provideApiService(): ApiService = ApiServiceImpl(httpClient)
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default
}

//access in other modules
//val apiService = NetworkModule.provideApiService()