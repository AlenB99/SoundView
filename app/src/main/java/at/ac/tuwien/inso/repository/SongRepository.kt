package at.ac.tuwien.inso.repository

import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.persistance.dao.SongDao
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

/**
 * A Repository for [Song] related data.
 *
 * This acts as an abstraction layer to access API and database.
 */
class SongRepository(
    private val songDao: SongDao
) {

    /**
     * Returns a [Flow] for the list of songs.
     * If the data in the database changes, the flow gets re-triggered.
     */
    fun getSongs(): Flow<List<Song>> = songDao.getSongs()

    /**
     * Creates a new [Song].
     */
    suspend fun insert(song: Song) {
        // Initially add it to the database, so no refresh is needed
        songDao.insert(song)
    }

    /**
     * Deletes the given [Song].
     */
    suspend fun delete(song: Song): Boolean {
        // Initially remove it from the database, so no refresh is needed
        songDao.delete(song)
        return try {
            // Tell the backend to delete the song
            //removeSong(song)
            true
        } catch (ex: Exception) {
            // If the API call fails, add it again from the database
            songDao.insert(song)
            false
        }
    }

    /**
     * Calls the fake API and inserts the result to the Room database.
     */
    suspend fun refreshSongs() {
        // Structured Concurrency: two asynchronous (suspend) function calls
        // with return values and Exception handling implemented in a structured
        // and easy to read format.
        /*val apiFriends = getFriendsFromApi()
        songDao.deleteAll()
        songDao.insert(*apiFriends.toTypedArray())*/
    }
}
