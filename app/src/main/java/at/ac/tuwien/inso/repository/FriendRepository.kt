package at.ac.tuwien.inso.repository

import at.ac.tuwien.inso.api.getFriendsFromApi
import at.ac.tuwien.inso.api.removeFriend
import at.ac.tuwien.inso.api.uploadFriend
import at.ac.tuwien.inso.model.Friend
import at.ac.tuwien.inso.persistence.database.dao.FriendDao
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

/**
 * A Repository for [Friend] related data.
 *
 * This acts as an abstraction layer to access API and database.
 */
class FriendRepository(
    private val friendDao: FriendDao
) {

    /**
     * Returns a [Flow] for the list of friends.
     * If the data in the database changes, the flow gets re-triggered.
     */
    fun getFriends(): Flow<List<Friend>> = friendDao.getFriends()

    /**
     * Creates a new [Friend].
     */
    suspend fun insert(friend: Friend): Boolean {
        // Initially add it to the database, so no refresh is needed
        friendDao.insert(friend)
        return try {
            // Upload the friend through the API
            uploadFriend(friend)
            true
        } catch (ex: Exception) {
            // If the API call fails, remove it again from the database
            friendDao.delete(friend)
            false
        }
    }

    /**
     * Deletes the given [Friend].
     */
    suspend fun delete(friend: Friend): Boolean {
        // Initially remove it from the database, so no refresh is needed
        friendDao.delete(friend)
        return try {
            // Tell the backend to delete the friend
            removeFriend(friend)
            true
        } catch (ex: Exception) {
            // If the API call fails, add it again from the database
            friendDao.insert(friend)
            false
        }
    }

    /**
     * Calls the fake API and inserts the result to the Room database.
     */
    suspend fun refreshFriends() {
        // Structured Concurrency: two asynchronous (suspend) function calls
        // with return values and Exception handling implemented in a structured
        // and easy to read format.
        val apiFriends = getFriendsFromApi()
        friendDao.deleteAll()
        friendDao.insert(*apiFriends.toTypedArray())
    }
}
