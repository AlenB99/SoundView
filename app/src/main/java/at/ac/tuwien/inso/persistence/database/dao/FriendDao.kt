package at.ac.tuwien.inso.persistence.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.ac.tuwien.inso.model.Friend
import at.ac.tuwien.inso.model.TABLE_NAME_FRIEND
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for [Friend].
 *
 * This class is abstract as the Room Framework will extend and generate the
 * respective functions and SQL queries.
 *
 * In the DAO, we can define all database relevant functions in simple ways.
 * We can use annotations like [Delete] to delete a given entry.
 * We can also create custom queries through the [Query] annotation.
 *
 * The Framework will fully implement the given functions depending on the annotations.
 */
@Dao
interface FriendDao {

    /**
     * Get all [Friend] from the database.
     *
     * @return A [Flow] with a list of all friends in the database.
     */
    @Query(
        """
        SELECT *
        FROM $TABLE_NAME_FRIEND
    """
    )
    fun getFriends(): Flow<List<Friend>>

    /**
     * Inserts the given [Friend] to the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg friends: Friend)

    /**
     * Removes the given [Friend] from the database.
     */
    @Delete
    suspend fun delete(vararg friend: Friend)

    /**
     * Removes all [Friend]s from the database.
     */
    @Query(
        """
        DELETE FROM $TABLE_NAME_FRIEND
    """
    )
    suspend fun deleteAll()
}
