package at.ac.tuwien.inso.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.tuwien.inso.model.Song
import at.ac.tuwien.inso.repository.SongRepository
import com.chaquo.python.PyException
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(
    private val songRepository: SongRepository
) : ViewModel() {
    val songs = songRepository
        .getSongs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var _prompt = MutableLiveData<String>()
    val prompt: LiveData<String> = _prompt

    private var _imageurls = MutableLiveData<List<String>>()

    private var _imageurl = MutableLiveData<String>()
    val imageurl: LiveData<String> = _imageurl

    private var _song = MutableLiveData<Song>()
    val song: LiveData<Song> = _song

    private var _keywords = MutableLiveData<String>()
    val keywords: LiveData<String> = _keywords


    // PostValue because of the Thread (async)
    fun setImageurls(imageurl: List<String>) {
        _imageurls.postValue(imageurl)
    }

    fun setImageurl(n: Int) {
        val imageurltmp = mutableListOf(
            song.value!!.image_1, song.value!!.image_2,
            song.value!!.image_3, song.value!!.image_4
        )
        _imageurl.value = imageurltmp[n]
    }

    fun setSong(song: Song) {
        _song.postValue(song)
    }
    fun setKeywords(keywords: String) {
        _keywords.postValue(keywords)
    }



    /**
     * Creates a new [Song] via the repository.
     *
     * @param song The [Song] which will be created.
     */
    fun insertSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) { songRepository.insert(song) }
    }

    // Functions that link the python methods
    fun pythonScriptMain(py: Python, prompt: String): List<String> {
        return songRepository.pythonScriptMain(py,prompt)
    }
    fun getLyrics(py: Python, prompt: String): String {
        return songRepository.getLyrics(py,prompt)
    }
    fun applyNLP(py: Python, lyrics: String): String {
        return songRepository.applyNLP(py,lyrics)
    }

    fun updateSong(value: List<String>, keyPrompt: String) {
        viewModelScope.launch(Dispatchers.IO) { songRepository.update(song.value!!, value, keyPrompt) }
    }
}
