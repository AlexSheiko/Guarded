package android.accessguarded.guarded365.co.uk.guardedguard.guardreview;

import android.accessguarded.guarded365.co.uk.guardedguard.util.ReviewResponse;
import android.accessguarded.guarded365.co.uk.guardedguard.util.Sites;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {

    @Headers("Content-Type: application/json")
    @GET("guards/daily/{userId}")
    Call<Sites> listGuards(
            @Path("userId") int userId);

    @Headers("Content-Type: application/json")
    @POST("dailyreview/submit")
    Call<ReviewResponse> submitReview(
            @Body Review review);

}
