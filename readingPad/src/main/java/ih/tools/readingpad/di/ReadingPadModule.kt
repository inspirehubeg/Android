package ih.tools.readingpad.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ih.tools.readingpad.feature_book_parsing.data.PreferencesManager
import ih.tools.readingpad.feature_book_parsing.data.data_source.AppDatabase
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.BookParserUseCases
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParseBook
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParseFont
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParseImage
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParseInternalLink
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParseInternalLinkLazy
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParsePageLazy
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParseRegularText
import ih.tools.readingpad.feature_book_parsing.domain.use_cases.ParseWebLink
import ih.tools.readingpad.feature_bookmark.data.data_source.BookmarkDao
import ih.tools.readingpad.feature_bookmark.data.repository.BookmarkRepositoryImpl
import ih.tools.readingpad.feature_bookmark.domain.repository.BookmarkRepository
import ih.tools.readingpad.feature_bookmark.domain.use_cases.AddBookmark
import ih.tools.readingpad.feature_bookmark.domain.use_cases.BookmarkUseCases
import ih.tools.readingpad.feature_bookmark.domain.use_cases.CheckBookmarkExists
import ih.tools.readingpad.feature_bookmark.domain.use_cases.GetBookmarksForBook
import ih.tools.readingpad.feature_bookmark.domain.use_cases.RemoveBookmarkById
import ih.tools.readingpad.feature_bookmark.domain.use_cases.UpdateBookmarkTitle
import ih.tools.readingpad.feature_highlight.data.data_source.HighlightDao
import ih.tools.readingpad.feature_highlight.data.repository.HighlightRepositoryImpl
import ih.tools.readingpad.feature_highlight.domain.repository.HighlightRepository
import ih.tools.readingpad.feature_highlight.domain.use_cases.AddHighlight
import ih.tools.readingpad.feature_highlight.domain.use_cases.GetPageHighlights
import ih.tools.readingpad.feature_highlight.domain.use_cases.HighlightUseCases
import ih.tools.readingpad.feature_highlight.domain.use_cases.RemoveHighlightById
import ih.tools.readingpad.feature_theme_color.data.repository.ThemeColorRepositoryImpl
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository
import ih.tools.readingpad.feature_theme_color.domain.use_case.AddThemeColorUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.ColorExistsUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.DeleteAllThemeColorsUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.DeleteThemeColorUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.GetThemeColorsUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.ThemeColorUseCases
import javax.inject.Singleton


/**
ReadingPad library module to handle dependency injection with dagger hilt
 */
@Module
@InstallIn(SingletonComponent::class)
object ReadingPadModule {

    /**   This module object should still be placed within your library module.
    It's responsible for providing the actual implementations of the dependencies
    declared in LibraryEntryPoint.
    for example the regular @Provide functions
     */

    /** provides database object to any class that needs it*/
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    /** provides highlight dao object to any class that needs it*/
    @Provides
    @Singleton
    fun highlightDao(database: AppDatabase): HighlightDao {
        return database.highlightDao
    }

    /** provides highlight repository object to any class that needs it*/
    @Provides
    @Singleton
    fun provideHighlightRepository(db: AppDatabase): HighlightRepository {
        return HighlightRepositoryImpl(db.highlightDao)
    }

    /** provides bookmark dao object to any class that needs it*/
    @Provides
    @Singleton
    fun bookmarkDao(database: AppDatabase): BookmarkDao {
        return database.bookmarkDao
    }

    /** provides bookmark repository object to any class that needs it*/
    @Provides
    @Singleton
    fun provideBookmarkRepository(db: AppDatabase): BookmarkRepository {
        return BookmarkRepositoryImpl(db.bookmarkDao)
    }

    /** provides theme color repository object to any class that needs it*/
    @Provides
    @Singleton
    fun provideThemeColorRepository(db: AppDatabase): ThemeColorRepository {
        return ThemeColorRepositoryImpl(db.themeColorDao)
    }

    /** provides a context to any class that needs it */
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    /** provides bookmark use cases object to any class that needs it*/
    @Provides
    @Singleton
    fun provideBookmarkUseCases(repository: BookmarkRepository): BookmarkUseCases {
        return BookmarkUseCases(
            getBookmarksForBook = GetBookmarksForBook(repository),
            addBookmark = AddBookmark(repository),
            removeBookmarkById = RemoveBookmarkById(repository),
            updateBookmarkTitle = UpdateBookmarkTitle(repository),
            checkBookmarkExists = CheckBookmarkExists(repository)
        )
    }

    /** provides highlight use cases object to any class that needs it*/
    @Provides
    @Singleton
    fun provideHighlightUseCases(repository: HighlightRepository): HighlightUseCases {
        return HighlightUseCases(
            addHighlight = AddHighlight(repository),
            getPageHighlights = GetPageHighlights(repository),
            removeHighlightById = RemoveHighlightById(repository)

        )
    }

    /** provides book parser use cases object to any class that needs it*/
    @Provides
    @Singleton
    fun provideBookParserUseCases(): BookParserUseCases {
        return BookParserUseCases(
            parseImage = ParseImage(),
            parseFont = ParseFont(),
            parseInternalLink = ParseInternalLink(),
            parseWebLink = ParseWebLink(),
            parseRegularText = ParseRegularText(),
            parseBook = ParseBook(),
            parsePageLazy = ParsePageLazy(),
            parseInternalLinkLazy = ParseInternalLinkLazy(),
        )
    }

    @Provides
    @Singleton
    fun provideThemeColorUseCases(repository: ThemeColorRepository): ThemeColorUseCases {
        return ThemeColorUseCases(
            addThemeColorUseCase = AddThemeColorUseCase(repository),
            deleteThemeColorUseCase = DeleteThemeColorUseCase(repository),
            deleteAllThemeColorsUseCase = DeleteAllThemeColorsUseCase(repository),
            getThemeColorsUseCase = GetThemeColorsUseCase(repository),
            colorExistsUseCase = ColorExistsUseCase(repository)
        )
    }

    /** provides preferences manager object to any class that needs it*/
    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }

}