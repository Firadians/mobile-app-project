package com.example.kenangin.listener

import com.example.kenangin.model.CartModel

interface ICartLoadListerner {
    fun onLoadCartSuccess(cartModelList: List<CartModel>)
    fun onLoadCartFailed(message:String?)
}