package uz.maxtac.glossary.manager

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.maxtac.glossary.model.Word

class MainPreferences(context: Context) {

    private val PREFS_NAME = "MyPreferences"
    private val KEY_MY_OBJECTS = "my_objects"

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveIds(ids: List<String>) {
        val idsSet = HashSet<String>(ids)
        preferences.edit().putStringSet(KEY_MY_OBJECTS, idsSet).apply()
    }

    fun getSavedIds(): List<String> {
        val idsSet = preferences.getStringSet(KEY_MY_OBJECTS, null)
        return idsSet?.toList() ?: emptyList()
    }

    fun addNewId(newId: String) {
        val existingIds = getSavedIds().toMutableList()
        existingIds.add(newId)
        saveIds(existingIds)
    }

    fun removeId(idToRemove: String) {
        val existingIds = getSavedIds().toMutableList()
        existingIds.remove(idToRemove)
        saveIds(existingIds)
    }
}