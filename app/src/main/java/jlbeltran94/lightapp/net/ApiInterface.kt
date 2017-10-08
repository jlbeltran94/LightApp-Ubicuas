package jlbeltran94.lightapp.net

import io.reactivex.Observable
import jlbeltran94.lightapp.models.Data
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by jlbeltran94 on 7/10/17.
 */
interface ApiInterface{

    @POST("/")
    fun sendData(@Body data: Data): Observable<SimpleResponse>

}