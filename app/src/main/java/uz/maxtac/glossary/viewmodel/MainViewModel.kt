package uz.maxtac.glossary.viewmodel


import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.maxtac.glossary.manager.ReadJSONFromAssets
import uz.maxtac.glossary.model.Word
import uz.maxtac.glossary.utils.Extensions.findObjectById
import uz.maxtac.glossary.utils.Resource
import uz.seppuku.vp.utils.SingleLiveEvent
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application

) : AndroidViewModel(application) {

    val TAG = "HomeViewModel"
    private val context = getApplication<Application>().applicationContext
    val gson = Gson()

    //products list livedata
    private var _words = SingleLiveEvent<Resource<List<Word>>>()
    val words: LiveData<Resource<List<Word>>> get() = _words

    //single product livedata
    private var _word = SingleLiveEvent<Resource<Word>>()
    val word: LiveData<Resource<Word>> get() = _word


    //list for put products from database
    private val wordsList = ArrayList<Word>()


    //object for get single product and return it
    private var singleWord = Word()

    val jsonWordsList = ReadJSONFromAssets(context, "words.json")

    // Определяем тип списка объектов
    val listType = object : TypeToken<List<Word>>() {}.type

    // Преобразуем строку JSON в список объектов
    val objectList: List<Word> = gson.fromJson(jsonWordsList, listType)

    fun getAllWords() = viewModelScope.launch(Dispatchers.IO) {
        _words.postValue(Resource.success(objectList))
    }


    fun getSingleWord(id: String) = viewModelScope.launch(Dispatchers.IO) {

        try {

            val word = objectList.findObjectById(id)
            if (word != null) {
                _word.postValue(Resource.success(word))
            }

        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }

    }


}