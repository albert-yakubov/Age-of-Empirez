package com.stepasha.ageofempirez.retro

import com.stepasha.ageofempirez.models.Civilization
import com.stepasha.ageofempirez.models.Structure
import com.stepasha.ageofempirez.models.Technology
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface AgeOfEmpiresAPI {

    @GET("civilization/{id}")
    fun getCivilization(@Path("id") id: Int): Call<Civilization>

    @GET("structure/{id}")
    fun getStructure(@Path("id") id: Int): Call<Structure>

    @GET("technology/{id}")
    fun getTechnologies(@Path("id") id: Int): Call<Technology>

    class Factory {

        companion object {

            private const val BASE_URL = "https://age-of-empires-2-api.herokuapp.com/api/v1/"

            fun create(): AgeOfEmpiresAPI {



                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                return retrofit.create(AgeOfEmpiresAPI::class.java)
            }
        }
    }
}