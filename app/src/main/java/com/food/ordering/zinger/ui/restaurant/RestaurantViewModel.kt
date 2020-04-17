package com.food.ordering.zinger.ui.restaurant

import androidx.lifecycle.*
import com.food.ordering.zinger.data.local.Resource
import com.food.ordering.zinger.data.model.MenuItem
import com.food.ordering.zinger.data.retrofit.ItemRepository
import kotlinx.coroutines.launch

import java.net.UnknownHostException


class RestaurantViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    //Fetch total stats
    private val performFetchMenu = MutableLiveData<Resource<List<MenuItem>>>()
    val performFetchMenuStatus: LiveData<Resource<List<MenuItem>>>
        get() = performFetchMenu

    fun getMenu(shopId: String) {
        viewModelScope.launch {
            try {
                performFetchMenu.value = Resource.loading()
                val response = itemRepository.getMenu(shopId)
                if(response!=null){
                    if(!response.data.isNullOrEmpty()){
                        performFetchMenu.value = Resource.success(response.data)
                    }else{
                        performFetchMenu.value = Resource.empty()
                    }
                }
            } catch (e: Exception) {
                println("fetch stats failed ${e.message}")
                if (e is UnknownHostException) {
                    performFetchMenu.value = Resource.offlineError()
                } else {
                    performFetchMenu.value = Resource.error(e)
                }
            }
        }
    }

}