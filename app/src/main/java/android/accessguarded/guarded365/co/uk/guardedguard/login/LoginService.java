package android.accessguarded.guarded365.co.uk.guardedguard.login;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService {

    @GET("login")
    Call<User> login(
            @Query("username") String username,
            @Query("password") String password);

}
