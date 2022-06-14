package com.example.dummyproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dummyproject.base.BaseViewModel
import com.example.dummyproject.data.Repository
import com.example.dummyproject.data.database.entites.RepositoriesEntity
import com.example.dummyproject.ui.model.RepositoriesResponse
import com.example.dummyproject.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    /**======== repositories Local Database MutableLiveData ========*/
    private var _readRepositories: MutableLiveData<List<RepositoriesEntity>> =
        MutableLiveData()
    val readRepositories: LiveData<List<RepositoriesEntity>> =
        _readRepositories

    /**======== repositories Remote Database MutableLiveData ========*/
    private var _repositoriesResponse: MutableLiveData<NetworkResult<RepositoriesResponse>> =
        MutableLiveData()
    val repositoriesResponse: LiveData<NetworkResult<RepositoriesResponse>> =
        _repositoriesResponse


    init {
        getRepositories()
    }

    /**======== API call Methods ========*/

    /**======== get Repositories API call Methods ========*/
    fun getRepositories() = viewModelScope.launch(Dispatchers.IO) {
        getRepositoriesSafeCall()
    }


    private suspend fun getRepositoriesSafeCall() {
        _repositoriesResponse.postValue(NetworkResult.Loading())
        try {
            val response = repository.remote.getRepositories()
            if (response.isSuccessful)
                response.body()?.let {
                    offlineCacheRepositories(it)

                }
            else {
                readRepositoriesFromCache(response.message().toString())
            }


        } catch (e: Exception) {
            readRepositoriesFromCache(e.message.toString())
        }
    }

    /**======== save data in local database ========*/
    private fun insertRepositories(
        repositoriesEntity: RepositoriesEntity,
        repositoriesResponse: RepositoriesResponse
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRepositories(repositoriesEntity)
            repository.local.readRepositories().collect {
                _readRepositories.postValue(it)
                _repositoriesResponse.postValue(NetworkResult.Success(repositoriesResponse))
            }
        }


    private fun offlineCacheRepositories(repositoriesResponse: RepositoriesResponse) {
        val recipesEntity = RepositoriesEntity(repositoriesResponse)
        insertRepositories(recipesEntity, repositoriesResponse)
    }


    /**========read data from local database ========*/
    private fun readRepositoriesFromCache(message: String) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.readRepositories().collect {
                _readRepositories.postValue(it)
                _repositoriesResponse.postValue(NetworkResult.Error(message))
            }
        }

}

