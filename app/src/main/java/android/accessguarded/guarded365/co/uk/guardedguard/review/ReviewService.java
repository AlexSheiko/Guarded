package android.accessguarded.guarded365.co.uk.guardedguard.review;

import android.accessguarded.guarded365.co.uk.guardedguard.util.Sites;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ReviewService {

    @Headers("Content-Type: application/json")
    @GET("guards/daily/{userId}")
    Call<Sites> listGuards(
            @Path("userId") int userId);

}
