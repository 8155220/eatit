package uagrm.androideatitserver.Remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import uagrm.androideatitserver.Model.MyResponse;
import uagrm.androideatitserver.Model.Sender;

/**
 * Created by Shep on 10/29/2017.
 */

public interface APIService {
    @Headers(
        {
            "Content-Type:application/json",
            "Authorization:key=AIzaSyARNhTLfyGKkbvhKn6ko3F1-PGtfc7wq3I"
        }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
