package com.example.appbanmypham.retrofit;

import com.example.appbanmypham.model.NotiResponse;
import com.example.appbanmypham.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNofication {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization: key=AAAAw8iTQ_M:APA91bGYOMocQZFGWbXqDM6KUyM9pDRx_e7FTZtdW8V-dLoQ0FVRyVH0Jjp0fkVlHPEBa_ZvPBdM6QPWMtIsa0ShvoiHfkwZyhe1ErDxHmoGgNtkn8DHjcJnA01vAfSEzsYELJZ8s-mz"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNofitication(@Body NotiSendData data);
}
