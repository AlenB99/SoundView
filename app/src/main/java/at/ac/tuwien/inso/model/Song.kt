package at.ac.tuwien.inso.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_SONG = "Songs"
const val COLUMN_SONG_ID = "id"
const val COLUMN_SONG_TITLE = "title"
const val COLUMN_SONG_ARTIST = "artist"
const val COLUMN_SONG_IMAGE1 = "image1"
const val COLUMN_SONG_IMAGE2 = "image2"
const val COLUMN_SONG_IMAGE3 = "image3"
const val COLUMN_SONG_IMAGE4 = "image4"


/**
 * A data class (POJO) which represents a basic Song.
 *
 * This is a Room Entity and can be persisted in the database.
 */
@Entity(tableName = TABLE_NAME_SONG)
data class Song(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_SONG_ID)
    val id: String,

    @ColumnInfo(name = COLUMN_SONG_TITLE)
    var title: String,

    @ColumnInfo(name = COLUMN_SONG_ARTIST)
    var artist: String,

    @ColumnInfo(name = COLUMN_SONG_IMAGE1)
    var image_1: String,

    @ColumnInfo(name = COLUMN_SONG_IMAGE2)
    var image_2: String,

    @ColumnInfo(name = COLUMN_SONG_IMAGE3)
    var image_3: String,

    @ColumnInfo(name = COLUMN_SONG_IMAGE4)
    var image_4: String,
) {
    override fun toString(): String {
        return "Song(id='$id', title='$title', artist='$artist', image_1='$image_1', image_2='$image_2', image_3='$image_3', image_4='$image_4')"
    }
}
