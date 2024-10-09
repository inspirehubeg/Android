package alexschool.bookreader.di

import alexschool.bookreader.data.AppRepository
import alexschool.bookreader.data.AppRepositoryImpl
import alexschool.bookreader.data.local.AlexSchoolDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlexSchoolModule {

    //for network and ktor
    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
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
    }


    @Singleton
    @Provides
    fun provideAlexSchoolDatabase(@ApplicationContext context: Context): AlexSchoolDatabase {
        return Room.databaseBuilder(
            context,
            AlexSchoolDatabase::class.java,
            "alex_school_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        //apiService: ApiService,
        appDatabase: AlexSchoolDatabase,
    ): AppRepository {
        return AppRepositoryImpl(
            appDatabase,
           // apiService,
        )
    }

//    @Singleton
//    @Provides
//    fun provideApiService(httpClient: HttpClient): ApiService = ApiServiceImpl(httpClient)

//    @Singleton
//    @Provides
//    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default
}