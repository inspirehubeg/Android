package alexSchool.network.dao

import alexSchool.network.entities.TranslatorEntity
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
public class TranslatorDao_Impl(
  __db: RoomDatabase,
) : TranslatorDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTranslatorEntity: EntityInsertAdapter<TranslatorEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTranslatorEntity = object : EntityInsertAdapter<TranslatorEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `translators` (`id`,`name`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TranslatorEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
      }
    }
  }

  public override suspend fun insertTranslator(translator: TranslatorEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTranslatorEntity.insert(_connection, translator)
  }

  public override suspend fun getAllTranslators(): List<TranslatorEntity> {
    val _sql: String = "SELECT * FROM translators"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: MutableList<TranslatorEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TranslatorEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _item = TranslatorEntity(_tmpId,_tmpName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTranslatorById(translatorId: Int): TranslatorEntity? {
    val _sql: String = "SELECT * FROM translators WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, translatorId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: TranslatorEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _result = TranslatorEntity(_tmpId,_tmpName)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTranslatorById(translatorId: Int) {
    val _sql: String = "DELETE FROM translators WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, translatorId.toLong())
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
