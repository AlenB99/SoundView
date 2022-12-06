package at.ac.tuwien.inso.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GenerateCoverViewModel: ViewModel() {
    private var _prompt = MutableLiveData<String>()
    val prompt : LiveData<String> = _prompt;

    private var _imageurls = MutableLiveData<List<String>>()
    val imageurls : LiveData<List<String>> = _imageurls;

    private var _imageurl = MutableLiveData<String>()
    val imageurl : LiveData<String> = _imageurl;

    fun setPrompt(prompt : String){
        _prompt.value = prompt
    }

    //PostValue because of the Thread (async)
    fun setImageurls(imageurl : List<String>){
        _imageurls.postValue(imageurl)
    }

    fun setImageurl(n : Int) {
        _imageurl.value = _imageurls.value?.get(n)
    }

}