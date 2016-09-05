package android.accessguarded.guarded365.co.uk.guardedguard.review;

import android.accessguarded.guarded365.co.uk.guardedguard.login.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ReviewService {

    @GET("login")
    Call<User> login(
            @Query("username") String username,
            @Query("password") String password);

}
