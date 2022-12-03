package at.ac.tuwien.inso.repository

import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.persistence.database.dao.FriendDao
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

/**
 * A Repository for [Song] related data.
 *
 * This acts as an abstraction layer to access API and database.
 */
class FriendRepository(
    private val songDao: FriendDao
) {

    /**
     * Returns a [Flow] for the list of friends.
     * If the data in the database changes, the flow gets re-triggered.
     */
    fun getFriends(): Flow<List<Song>> = songDao.getFriends()

    /**
     * Creates a new [Friend].
     */
    suspend fun insert(song: Song): Boolean {
        // Initially add it to the database, so no refresh is needed
        songDao.insert(song)
        return try {
            // Upload the friend through the API
            // uploadFriend(song)
            true
        } catch (ex: Exception) {
            // If the API call fails, remove it again from the database
            songDao.delete(song)
            false
        }
    }

    /**
     * Deletes the given [Friend].
     */
    suspend fun delete(song: Song): Boolean {
        // Initially remove it from the database, so no refresh is needed
        songDao.delete(song)
        return try {
            // Tell the backend to delete the friend
            // removeFriend(song)
            true
        } catch (ex: Exception) {
            // If the API call fails, add it again from the database
            songDao.insert(song)
            false
        }
    }

    /**
     * Calls the DALLE API and inserts the result to the Room database.
     */
    suspend fun generateCover() {
        //TODO: ADD API Call

        // Structured Concurrency: two asynchronous (suspend) function calls
        // with return values and Exception handling implemented in a structured
        // and easy to read format.
        //val apiCovers = getFriendsFromApi()
        songDao.deleteAll()
        //songDao.insert(*apiCovers.toTypedArray())
    }
}
