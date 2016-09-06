package android.accessguarded.guarded365.co.uk.guardedguard.review;

import android.accessguarded.guarded365.co.uk.guardedguard.login.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReviewService {

    @GET("guards/daily/{userId}")
    Call<User> listGuards(
            @Path("userId") int userId);

}
