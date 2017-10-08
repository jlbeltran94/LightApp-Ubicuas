package jlbeltran94.lightapp.net

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jlbeltran94 on 7/10/17.
 */
object ApiClient{

    val BASE_URL: String = "http://192.168.0.12:5000/"
    var retrofit: Retrofit ?= null


    private fun createInstance():Retrofit{
        if (retrofit == null){
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()
        }

        return retrofit!!
    }

    fun  getInstance(): ApiInterface{
        return createInstance().create(ApiInterface::class.java)
    }

}