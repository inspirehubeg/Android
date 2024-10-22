package alexSchool.network.dao

import alexSchool.network.entities.SubscriptionEntity
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class SubscriptionDao_Impl(
  __db: RoomDatabase,
) : SubscriptionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSubscriptionEntity: EntityInsertAdapter<SubscriptionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSubscriptionEntity = object : EntityInsertAdapter<SubscriptionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `subscriptions` (`id`,`name`,`image`,`isDeleted`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SubscriptionEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        val _tmpImage: String? = entity.image
        if (_tmpImage == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpImage)
        }
        val _tmpIsDeleted: Boolean? = entity.isDeleted
        val _tmp: Int? = _tmpIsDeleted?.let { if (it) 1 else 0 }
        if (_tmp == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmp.toLong())
        }
      }
    }
  }

  public override suspend fun insertSubscription(subscription: SubscriptionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSubscriptionEntity.insert(_connection, subscription)
  }

  public override suspend fun getAllSubscriptions(): List<SubscriptionEntity> {
    val _sql: String = "SELECT * FROM subscriptions"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _result: MutableList<SubscriptionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SubscriptionEntity
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
          val _tmpIsDeleted: Boolean?
          val _tmp: Int?
          if (_stmt.isNull(_cursorIndexOfIsDeleted)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          }
          _tmpIsDeleted = _tmp?.let { it != 0 }
          _item = SubscriptionEntity(_tmpId,_tmpName,_tmpImage,_tmpIsDeleted)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSubscriptionById(subscriptionId: Int): SubscriptionEntity? {
    val _sql: String = "SELECT * FROM subscriptions WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, subscriptionId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _cursorIndexOfIsDeleted: Int = getColumnIndexOrThrow(_stmt, "isDeleted")
        val _result: SubscriptionEntity?
        if (_stmt.step()) {
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
          val _tmpIsDeleted: Boolean?
          val _tmp: Int?
          if (_stmt.isNull(_cursorIndexOfIsDeleted)) {
            _tmp = null
          } else {
            _tmp = _stmt.getLong(_cursorIndexOfIsDeleted).toInt()
          }
          _tmpIsDeleted = _tmp?.let { it != 0 }
          _result = SubscriptionEntity(_tmpId,_tmpName,_tmpImage,_tmpIsDeleted)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSubscriptionById(subscriptionId: Int) {
    val _sql: String = "DELETE FROM subscriptions WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, subscriptionId.toLong())
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
