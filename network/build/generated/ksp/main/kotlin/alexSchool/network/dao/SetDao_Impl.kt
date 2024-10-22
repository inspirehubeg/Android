package alexSchool.network.dao

import alexSchool.network.entities.SetEntity
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
public class SetDao_Impl(
  __db: RoomDatabase,
) : SetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSetEntity: EntityInsertAdapter<SetEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSetEntity = object : EntityInsertAdapter<SetEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `sets` (`id`,`userId`,`name`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SetEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindLong(2, entity.userId.toLong())
        statement.bindText(3, entity.name)
      }
    }
  }

  public override suspend fun insertSet(`set`: SetEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfSetEntity.insert(_connection, set)
  }

  public override suspend fun getAllSets(): List<SetEntity> {
    val _sql: String = "SELECT * FROM sets"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: MutableList<SetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SetEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _item = SetEntity(_tmpId,_tmpUserId,_tmpName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSetById(setId: Int): SetEntity? {
    val _sql: String = "SELECT * FROM sets WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, setId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: SetEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _result = SetEntity(_tmpId,_tmpUserId,_tmpName)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSetsByUserId(userId: Int): List<SetEntity> {
    val _sql: String = "SELECT * FROM sets WHERE userId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _result: MutableList<SetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SetEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_cursorIndexOfId).toInt()
          val _tmpUserId: Int
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId).toInt()
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          _item = SetEntity(_tmpId,_tmpUserId,_tmpName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSetById(setId: Int) {
    val _sql: String = "DELETE FROM sets WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, setId.toLong())
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
