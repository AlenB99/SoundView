package at.ac.tuwien.inso.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_SONG = "Songs"
const val COLUMN_SONG_ID = "id"
const val COLUMN_SONG_TITLE = "title"
const val COLUMN_SONG_ARTIST = "artist"

/**
 * A data class (POJO) which represents a basic Song.
 *
 * This is a Room Entity and can be persisted in the database.
 */
@Entity(tableName = TABLE_NAME_SONG)
data class Song(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_SONG_ID)
    val id: Int,

    @ColumnInfo(name = COLUMN_SONG_TITLE)
    var title: String,

    @ColumnInfo(name = COLUMN_SONG_ARTIST)
    var artist: String
) {
    override fun toString(): String {
        return "Song(id=$id, title='$title', artist='$artist')"
    }
}
