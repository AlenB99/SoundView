package at.ac.tuwien.inso.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(
    private val songRepository: SongRepository
) : ViewModel()
{
    val songs = songRepository
        .getSongs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var _prompt = MutableLiveData<String>()
    val prompt: LiveData<String> = _prompt

    private var _imageurls = MutableLiveData<List<String>>()
    val imageurls: LiveData<List<String>> = _imageurls

    private var _imageurl = MutableLiveData<String>()
    val imageurl: LiveData<String> = _imageurl

    private var _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song

    fun setPrompt(prompt: String) {
        _prompt.value = prompt
    }

    // PostValue because of the Thread (async)
    fun setImageurls(imageurl: List<String>) {
        _imageurls.postValue(imageurl)
    }

    fun setImageurl(n: Int) {
        _imageurl.value = _imageurls.value?.get(n)
    }

    fun setSong(song: Song) {
        _song.postValue(song)
    }

    /**
     * Uses the [SongRepository] to update the song list.
     * This will execute the API request and persist the result to the database.
     * When the database is updated, the view will automatically get notified
     * in the flow [songs].
     */
    fun refreshSongss() {
        // Launch I/O operation asynchronously and bind it to the Scope of the ViewModel.
        viewModelScope.launch(Dispatchers.IO) {
            songRepository.refreshSongs()
        }
    }

    /**
     * Creates a new [Song] via the repository.
     *
     * @param song The [Song] which will be created.
     */
    fun insertSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) { songRepository.insert(song) }
    }

    /**
     * Deletes the given song through the repository.
     *
     * @param song The [Song] to delete.
     */
    fun deleteSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) { songRepository.delete(song) }
    }

    /**
     * Loads a single [Song] by its ID.
     *
     * @param id The ID of the song.
     * @return A [Song] if one exists for the given ID. Otherwise null.
     */
    fun getSongById(id: String): Song? {
        return songs.value.firstOrNull { it.id == id }
    }

}
