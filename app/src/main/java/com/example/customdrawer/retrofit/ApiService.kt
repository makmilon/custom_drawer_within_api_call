package com.example.customdrawer.retrofit


import com.example.customdrawer.dataclass.MenuItemItem
import retrofit2.Call
import retrofit2.http.GET


interface ApiService {


    //target response

    @GET("/users")
    fun getUsers(): Call<List<MenuItemItem>>

}