package android.accessguarded.guarded365.co.uk.guardedguard.login;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
    }
}
