package at.ac.tuwien.inso.persistance.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.model.TABLE_NAME_SONG
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for [Song].
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
interface SongDao {

    /**
     * Get all [Song] from the database.
     *
     * @return A [Flow] with a list of all Songs in the database.
     */
    @Query(
        """
        SELECT *
        FROM $TABLE_NAME_SONG
    """
    )
    fun getSongs(): Flow<List<Song>>

    /**
     * Inserts the given [Song] to the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg song: Song)

    /**
     * Removes the given [Song] from the database.
     */
    @Delete
    suspend fun delete(vararg song: Song)

    /**
     * Removes all [Song]s from the database.
     */
    @Query(
        """
        DELETE FROM $TABLE_NAME_SONG
    """
    )
    suspend fun deleteAll()
}