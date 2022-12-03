package at.ac.tuwien.inso.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_SONG = "songs"
const val COLUMN_SONG_ID = "id"
const val COLUMN_SONG_NAME = "name"
const val COLUMN_SONG_ARTIST = "score"

/**
 * A data class (POJO) which represents a basic Songs that has been scanned.
 *
 * This is a Room Entity and can be persisted in the database.
 */
@Entity(tableName = TABLE_NAME_SONG)
data class Song(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_SONG_ID)
    val id: Int,

    @ColumnInfo(name = COLUMN_SONG_NAME)
    var name: String,

    @ColumnInfo(name = COLUMN_SONG_ARTIST)
    var artist: String,
)
