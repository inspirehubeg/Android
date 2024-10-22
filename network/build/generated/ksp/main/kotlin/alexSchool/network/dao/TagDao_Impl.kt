package alexSchool.network.dao

import alexSchool.network.entities.TagEntity
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
public class TagDao_Impl(
  __db: RoomDatabase,
) : TagDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTagEntity: EntityInsertAdapter<TagEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTagEntity = object : EntityInsertAdapter<TagEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `tags` (`id`,`name`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TagEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
      }
    }
  }

  public override suspend fun insertTag(tag: TagEntity): Unit = performSuspending(__db, false, true)
      { _connection ->
    __insertAdapterOfTagEntity.insert(_connection, tag)
  }

  public override suspend fun getAllTags(): List<TagEntity> {
    val _sql: String = "SELECT * FROM tags"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: MutableList<TagEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TagEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _item = TagEntity(_tmpId,_tmpName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTagById(tagId: Int): TagEntity? {
    val _sql: String = "SELECT * FROM tags WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, tagId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: TagEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _result = TagEntity(_tmpId,_tmpName)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTagById(tagId: Int) {
    val _sql: String = "DELETE FROM tags WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, tagId.toLong())
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
