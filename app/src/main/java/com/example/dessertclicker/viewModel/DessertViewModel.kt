package com.example.dessertclicker.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.state.DessertUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel: ViewModel() {
    private val dessertUiState = MutableStateFlow(DessertUiState())
    val dessertState: StateFlow<DessertUiState> = dessertUiState.asStateFlow()


        var currentDessertPrice by mutableIntStateOf(Datasource.dessertList[dessertState.value.currentDessertIndex].price)
        private set

    var currentDessertImageId by  mutableIntStateOf(Datasource.dessertList[dessertState.value.currentDessertIndex].imageId)
        private set

    fun updateRevenue(currentPrice: Int) {
        dessertUiState.update { currentState -> currentState.copy(
            revenue = dessertState.value.revenue + currentPrice,
            dessertsSold = dessertState.value.dessertsSold + 1,
        )}
    }


    fun determineImageToShow(desserts: List<Dessert>){
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if ( dessertUiState.value.dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        currentDessertPrice = dessertToShow.price
        currentDessertImageId = dessertToShow.imageId

    }


}
