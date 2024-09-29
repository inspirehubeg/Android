package alexschool.bookreader.di

import alexschool.bookreader.data.AppRepository
import alexschool.bookreader.data.AppRepositoryImpl
import alexschool.bookreader.data.local.AlexSchoolDatabase
import alexschool.bookreader.network.ApiService
import alexschool.bookreader.network.ApiServiceImpl
import android.content.Context
import androidx.room.Room
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@dagger.Module
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

//    @Singleton
//    @Provides
//    fun provideCategoryDao(alexSchoolDatabase: AlexSchoolDatabase): CategoryDao {
//        return alexSchoolDatabase.categoryDao
//    }
//
//    @Singleton
//    @Provides
//    fun provideBookInfoDao(alexSchoolDatabase: AlexSchoolDatabase): BookInfoDao {
//        return alexSchoolDatabase.bookInfoDao
//    }
//    @Singleton
//    @Provides
//    fun provideBookDao(alexSchoolDatabase: AlexSchoolDatabase): BookDao {
//        return alexSchoolDatabase.bookDao
//    }
//
//    @Singleton
//    @Provides
//    fun provideAuthorDao(alexSchoolDatabase: AlexSchoolDatabase): AuthorDao {
//        return alexSchoolDatabase.authorDao
//    }

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
        apiService: ApiService,
        appDatabase: AlexSchoolDatabase,
        defaultDispatcher: CoroutineDispatcher
    ): AppRepository {
        return AppRepositoryImpl(
            appDatabase,
            apiService,
            defaultDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideApiService(httpClient: HttpClient): ApiService = ApiServiceImpl(httpClient)

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default
}