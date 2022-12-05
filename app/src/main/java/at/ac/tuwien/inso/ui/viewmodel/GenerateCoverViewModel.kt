package at.ac.tuwien.inso.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenerateCoverViewModel: ViewModel() {
    private var _prompt = MutableLiveData<String>()
    val prompt : LiveData<String> = _prompt;

    fun setPrompt(prompt : String){
        _prompt.value = prompt
    }
}