package alexSchool.network.dao

import alexSchool.network.domain.Encoding
import alexSchool.network.domain.TargetLink
import alexSchool.network.dtos.ChapterDto
import alexSchool.network.entities.AuthorEntity
import alexSchool.network.entities.BookInfoEntity
import alexSchool.network.entities.CategoryEntity
import alexSchool.network.entities.Converters
import alexSchool.network.entities.InstantConverter
import alexSchool.network.entities.MetadataEntity
import alexSchool.network.entities.PublisherEntity
import alexSchool.network.entities.ReadingProgressEntity
import alexSchool.network.entities.SavedBookEntity
import alexSchool.network.entities.TagEntity
import alexSchool.network.entities.TokenEntity
import alexSchool.network.entities.TranslatorEntity
import alexSchool.network.entities.cross_tables.BookWithDetails
import androidx.collection.LongSparseArray
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import java.time.Instant
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class BookDao_Impl(
  __db: RoomDatabase,
) : BookDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTokenEntity: EntityInsertAdapter<TokenEntity>

  private val __insertAdapterOfMetadataEntity: EntityInsertAdapter<MetadataEntity>

  private val __converters: Converters = Converters()

  private val __insertAdapterOfBookInfoEntity: EntityInsertAdapter<BookInfoEntity>

  private val __insertAdapterOfReadingProgressEntity: EntityInsertAdapter<ReadingProgressEntity>

  private val __insertAdapterOfSavedBookEntity: EntityInsertAdapter<SavedBookEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTokenEntity = object : EntityInsertAdapter<TokenEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `tokens` (`id`,`bookId`,`firstPageNumber`,`lastPageNumber`,`firstChapterNumber`,`lastChapterNumber`,`count`,`content`,`size`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TokenEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.bookId.toLong())
        statement.bindLong(3, entity.firstPageNumber.toLong())
        statement.bindLong(4, entity.lastPageNumber.toLong())
        statement.bindLong(5, entity.firstChapterNumber.toLong())
        statement.bindLong(6, entity.lastChapterNumber.toLong())
        statement.bindLong(7, entity.count.toLong())
        statement.bindText(8, entity.content)
        statement.bindDouble(9, entity.size)
      }
    }
    this.__insertAdapterOfMetadataEntity = object : EntityInsertAdapter<MetadataEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `metadata` (`bookId`,`encoding`,`targetLinks`,`index`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: MetadataEntity) {
        statement.bindLong(1, entity.bookId.toLong())
        val _tmp: String = __converters.fromEncoding(entity.encoding)
        statement.bindText(2, _tmp)
        val _tmp_1: String = __converters.fromTargetLinkList(entity.targetLinks)
        statement.bindText(3, _tmp_1)
        val _tmp_2: String = __converters.fromChapterDtoList(entity.index)
        statement.bindText(4, _tmp_2)
      }
    }
    this.__insertAdapterOfBookInfoEntity = object : EntityInsertAdapter<BookInfoEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `bookInfo` (`id`,`name`,`description`,`summary`,`pagesNumber`,`chaptersNumber`,`readingProgress`,`subscriptionId`,`releaseDate`,`bookRating`,`cover`,`internationalNum`,`language`,`size`,`isDeleted`,`lastOpened`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BookInfoEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.description)
        statement.bindText(4, entity.summary)
        statement.bindLong(5, entity.pagesNumber.toLong())
        statement.bindLong(6, entity.chaptersNumber.toLong())
        val _tmpReadingProgress: Int? = entity.readingProgress
        if (_tmpReadingProgress == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmpReadingProgress.toLong())
        }
        statement.bindLong(8, entity.subscriptionId.toLong())
        val _tmpReleaseDate: String? = entity.releaseDate
        if (_tmpReleaseDate == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpReleaseDate)
        }
        val _tmpBookRating: Double? = entity.bookRating
        if (_tmpBookRating == null) {
          statement.bindNull(10)
        } else {
          statement.bindDouble(10, _tmpBookRating)
        }
        statement.bindText(11, entity.cover)
        val _tmpInternationalNum: String? = entity.internationalNum
        if (_tmpInternationalNum == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpInternationalNum)
        }
        statement.bindText(13, entity.language)
        val _tmpSize: Float? = entity.size
        if (_tmpSize == null) {
          statement.bindNull(14)
        } else {
          statement.bindDouble(14, _tmpSize.toDouble())
        }
        val _tmpIsDeleted: Boolean? = entity.isDeleted
        val _tmp: Int? = _tmpIsDeleted?.let { if (it) 1 else 0 }
        if (_tmp == null) {
          statement.bindNull(15)
        } else {
          statement.bindLong(15, _tmp.toLong())
        }
        val _tmpLastOpened: Instant? = entity.lastOpened
        val _tmp_1: Long? = InstantConverter.dateToTimestamp(_tmpLastOpened)
        if (_tmp_1 == null) {
          statement.bindNull(16)
        } else {
          statement.bindLong(16, _tmp_1)
        }
      }
    }
    this.__insertAdapterOfReadingProgressEntity = object :
        EntityInsertAdapter<ReadingProgressEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `reading_progress` (`bookId`,`userId`,`progress`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ReadingProgressEntity) {
        statement.bindLong(1, entity.bookId.toLong())
        statement.bindLong(2, entity.userId.toLong())
        statement.bindLong(3, entity.progress.toLong())
      }
    }
    this.__insertAdapterOfSavedBookEntity = object : EntityInsertAdapter<SavedBookEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `saved_books` (`userId`,`bookId`,`type`,`savedAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SavedBookEntity) {
        statement.bindLong(1, entity.userId.toLong())
        statement.bindLong(2, entity.bookId.toLong())
        statement.bindText(3, entity.type)
        val _tmpSavedAt: Instant? = entity.savedAt
        val _tmp: Long? = InstantConverter.dateToTimestamp(_tmpSavedAt)
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmp)
        }
      }
    }
  }

  public override suspend fun insertToken(tokenEntity: TokenEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfTokenEntity.insert(_connection, tokenEntity)
  }

  public override suspend fun insertMetadata(metadata: MetadataEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfMetadataEntity.insert(_connection, metadata)
  }

  public override suspend fun insertBook(book: BookInfoEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfBookInfoEntity.insert(_connection, book)
  }

  public override suspend fun insertReadingProgress(readingProgress: ReadingProgressEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfReadingProgressEntity.insert(_connection, readingProgress)
  }

  public override suspend fun insertSavedBook(savedBook: SavedBookEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSavedBookEntity.insert(_connection, savedBook)
  }

  public override suspend fun getTokenById(tokenId: Int, bookId: Int): TokenEntity? {
    val _sql: String = "SELECT * FROM tokens WHERE count = ? AND bookId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, tokenId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _cursorIndexOfFirstPageNumber: Int = getColumnIndexOrThrow(_stmt, "firstPageNumber")
        val _cursorIndexOfLastPageNumber: Int = getColumnIndexOrThrow(_stmt, "lastPageNumber")
        val _cursorIndexOfFirstChapterNumber: Int = getColumnIndexOrThrow(_stmt,
            "firstChapterNumber")
        val _cursorIndexOfLastChapterNumber: Int = getColumnIndexOrThrow(_stmt, "lastChapterNumber")
        val _cursorIndexOfCount: Int = getColumnIndexOrThrow(_stmt, "count")
        val _cursorIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _cursorIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _result: TokenEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpBookId: Int
          _tmpBookId = _stmt.getLong(_cursorIndexOfBookId).toInt()
          val _tmpFirstPageNumber: Int
          _tmpFirstPageNumber = _stmt.getLong(_cursorIndexOfFirstPageNumber).toInt()
          val _tmpLastPageNumber: Int
          _tmpLastPageNumber = _stmt.getLong(_cursorIndexOfLastPageNumber).toInt()
          val _tmpFirstChapterNumber: Int
          _tmpFirstChapterNumber = _stmt.getLong(_cursorIndexOfFirstChapterNumber).toInt()
          val _tmpLastChapterNumber: Int
          _tmpLastChapterNumber = _stmt.getLong(_cursorIndexOfLastChapterNumber).toInt()
          val _tmpCount: Int
          _tmpCount = _stmt.getLong(_cursorIndexOfCount).toInt()
          val _tmpContent: String
          _tmpContent = _stmt.getText(_cursorIndexOfContent)
          val _tmpSize: Double
          _tmpSize = _stmt.getDouble(_cursorIndexOfSize)
          _result =
              TokenEntity(_tmpId,_tmpBookId,_tmpFirstPageNumber,_tmpLastPageNumber,_tmpFirstChapterNumber,_tmpLastChapterNumber,_tmpCount,_tmpContent,_tmpSize)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllTokensForBook(bookId: Int): List<TokenEntity> {
    val _sql: String = "SELECT * FROM tokens WHERE bookId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _cursorIndexOfFirstPageNumber: Int = getColumnIndexOrThrow(_stmt, "firstPageNumber")
        val _cursorIndexOfLastPageNumber: Int = getColumnIndexOrThrow(_stmt, "lastPageNumber")
        val _cursorIndexOfFirstChapterNumber: Int = getColumnIndexOrThrow(_stmt,
            "firstChapterNumber")
        val _cursorIndexOfLastChapterNumber: Int = getColumnIndexOrThrow(_stmt, "lastChapterNumber")
        val _cursorIndexOfCount: Int = getColumnIndexOrThrow(_stmt, "count")
        val _cursorIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _cursorIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _result: MutableList<TokenEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TokenEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpBookId: Int
          _tmpBookId = _stmt.getLong(_cursorIndexOfBookId).toInt()
          val _tmpFirstPageNumber: Int
          _tmpFirstPageNumber = _stmt.getLong(_cursorIndexOfFirstPageNumber).toInt()
          val _tmpLastPageNumber: Int
          _tmpLastPageNumber = _stmt.getLong(_cursorIndexOfLastPageNumber).toInt()
          val _tmpFirstChapterNumber: Int
          _tmpFirstChapterNumber = _stmt.getLong(_cursorIndexOfFirstChapterNumber).toInt()
          val _tmpLastChapterNumber: Int
          _tmpLastChapterNumber = _stmt.getLong(_cursorIndexOfLastChapterNumber).toInt()
          val _tmpCount: Int
          _tmpCount = _stmt.getLong(_cursorIndexOfCount).toInt()
          val _tmpContent: String
          _tmpContent = _stmt.getText(_cursorIndexOfContent)
          val _tmpSize: Double
          _tmpSize = _stmt.getDouble(_cursorIndexOfSize)
          _item =
              TokenEntity(_tmpId,_tmpBookId,_tmpFirstPageNumber,_tmpLastPageNumber,_tmpFirstChapterNumber,_tmpLastChapterNumber,_tmpCount,_tmpContent,_tmpSize)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMetadataByBookId(bookId: Int): MetadataEntity? {
    val _sql: String = "SELECT * FROM metadata WHERE bookId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        val _cursorIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _cursorIndexOfEncoding: Int = getColumnIndexOrThrow(_stmt, "encoding")
        val _cursorIndexOfTargetLinks: Int = getColumnIndexOrThrow(_stmt, "targetLinks")
        val _cursorIndexOfIndex: Int = getColumnIndexOrThrow(_stmt, "index")
        val _result: MetadataEntity?
        if (_stmt.step()) {
          val _tmpBookId: Int
          _tmpBookId = _stmt.getLong(_cursorIndexOfBookId).toInt()
          val _tmpEncoding: Encoding
          val _tmp: String
          _tmp = _stmt.getText(_cursorIndexOfEncoding)
          _tmpEncoding = __converters.toEncoding(_tmp)
          val _tmpTargetLinks: List<TargetLink>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_cursorIndexOfTargetLinks)
          _tmpTargetLinks = __converters.toTargetLinkList(_tmp_1)
          val _tmpIndex: List<ChapterDto>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_cursorIndexOfIndex)
          _tmpIndex = __converters.toChapterDtoList(_tmp_2)
          _result = MetadataEntity(_tmpBookId,_tmpEncoding,_tmpTargetLinks,_tmpIndex)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookById(bookId: Int): BookInfoEntity? {
    val _sql: String = "SELECT * FROM bookinfo WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _cursorIndexOfPagesNumber: Int = getColumnIndexOrThrow(_stmt, "pagesNumber")
        val _cursorIndexOfChaptersNumber: Int = getColumnIndexOrThrow(_stmt, "chaptersNumber")
        val _cursorIndexOfReadingProgress: Int = getColumnIndexOrThrow(_stmt, "readingProgress")
        val _cursorIndexOfSubscriptionId: Int = getColumnIndexOrThrow(_stmt, "subscriptionId")
        val _cursorIndexOfReleaseDate: Int = getColumnIndexOrThrow(_stmt, "releaseDate")
        val _cursorIndexOfBookRating: Int = getColumnIndexOrThrow(_stmt, "bookRating")
        val _cursorIndexOfCover: Int = getColumnIndexOrThrow(_stmt, "cover")
        val _cursorIndexOfInternationalNum: Int = getColumnIndexOrThrow(_stmt, "internationalNum")
        val _cursorIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _cursorIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _cursorIndexOfLastOpened: Int = getColumnIndexOrThrow(_stmt, "lastOpened")
        val _result: BookInfoEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpSummary: String
          _tmpSummary = _stmt.getText(_cursorIndexOfSummary)
          val _tmpPagesNumber: Int
          _tmpPagesNumber = _stmt.getLong(_cursorIndexOfPagesNumber).toInt()
          val _tmpChaptersNumber: Int
          _tmpChaptersNumber = _stmt.getLong(_cursorIndexOfChaptersNumber).toInt()
          val _tmpReadingProgress: Int?
          if (_stmt.isNull(_cursorIndexOfReadingProgress)) {
            _tmpReadingProgress = null
          } else {
            _tmpReadingProgress = _stmt.getLong(_cursorIndexOfReadingProgress).toInt()
          }
          val _tmpSubscriptionId: Int
          _tmpSubscriptionId = _stmt.getLong(_cursorIndexOfSubscriptionId).toInt()
          val _tmpReleaseDate: String?
          if (_stmt.isNull(_cursorIndexOfReleaseDate)) {
            _tmpReleaseDate = null
          } else {
            _tmpReleaseDate = _stmt.getText(_cursorIndexOfReleaseDate)
          }
          val _tmpBookRating: Double?
          if (_stmt.isNull(_cursorIndexOfBookRating)) {
            _tmpBookRating = null
          } else {
            _tmpBookRating = _stmt.getDouble(_cursorIndexOfBookRating)
          }
          val _tmpCover: String
          _tmpCover = _stmt.getText(_cursorIndexOfCover)
          val _tmpInternationalNum: String?
          if (_stmt.isNull(_cursorIndexOfInternationalNum)) {
            _tmpInternationalNum = null
          } else {
            _tmpInternationalNum = _stmt.getText(_cursorIndexOfInternationalNum)
          }
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_cursorIndexOfLanguage)
          val _tmpSize: Float?
          if (_stmt.isNull(_cursorIndexOfSize)) {
            _tmpSize = null
          } else {
            _tmpSize = _stmt.getDouble(_cursorIndexOfSize).toFloat()
          }
          val _tmpIsDeleted: Boolean?
          val _tmp: Int?
          if (_stmt.isNull(_cursorIndexOfIsDeleted)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          }
          _tmpIsDeleted = _tmp?.let { it != 0 }
          val _tmpLastOpened: Instant?
          val _tmp_1: Long?
          if (_stmt.isNull(_cursorIndexOfLastOpened)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_cursorIndexOfLastOpened)
          }
          _tmpLastOpened = InstantConverter.fromTimestamp(_tmp_1)
          _result =
              BookInfoEntity(_tmpId,_tmpName,_tmpDescription,_tmpSummary,_tmpPagesNumber,_tmpChaptersNumber,_tmpReadingProgress,_tmpSubscriptionId,_tmpReleaseDate,_tmpBookRating,_tmpCover,_tmpInternationalNum,_tmpLanguage,_tmpSize,_tmpIsDeleted,_tmpLastOpened)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllBooks(): List<BookInfoEntity> {
    val _sql: String = "SELECT * FROM bookInfo"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _cursorIndexOfPagesNumber: Int = getColumnIndexOrThrow(_stmt, "pagesNumber")
        val _cursorIndexOfChaptersNumber: Int = getColumnIndexOrThrow(_stmt, "chaptersNumber")
        val _cursorIndexOfReadingProgress: Int = getColumnIndexOrThrow(_stmt, "readingProgress")
        val _cursorIndexOfSubscriptionId: Int = getColumnIndexOrThrow(_stmt, "subscriptionId")
        val _cursorIndexOfReleaseDate: Int = getColumnIndexOrThrow(_stmt, "releaseDate")
        val _cursorIndexOfBookRating: Int = getColumnIndexOrThrow(_stmt, "bookRating")
        val _cursorIndexOfCover: Int = getColumnIndexOrThrow(_stmt, "cover")
        val _cursorIndexOfInternationalNum: Int = getColumnIndexOrThrow(_stmt, "internationalNum")
        val _cursorIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _cursorIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _cursorIndexOfLastOpened: Int = getColumnIndexOrThrow(_stmt, "lastOpened")
        val _result: MutableList<BookInfoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookInfoEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpSummary: String
          _tmpSummary = _stmt.getText(_cursorIndexOfSummary)
          val _tmpPagesNumber: Int
          _tmpPagesNumber = _stmt.getLong(_cursorIndexOfPagesNumber).toInt()
          val _tmpChaptersNumber: Int
          _tmpChaptersNumber = _stmt.getLong(_cursorIndexOfChaptersNumber).toInt()
          val _tmpReadingProgress: Int?
          if (_stmt.isNull(_cursorIndexOfReadingProgress)) {
            _tmpReadingProgress = null
          } else {
            _tmpReadingProgress = _stmt.getLong(_cursorIndexOfReadingProgress).toInt()
          }
          val _tmpSubscriptionId: Int
          _tmpSubscriptionId = _stmt.getLong(_cursorIndexOfSubscriptionId).toInt()
          val _tmpReleaseDate: String?
          if (_stmt.isNull(_cursorIndexOfReleaseDate)) {
            _tmpReleaseDate = null
          } else {
            _tmpReleaseDate = _stmt.getText(_cursorIndexOfReleaseDate)
          }
          val _tmpBookRating: Double?
          if (_stmt.isNull(_cursorIndexOfBookRating)) {
            _tmpBookRating = null
          } else {
            _tmpBookRating = _stmt.getDouble(_cursorIndexOfBookRating)
          }
          val _tmpCover: String
          _tmpCover = _stmt.getText(_cursorIndexOfCover)
          val _tmpInternationalNum: String?
          if (_stmt.isNull(_cursorIndexOfInternationalNum)) {
            _tmpInternationalNum = null
          } else {
            _tmpInternationalNum = _stmt.getText(_cursorIndexOfInternationalNum)
          }
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_cursorIndexOfLanguage)
          val _tmpSize: Float?
          if (_stmt.isNull(_cursorIndexOfSize)) {
            _tmpSize = null
          } else {
            _tmpSize = _stmt.getDouble(_cursorIndexOfSize).toFloat()
          }
          val _tmpIsDeleted: Boolean?
          val _tmp: Int?
          if (_stmt.isNull(_cursorIndexOfIsDeleted)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          }
          _tmpIsDeleted = _tmp?.let { it != 0 }
          val _tmpLastOpened: Instant?
          val _tmp_1: Long?
          if (_stmt.isNull(_cursorIndexOfLastOpened)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_cursorIndexOfLastOpened)
          }
          _tmpLastOpened = InstantConverter.fromTimestamp(_tmp_1)
          _item =
              BookInfoEntity(_tmpId,_tmpName,_tmpDescription,_tmpSummary,_tmpPagesNumber,_tmpChaptersNumber,_tmpReadingProgress,_tmpSubscriptionId,_tmpReleaseDate,_tmpBookRating,_tmpCover,_tmpInternationalNum,_tmpLanguage,_tmpSize,_tmpIsDeleted,_tmpLastOpened)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookWithDetails(bookId: Int): BookWithDetails? {
    val _sql: String = "SELECT * FROM bookInfo WHERE id = ?"
    return performSuspending(__db, true, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfSummary: Int = getColumnIndexOrThrow(_stmt, "summary")
        val _cursorIndexOfPagesNumber: Int = getColumnIndexOrThrow(_stmt, "pagesNumber")
        val _cursorIndexOfChaptersNumber: Int = getColumnIndexOrThrow(_stmt, "chaptersNumber")
        val _cursorIndexOfReadingProgress: Int = getColumnIndexOrThrow(_stmt, "readingProgress")
        val _cursorIndexOfSubscriptionId: Int = getColumnIndexOrThrow(_stmt, "subscriptionId")
        val _cursorIndexOfReleaseDate: Int = getColumnIndexOrThrow(_stmt, "releaseDate")
        val _cursorIndexOfBookRating: Int = getColumnIndexOrThrow(_stmt, "bookRating")
        val _cursorIndexOfCover: Int = getColumnIndexOrThrow(_stmt, "cover")
        val _cursorIndexOfInternationalNum: Int = getColumnIndexOrThrow(_stmt, "internationalNum")
        val _cursorIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _cursorIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _cursorIndexOfLastOpened: Int = getColumnIndexOrThrow(_stmt, "lastOpened")
        val _collectionCategories: LongSparseArray<MutableList<CategoryEntity>> =
            LongSparseArray<MutableList<CategoryEntity>>()
        val _collectionTags: LongSparseArray<MutableList<TagEntity>> =
            LongSparseArray<MutableList<TagEntity>>()
        val _collectionAuthors: LongSparseArray<MutableList<AuthorEntity>> =
            LongSparseArray<MutableList<AuthorEntity>>()
        val _collectionTranslators: LongSparseArray<MutableList<TranslatorEntity>> =
            LongSparseArray<MutableList<TranslatorEntity>>()
        val _collectionPublishers: LongSparseArray<MutableList<PublisherEntity>> =
            LongSparseArray<MutableList<PublisherEntity>>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_cursorIndexOfId)
          if (!_collectionCategories.containsKey(_tmpKey)) {
            _collectionCategories.put(_tmpKey, mutableListOf())
          }
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_cursorIndexOfId)
          if (!_collectionTags.containsKey(_tmpKey_1)) {
            _collectionTags.put(_tmpKey_1, mutableListOf())
          }
          val _tmpKey_2: Long
          _tmpKey_2 = _stmt.getLong(_cursorIndexOfId)
          if (!_collectionAuthors.containsKey(_tmpKey_2)) {
            _collectionAuthors.put(_tmpKey_2, mutableListOf())
          }
          val _tmpKey_3: Long
          _tmpKey_3 = _stmt.getLong(_cursorIndexOfId)
          if (!_collectionTranslators.containsKey(_tmpKey_3)) {
            _collectionTranslators.put(_tmpKey_3, mutableListOf())
          }
          val _tmpKey_4: Long
          _tmpKey_4 = _stmt.getLong(_cursorIndexOfId)
          if (!_collectionPublishers.containsKey(_tmpKey_4)) {
            _collectionPublishers.put(_tmpKey_4, mutableListOf())
          }
        }
        _stmt.reset()
        __fetchRelationshipcategoriesAsalexSchoolNetworkEntitiesCategoryEntity(_connection,
            _collectionCategories)
        __fetchRelationshiptagsAsalexSchoolNetworkEntitiesTagEntity(_connection, _collectionTags)
        __fetchRelationshipauthorsAsalexSchoolNetworkEntitiesAuthorEntity(_connection,
            _collectionAuthors)
        __fetchRelationshiptranslatorsAsalexSchoolNetworkEntitiesTranslatorEntity(_connection,
            _collectionTranslators)
        __fetchRelationshippublishersAsalexSchoolNetworkEntitiesPublisherEntity(_connection,
            _collectionPublishers)
        val _result: BookWithDetails?
        if (_stmt.step()) {
          val _tmpBook: BookInfoEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpSummary: String
          _tmpSummary = _stmt.getText(_cursorIndexOfSummary)
          val _tmpPagesNumber: Int
          _tmpPagesNumber = _stmt.getLong(_cursorIndexOfPagesNumber).toInt()
          val _tmpChaptersNumber: Int
          _tmpChaptersNumber = _stmt.getLong(_cursorIndexOfChaptersNumber).toInt()
          val _tmpReadingProgress: Int?
          if (_stmt.isNull(_cursorIndexOfReadingProgress)) {
            _tmpReadingProgress = null
          } else {
            _tmpReadingProgress = _stmt.getLong(_cursorIndexOfReadingProgress).toInt()
          }
          val _tmpSubscriptionId: Int
          _tmpSubscriptionId = _stmt.getLong(_cursorIndexOfSubscriptionId).toInt()
          val _tmpReleaseDate: String?
          if (_stmt.isNull(_cursorIndexOfReleaseDate)) {
            _tmpReleaseDate = null
          } else {
            _tmpReleaseDate = _stmt.getText(_cursorIndexOfReleaseDate)
          }
          val _tmpBookRating: Double?
          if (_stmt.isNull(_cursorIndexOfBookRating)) {
            _tmpBookRating = null
          } else {
            _tmpBookRating = _stmt.getDouble(_cursorIndexOfBookRating)
          }
          val _tmpCover: String
          _tmpCover = _stmt.getText(_cursorIndexOfCover)
          val _tmpInternationalNum: String?
          if (_stmt.isNull(_cursorIndexOfInternationalNum)) {
            _tmpInternationalNum = null
          } else {
            _tmpInternationalNum = _stmt.getText(_cursorIndexOfInternationalNum)
          }
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_cursorIndexOfLanguage)
          val _tmpSize: Float?
          if (_stmt.isNull(_cursorIndexOfSize)) {
            _tmpSize = null
          } else {
            _tmpSize = _stmt.getDouble(_cursorIndexOfSize).toFloat()
          }
          val _tmpIsDeleted: Boolean?
          val _tmp: Int?
          if (_stmt.isNull(_cursorIndexOfIsDeleted)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          }
          _tmpIsDeleted = _tmp?.let { it != 0 }
          val _tmpLastOpened: Instant?
          val _tmp_1: Long?
          if (_stmt.isNull(_cursorIndexOfLastOpened)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_cursorIndexOfLastOpened)
          }
          _tmpLastOpened = InstantConverter.fromTimestamp(_tmp_1)
          _tmpBook =
              BookInfoEntity(_tmpId,_tmpName,_tmpDescription,_tmpSummary,_tmpPagesNumber,_tmpChaptersNumber,_tmpReadingProgress,_tmpSubscriptionId,_tmpReleaseDate,_tmpBookRating,_tmpCover,_tmpInternationalNum,_tmpLanguage,_tmpSize,_tmpIsDeleted,_tmpLastOpened)
          val _tmpCategoriesCollection: MutableList<CategoryEntity>
          val _tmpKey_5: Long
          _tmpKey_5 = _stmt.getLong(_cursorIndexOfId)
          _tmpCategoriesCollection = checkNotNull(_collectionCategories.get(_tmpKey_5))
          val _tmpTagsCollection: MutableList<TagEntity>
          val _tmpKey_6: Long
          _tmpKey_6 = _stmt.getLong(_cursorIndexOfId)
          _tmpTagsCollection = checkNotNull(_collectionTags.get(_tmpKey_6))
          val _tmpAuthorsCollection: MutableList<AuthorEntity>
          val _tmpKey_7: Long
          _tmpKey_7 = _stmt.getLong(_cursorIndexOfId)
          _tmpAuthorsCollection = checkNotNull(_collectionAuthors.get(_tmpKey_7))
          val _tmpTranslatorsCollection: MutableList<TranslatorEntity>
          val _tmpKey_8: Long
          _tmpKey_8 = _stmt.getLong(_cursorIndexOfId)
          _tmpTranslatorsCollection = checkNotNull(_collectionTranslators.get(_tmpKey_8))
          val _tmpPublishersCollection: MutableList<PublisherEntity>
          val _tmpKey_9: Long
          _tmpKey_9 = _stmt.getLong(_cursorIndexOfId)
          _tmpPublishersCollection = checkNotNull(_collectionPublishers.get(_tmpKey_9))
          _result =
              BookWithDetails(_tmpBook,_tmpCategoriesCollection,_tmpTagsCollection,_tmpAuthorsCollection,_tmpTranslatorsCollection,_tmpPublishersCollection)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getReadingProgressByUserId(userId: Int): List<ReadingProgressEntity> {
    val _sql: String = "SELECT * FROM reading_progress WHERE userId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId.toLong())
        val _cursorIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _result: MutableList<ReadingProgressEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReadingProgressEntity
          val _tmpBookId: Int
          _tmpBookId = _stmt.getLong(_cursorIndexOfBookId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId).toInt()
          val _tmpProgress: Int
          _tmpProgress = _stmt.getLong(_cursorIndexOfProgress).toInt()
          _item = ReadingProgressEntity(_tmpBookId,_tmpUserId,_tmpProgress)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getReadingProgressByBookIdAndUserId(bookId: Int, userId: Int):
      ReadingProgressEntity? {
    val _sql: String = "SELECT * FROM reading_progress WHERE bookId = ? AND userId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, userId.toLong())
        val _cursorIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfProgress: Int = getColumnIndexOrThrow(_stmt, "progress")
        val _result: ReadingProgressEntity?
        if (_stmt.step()) {
          val _tmpBookId: Int
          _tmpBookId = _stmt.getLong(_cursorIndexOfBookId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId).toInt()
          val _tmpProgress: Int
          _tmpProgress = _stmt.getLong(_cursorIndexOfProgress).toInt()
          _result = ReadingProgressEntity(_tmpBookId,_tmpUserId,_tmpProgress)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSavedBooksByUserId(userId: Int): List<SavedBookEntity> {
    val _sql: String = "SELECT * FROM saved_books WHERE userId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId.toLong())
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfBookId: Int = getColumnIndexOrThrow(_stmt, "bookId")
        val _cursorIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _cursorIndexOfSavedAt: Int = getColumnIndexOrThrow(_stmt, "savedAt")
        val _result: MutableList<SavedBookEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SavedBookEntity
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId).toInt()
          val _tmpBookId: Int
          _tmpBookId = _stmt.getLong(_cursorIndexOfBookId).toInt()
          val _tmpType: String
          _tmpType = _stmt.getText(_cursorIndexOfType)
          val _tmpSavedAt: Instant?
          val _tmp: Long?
          if (_stmt.isNull(_cursorIndexOfSavedAt)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_cursorIndexOfSavedAt)
          }
          _tmpSavedAt = InstantConverter.fromTimestamp(_tmp)
          _item = SavedBookEntity(_tmpUserId,_tmpBookId,_tmpType,_tmpSavedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTokenById(tokenId: Int, bookId: Int) {
    val _sql: String = "DELETE FROM tokens WHERE count = ? AND bookId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, tokenId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, bookId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllTokensForBook(bookId: Int) {
    val _sql: String = "DELETE FROM tokens WHERE bookId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteBookById(bookId: Int) {
    val _sql: String = "DELETE FROM bookInfo WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSavedBook(bookId: Int, userId: Int) {
    val _sql: String = "DELETE FROM saved_books WHERE bookId = ? AND userId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, bookId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, userId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  private
      fun __fetchRelationshipcategoriesAsalexSchoolNetworkEntitiesCategoryEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<CategoryEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshipcategoriesAsalexSchoolNetworkEntitiesCategoryEntity(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `categories`.`id` AS `id`,`categories`.`name` AS `name`,`categories`.`image` AS `image`,_junction.`bookId` FROM `book_categories` AS _junction INNER JOIN `categories` ON (_junction.`categoryId` = `categories`.`id`) WHERE _junction.`bookId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      // _junction.bookId
      val _itemKeyIndex: Int = 3
      if (_itemKeyIndex == -1) {
        return
      }
      val _cursorIndexOfId: Int = 0
      val _cursorIndexOfName: Int = 1
      val _cursorIndexOfImage: Int = 2
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<CategoryEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: CategoryEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpImage: String?
          if (_stmt.isNull(_cursorIndexOfImage)) {
            _tmpImage = null
          } else {
            _tmpImage = _stmt.getText(_cursorIndexOfImage)
          }
          _item_1 = CategoryEntity(_tmpId,_tmpName,_tmpImage)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  private
      fun __fetchRelationshiptagsAsalexSchoolNetworkEntitiesTagEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<TagEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshiptagsAsalexSchoolNetworkEntitiesTagEntity(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `tags`.`id` AS `id`,`tags`.`name` AS `name`,_junction.`bookId` FROM `BookTag` AS _junction INNER JOIN `tags` ON (_junction.`tagId` = `tags`.`id`) WHERE _junction.`bookId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      // _junction.bookId
      val _itemKeyIndex: Int = 2
      if (_itemKeyIndex == -1) {
        return
      }
      val _cursorIndexOfId: Int = 0
      val _cursorIndexOfName: Int = 1
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<TagEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: TagEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _item_1 = TagEntity(_tmpId,_tmpName)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  private
      fun __fetchRelationshipauthorsAsalexSchoolNetworkEntitiesAuthorEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<AuthorEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshipauthorsAsalexSchoolNetworkEntitiesAuthorEntity(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `authors`.`id` AS `id`,`authors`.`name` AS `name`,`authors`.`bio` AS `bio`,`authors`.`image` AS `image`,_junction.`bookId` FROM `BookAuthor` AS _junction INNER JOIN `authors` ON (_junction.`authorId` = `authors`.`id`) WHERE _junction.`bookId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      // _junction.bookId
      val _itemKeyIndex: Int = 4
      if (_itemKeyIndex == -1) {
        return
      }
      val _cursorIndexOfId: Int = 0
      val _cursorIndexOfName: Int = 1
      val _cursorIndexOfBio: Int = 2
      val _cursorIndexOfImage: Int = 3
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<AuthorEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: AuthorEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpBio: String
          _tmpBio = _stmt.getText(_cursorIndexOfBio)
          val _tmpImage: String?
          if (_stmt.isNull(_cursorIndexOfImage)) {
            _tmpImage = null
          } else {
            _tmpImage = _stmt.getText(_cursorIndexOfImage)
          }
          _item_1 = AuthorEntity(_tmpId,_tmpName,_tmpBio,_tmpImage)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  private
      fun __fetchRelationshiptranslatorsAsalexSchoolNetworkEntitiesTranslatorEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<TranslatorEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshiptranslatorsAsalexSchoolNetworkEntitiesTranslatorEntity(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `translators`.`id` AS `id`,`translators`.`name` AS `name`,_junction.`bookId` FROM `BookTranslator` AS _junction INNER JOIN `translators` ON (_junction.`translatorId` = `translators`.`id`) WHERE _junction.`bookId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      // _junction.bookId
      val _itemKeyIndex: Int = 2
      if (_itemKeyIndex == -1) {
        return
      }
      val _cursorIndexOfId: Int = 0
      val _cursorIndexOfName: Int = 1
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<TranslatorEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: TranslatorEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _item_1 = TranslatorEntity(_tmpId,_tmpName)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  private
      fun __fetchRelationshippublishersAsalexSchoolNetworkEntitiesPublisherEntity(_connection: SQLiteConnection,
      _map: LongSparseArray<MutableList<PublisherEntity>>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, true) { _tmpMap ->
        __fetchRelationshippublishersAsalexSchoolNetworkEntitiesPublisherEntity(_connection,
            _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `publishers`.`id` AS `id`,`publishers`.`name` AS `name`,_junction.`bookId` FROM `book_publisher` AS _junction INNER JOIN `publishers` ON (_junction.`publisherId` = `publishers`.`id`) WHERE _junction.`bookId` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      // _junction.bookId
      val _itemKeyIndex: Int = 2
      if (_itemKeyIndex == -1) {
        return
      }
      val _cursorIndexOfId: Int = 0
      val _cursorIndexOfName: Int = 1
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        val _tmpRelation: MutableList<PublisherEntity>? = _map.get(_tmpKey)
        if (_tmpRelation != null) {
          val _item_1: PublisherEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _item_1 = PublisherEntity(_tmpId,_tmpName)
          _tmpRelation.add(_item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
