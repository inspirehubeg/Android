package alexSchool.network.dao

import alexSchool.network.entities.CategoryEntity
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
public class CategoryDao_Impl(
  __db: RoomDatabase,
) : CategoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfCategoryEntity: EntityInsertAdapter<CategoryEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfCategoryEntity = object : EntityInsertAdapter<CategoryEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `categories` (`id`,`name`,`image`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: CategoryEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.name)
        val _tmpImage: String? = entity.image
        if (_tmpImage == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpImage)
        }
      }
    }
  }

  public override suspend fun insertCategory(category: CategoryEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfCategoryEntity.insert(_connection, category)
  }

  public override suspend fun getAllCategories(): List<CategoryEntity> {
    val _sql: String = "SELECT * FROM categories"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _result: MutableList<CategoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategoryEntity
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
          _item = CategoryEntity(_tmpId,_tmpName,_tmpImage)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCategoryIds(): List<Int> {
    val _sql: String = "SELECT id FROM categories"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<Int> = mutableListOf()
        while (_stmt.step()) {
          val _item: Int
          _item = _stmt.getLong(0).toInt()
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCategoryById(categoryId: Int): CategoryEntity? {
    val _sql: String = "SELECT * FROM categories WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, categoryId.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfImage: Int = getColumnIndexOrThrow(_stmt, "image")
        val _result: CategoryEntity?
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
          _result = CategoryEntity(_tmpId,_tmpName,_tmpImage)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteCategoryById(categoryId: Int) {
    val _sql: String = "DELETE FROM categories WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, categoryId.toLong())
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
