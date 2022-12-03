package at.ac.tuwien.inso.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.persistence.database.dao.FriendDao

/**
 * The Room Database with DAO functions.
 *
 * This class is abstract the Room Framework will extend
 * and generate the DAO functions.
 *
 * We can define multiple DAOs in the same Database file.
 * We can also add multiple Entities, which this database will handle.
 *
 * The version identifier in the annotation helps us with possible migrations
 * between app versions.
 * For more info, see: https://developer.android.com/training/data-storage/room/migrating-db-versions
 */
@Database(
    entities = [
        Song::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun friendDao(): FriendDao
}
