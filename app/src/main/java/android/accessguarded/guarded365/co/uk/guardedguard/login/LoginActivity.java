package android.accessguarded.guarded365.co.uk.guardedguard.login;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.accessguarded.guarded365.co.uk.guardedguard.guards.GuardsActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameField = (EditText) findViewById(R.id.usernameField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCredentials(
                        mUsernameField.getText().toString().trim(),
                        mPasswordField.getText().toString().trim());
            }
        });
    }

    private void validateCredentials(String username, String password) {
        if (username.isEmpty()) {
            mUsernameField.setError(getString(R.string.error_empty_username));
            return;
        }
        if (password.isEmpty()) {
            mPasswordField.setError(getString(R.string.error_empty_password));
            return;
        }
        login(username, password);
    }

    private void login(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.guarded365.co.uk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginService service = retrofit.create(LoginService.class);
        Call<User> call = service.login(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                boolean responseSuccessful = user.getId() != 0;
                if (responseSuccessful) {
                    // Save user session
                    saveUser(user);
                    // Navigate to the home screen
                    startActivity(new Intent(LoginActivity.this, GuardsActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void saveUser(User user) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("userId", user.getId()).apply();
    }
}