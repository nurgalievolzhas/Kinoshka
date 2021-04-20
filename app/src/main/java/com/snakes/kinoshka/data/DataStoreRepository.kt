package com.snakes.kinoshka.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.snakes.kinoshka.data.DataStoreRepository.PreferencesKeys.selectedPage
import com.snakes.kinoshka.data.DataStoreRepository.PreferencesKeys.selectedPageId
import com.snakes.kinoshka.util.Constants.DEFAULT_PAGE
import com.snakes.kinoshka.util.Constants.PREFERENCES_NAME
import com.snakes.kinoshka.util.Constants.PREFERENCES_PAGE
import com.snakes.kinoshka.util.Constants.PREFERENCES_PAGE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private object PreferencesKeys{
        val selectedPage = stringPreferencesKey(PREFERENCES_PAGE)
        val selectedPageId = intPreferencesKey(PREFERENCES_PAGE_ID)
    }

    private val dataStore:DataStore<Preferences> = context.dataStore

    suspend fun savePage(page:String,pageId:Int){
        dataStore.edit {preferences->
            preferences[selectedPage] = page
            preferences[selectedPageId] = pageId

        }
    }

    val readPage:Flow<Page> = dataStore.data
        .catch { exception->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map {preferences->
            val selectedPage = preferences[PreferencesKeys.selectedPage] ?: DEFAULT_PAGE
            val selectedPageId = preferences[PreferencesKeys.selectedPageId] ?: 0
            Page(selectedPage,selectedPageId)
        }

}
data class Page(
    val selectedPage:String,
    val selectedPageId:Int
)

