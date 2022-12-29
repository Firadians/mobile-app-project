package com.example.kenangin.listener

import com.example.kenangin.model.DrinkModel

interface IDrinkLoadListener {
    fun onDrinkLoadFailed(message:String?)
    fun onDrinkLoadSuccess(drinkModelList: List<DrinkModel>?)
}
