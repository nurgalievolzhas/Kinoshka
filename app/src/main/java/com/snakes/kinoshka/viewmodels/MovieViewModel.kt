package com.snakes.kinoshka.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.snakes.kinoshka.data.DataStoreRepository
import com.snakes.kinoshka.util.Constants
import com.snakes.kinoshka.util.Constants.API_KEY
import com.snakes.kinoshka.util.Constants.DEFAULT_LANGUAGE
import com.snakes.kinoshka.util.Constants.DEFAULT_PAGE
import com.snakes.kinoshka.util.Constants.QUERY_API_KEY
import com.snakes.kinoshka.util.Constants.QUERY_INCLUDE_ADULT
import com.snakes.kinoshka.util.Constants.QUERY_LANGUAGE
import com.snakes.kinoshka.util.Constants.QUERY_PAGE
import com.snakes.kinoshka.util.Constants.QUERY_SEARCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
): AndroidViewModel(application) {

    private var page = DEFAULT_PAGE

    val readPage  = dataStoreRepository.readPage

    fun savePage(page:String,pageId:Int){
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.savePage(page, pageId)
        }
    }

    fun applyQueries():HashMap<String,String>{
        val queries:HashMap<String,String> = HashMap()

        viewModelScope.launch {
            readPage.collect {
                page = it.selectedPage
            }
        }

        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_LANGUAGE] = "en-US"
        queries[QUERY_PAGE] = page

        return queries
    }

    fun applySearchQuery(query:String):HashMap<String,String>{
        val queries:HashMap<String,String> = HashMap()

        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_LANGUAGE] = DEFAULT_LANGUAGE
        queries[QUERY_SEARCH] = query
        queries[QUERY_PAGE] = DEFAULT_PAGE
        queries[QUERY_INCLUDE_ADULT] = "false"

        return queries
    }
}