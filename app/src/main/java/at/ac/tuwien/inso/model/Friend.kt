package at.ac.tuwien.inso.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME_FRIEND = "friends"
const val COLUMN_FRIEND_ID = "id"
const val COLUMN_FRIEND_NAME = "name"
const val COLUMN_FRIEND_SCORE = "score"

/**
 * A data class (POJO) which represents a basic friend.
 *
 * This is a Room Entity and can be persisted in the database.
 */
@Entity(tableName = TABLE_NAME_FRIEND)
data class Friend(

    @PrimaryKey
    @ColumnInfo(name = COLUMN_FRIEND_ID)
    val id: Int,

    @ColumnInfo(name = COLUMN_FRIEND_NAME)
    var name: String,

    @ColumnInfo(name = COLUMN_FRIEND_SCORE)
    var score: Int
)
