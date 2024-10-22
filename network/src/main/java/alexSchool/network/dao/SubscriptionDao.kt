package alexSchool.network.dao

import alexSchool.network.entities.SubscriptionEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions")
    suspend fun getAllSubscriptions(): List<SubscriptionEntity>

    @Insert(entity = SubscriptionEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: SubscriptionEntity)

    @Query("DELETE FROM subscriptions WHERE id = :subscriptionId")
    suspend fun deleteSubscriptionById(subscriptionId: Int)


    @Query("SELECT * FROM subscriptions WHERE id = :subscriptionId")
    suspend fun getSubscriptionById(subscriptionId: Int): SubscriptionEntity?

}