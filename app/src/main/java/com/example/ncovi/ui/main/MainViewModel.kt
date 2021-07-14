package com.example.ncovi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ncovi.data.datasource.remote.api.ResultCovi
import com.example.ncovi.data.model.Recent
import com.example.ncovi.data.repo.CoviRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repo: CoviRepository) : ViewModel() {

    val recentCovi: Flow<ResultCovi<Recent>> get() = _recentCovi
    val historyCovi: Flow<ResultCovi<List<Recent>>> get() = _historyCovi
    val localData = repo.getAllHistories()
    private val _recentCovi = MutableStateFlow<ResultCovi<Recent>>(ResultCovi.loading(null))
    private val _historyCovi = MutableStateFlow<ResultCovi<List<Recent>>>(ResultCovi.loading(null))

    fun getRecent() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _recentCovi.value = ResultCovi.loading(null)
            try {
                val recent = repo.getRecent()
                _recentCovi.value = ResultCovi.success(recent)
            } catch (e: Exception) {
                _recentCovi.value = ResultCovi.error(null, e.message.toString())
            }
        }
    }

    fun getHistory(limit: Int, offset: Int) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _historyCovi.value = ResultCovi.loading(null)
            try {
                val history = repo.getHistory(limit, offset)
                _historyCovi.value = ResultCovi.success(history)
            } catch (e: Exception) {
                _historyCovi.value = ResultCovi.error(null, e.message.toString())
            }
        }
    }

    fun insertAll(data: List<Recent>) = viewModelScope.launch {
        withContext(Dispatchers.IO){
            data.forEach {
                repo.insert(it)
            }
        }
    }

    fun insert(entity: Recent) = viewModelScope.launch {
        repo.insert(entity)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repo.delete(id)
    }
}
