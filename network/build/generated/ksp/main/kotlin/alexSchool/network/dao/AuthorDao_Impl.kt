package alexSchool.network.dao

import alexSchool.network.entities.AuthorEntity
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AuthorDao_Impl(
  __db: RoomDatabase,
) : AuthorDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAuthorEntity: EntityInsertAdapter<AuthorEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAuthorEntity = object : EntityInsertAdapter<AuthorEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `authors` (`id`,`name`,`bio`,`image`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AuthorEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.bio)
        val _tmpImage: String? = entity.image
        if (_tmpImage == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpImage)
        }
      }
    }
  }

  public override suspend fun insertAuthor(author: AuthorEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfAuthorEntity.insert(_connection, author)
  }

  public override suspend fun getAuthorById(authorId: Int): AuthorEntity? {
    val _sql: String = "SELECT * FROM authors WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, authorId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfBio: Int = getColumnIndexOrThrow(_stmt, "bio")
        val _cursorIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _result: AuthorEntity?
        if (_stmt.step()) {
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
          _result = AuthorEntity(_tmpId,_tmpName,_tmpBio,_tmpImage)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAllAuthors(): List<AuthorEntity> {
    val _sql: String = "SELECT * FROM authors"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfBio: Int = getColumnIndexOrThrow(_stmt, "bio")
        val _cursorIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _result: MutableList<AuthorEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AuthorEntity
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
          _item = AuthorEntity(_tmpId,_tmpName,_tmpBio,_tmpImage)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAuthorById(authorId: Int) {
    val _sql: String = "DELETE FROM authors WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, authorId.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
