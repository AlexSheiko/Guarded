package android.accessguarded.guarded365.co.uk.guardedguard.login;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.guarded365.co.uk/")
                .build();

        LoginService service = retrofit.create(LoginService.class);
        Call<User> call = service.login("api_test", "api_test_password");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Log.d("LoginActivity", "onResponse: " + user.getUsername());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
