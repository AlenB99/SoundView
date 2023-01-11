package at.ac.tuwien.inso.repository

import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.persistance.dao.SongDao
import com.chaquo.python.PyException
import com.chaquo.python.Python
import kotlinx.coroutines.flow.Flow

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
     * Updates an existing [Song].
     */
    suspend fun update(song: Song, value: List<String>, keyPrompt: String) {
        // Initially add it to the database, so no refresh is needed
        if (value.isNotEmpty()) {
            song.image_1 = value[0]
            song.image_2 = value[1]
            song.image_3 = value[2]
            song.image_4 = value[3]
        }
        if (value.isNotEmpty() || keyPrompt.isNotEmpty()) {
            song.keyPrompt = keyPrompt
        }
        songDao.update(song)
    }


    fun pythonScriptMain(py: Python, prompt: String): List<String> {
        var urlList: List<String> = listOf("", "", "", "")
        val module = py.getModule("image_generate")
        try {
            val url = module.callAttr("image_generate", prompt)
                .toString()
            urlList = url.split(",").map {
                it.trim()
                    .replace("[", "").replace("]", "")
                    .replace("'", "")
            }
            return urlList
            // From python script we get a PyObject, which is converted to a string. Afterwards
            // its added to urlList, so that we can select the urls through indexing
        } catch (_: PyException) {
        }
        return urlList
    }
    fun getLyrics(py: Python, prompt: String): String {
        val module = py.getModule("image_generate")
        val lyrics = "No lyrics have been found"
        try {

            return module.callAttr("get_song_lyrics", prompt)
                .toString()
        } catch (_: PyException) {
        }

        return lyrics
    }
    fun applyNLP(py: Python, lyrics: String): String {
        val module = py.getModule("image_generate")
        val keywords = "No keywords have been found"
        if (lyrics != "No lyrics have been found") {
            try {

                return module.callAttr("nlp_on_lyrics", lyrics)
                    .toString()
            } catch (_: PyException) {
            }
        }
        return keywords
    }




}
