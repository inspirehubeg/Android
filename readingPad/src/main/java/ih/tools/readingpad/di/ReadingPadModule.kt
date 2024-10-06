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
import ih.tools.readingpad.feature_book_parsing.data.data_source.ReadingPadDatabase
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
import ih.tools.readingpad.feature_highlight.domain.use_cases.GetBookHighlights
import ih.tools.readingpad.feature_highlight.domain.use_cases.GetPageHighlights
import ih.tools.readingpad.feature_highlight.domain.use_cases.HighlightUseCases
import ih.tools.readingpad.feature_highlight.domain.use_cases.RemoveHighlightById
import ih.tools.readingpad.feature_note.data.repository.NoteRepositoryImpl
import ih.tools.readingpad.feature_note.domain.repository.NoteRepository
import ih.tools.readingpad.feature_note.domain.use_cases.AddNote
import ih.tools.readingpad.feature_note.domain.use_cases.DeleteNoteById
import ih.tools.readingpad.feature_note.domain.use_cases.GetBookNotes
import ih.tools.readingpad.feature_note.domain.use_cases.GetPageNotes
import ih.tools.readingpad.feature_note.domain.use_cases.NoteUseCases
import ih.tools.readingpad.feature_note.domain.use_cases.UpdateNoteText
import ih.tools.readingpad.feature_theme_color.data.repository.ThemeColorRepositoryImpl
import ih.tools.readingpad.feature_theme_color.domain.repository.ThemeColorRepository
import ih.tools.readingpad.feature_theme_color.domain.use_case.AddThemeColorUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.ColorExistsUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.DeleteAllThemeColorsUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.DeleteThemeColorUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.GetThemeColorsUseCase
import ih.tools.readingpad.feature_theme_color.domain.use_case.ThemeColorUseCases
import ih.tools.readingpad.network.BookInputApi
import ih.tools.readingpad.network.BookInputApiImpl
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


/**
 * Dagger Hilt module for providing dependencies related to the ReadingPad library.
 * This module is installed in the SingletonComponent,
 * ensuring that dependencies are available throughout the application lifecycle.*/

@Module
@InstallIn(SingletonComponent::class)
object ReadingPadModule {

    /**   This module object should still be placed within your library module.
    It's responsible for providing the actual implementations of the dependencies
    declared in LibraryEntryPoint.
    for example the regular @Provide functions
     */

    /**
     * Provides an instance of AppDatabase.
     *
     * @param context The application context.
     * @return An instance of AppDatabase.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ReadingPadDatabase {
        return Room.databaseBuilder(
            context,
            ReadingPadDatabase::class.java, "reading_pad_database"
        ).build()
    }

    /**
     * Provides an instance of HighlightDao.
     *
     * @param database The AppDatabase instance.
     * @return An instance of HighlightDao.
     */
    @Provides
    @Singleton
    fun highlightDao(database: ReadingPadDatabase): HighlightDao {
        return database.highlightDao
    }

    /**
     * Provides an instance of HighlightRepository.
     *
     * @param database The AppDatabase instance.
     * @return An instance of HighlightRepository.
     */
    @Provides
    @Singleton
    fun provideHighlightRepository(
        database: ReadingPadDatabase,
        defaultDispatcher: CoroutineDispatcher,
        inputApi: BookInputApi
    ): HighlightRepository {
        return HighlightRepositoryImpl(
            database.highlightDao,
            defaultDispatcher = defaultDispatcher,
            inputApi = inputApi
        )
    }

    /**
     * Provides an instance of BookmarkDao.
     *
     * @param database The AppDatabase instance.
     * @return An instance of BookmarkDao.
     */
    @Provides
    @Singleton
    fun provideBookmarkDao(database: ReadingPadDatabase): BookmarkDao {
        return database.bookmarkDao
    }

    /**
     * Provides an instance of BookmarkRepository.
     *
     * @param database The AppDatabase instance.
     * @return An instance of BookmarkRepository.
     */
    @Provides
    @Singleton
    fun provideBookmarkRepository(
        database: ReadingPadDatabase,
        defaultDispatcher: CoroutineDispatcher,
        inputApi: BookInputApi
    ): BookmarkRepository {
        return BookmarkRepositoryImpl(
            database.bookmarkDao,
            defaultDispatcher = defaultDispatcher,
            inputApi = inputApi
        )
    }

    /**
     * Provides an instance of ThemeColorRepository.
     *
     * @param database The AppDatabase instance.
     * @return An instance of ThemeColorRepository.
     */
    @Provides
    @Singleton
    fun provideThemeColorRepository(database: ReadingPadDatabase): ThemeColorRepository {
        return ThemeColorRepositoryImpl(database.themeColorDao)
    }

    /**
     * Provides the application context.
     *
     * @param application The application instance.
     * @return The application context.
     */
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext


    /**
     * Provides an instance of BookmarkUseCases.
     *
     * @param repository The BookmarkRepository instance.
     * @return An instance of BookmarkUseCases.
     */
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

    /**
     * Provides an instance of HighlightUseCases.
     *
     * @param repository The HighlightRepository instance.
     * @return An instance of HighlightUseCases.
     */
    @Provides
    @Singleton
    fun provideHighlightUseCases(repository: HighlightRepository): HighlightUseCases {
        return HighlightUseCases(
            addHighlight = AddHighlight(repository),
            getPageHighlights = GetPageHighlights(repository),
            removeHighlightById = RemoveHighlightById(repository),
            getBookHighlights = GetBookHighlights(repository)
        )
    }

    /**
     * Provides an instance of BookParserUseCases.
     *
     * @return An instance of BookParserUseCases.
     */
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

    /**
     * Provides an instance of ThemeColorUseCases.
     *
     * @param repository The ThemeColorRepository instance.
     * @return An instance of ThemeColorUseCases.
     */
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

    /**
     * Provides an instance of PreferencesManager.
     *
     * @param context The application context.
     * @return An instance of PreferencesManager.
     */
    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }


    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            addNote = AddNote(repository),
            deleteNoteById = DeleteNoteById(repository),
            getBookNotes = GetBookNotes(repository),
            getPageNotes = GetPageNotes(repository),
            updateNoteText = UpdateNoteText(repository)
        )
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        database: ReadingPadDatabase,
        inputApi: BookInputApi,
        defaultDispatcher: CoroutineDispatcher
    ): NoteRepository {
        return NoteRepositoryImpl(database.noteDao, inputApi, defaultDispatcher)
    }


    @Singleton
    @Provides
    fun provideBookInputApi(httpClient: HttpClient): BookInputApi = BookInputApiImpl(httpClient)

}